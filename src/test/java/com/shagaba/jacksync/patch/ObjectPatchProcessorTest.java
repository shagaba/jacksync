package com.shagaba.jacksync.patch;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shagaba.jacksync.operation.AddOperation;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.operation.ReplaceOperation;
import com.shagaba.jacksync.post.dto.Post;
import com.shagaba.jacksync.utils.JacksonUtils;

public class ObjectPatchProcessorTest {

	private ObjectMapper mapper = null;

	private PatchProcessor patchProcessor = null;
	
    public ObjectMapper newObjectMapper() {
        ObjectMapper jacksonObjectMapper = new ObjectMapper();

        // - SerializationFeature for changing how JSON is written

        // to allow serialization of "empty" POJOs (no properties to serialize)
        // (without this setting, an exception is thrown in those cases)
        jacksonObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // Feature that determines whether Map entries with null values are to be serialized (true) or not (false)
        jacksonObjectMapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        // only properties with non-null
        jacksonObjectMapper.setSerializationInclusion(Include.NON_NULL);

        // - DeserializationFeature for changing how JSON is read as POJOs:

        // to prevent exception when encountering unknown property:
        jacksonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // default: java.util.Date serialized as textual (ISO-8601) values
        return jacksonObjectMapper;
    }
    
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	patchProcessor = new ObjectPatchProcessor(mapper);
    }

    @Test
    public void serverSyncV1() throws Exception {
    	// server post
    	Post postV1 = new Post();
    	postV1.setVersion(1L);

    	// operations
    	PatchOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree("my test title"));
    	List<PatchOperation> operations = Arrays.asList(addOperation);
    	
    	// expected target
    	Post targetPost = new Post();
    	targetPost.setTitle("my test title");
    	targetPost.setVersion(1L);

    	// server patch
        Post postV2 = patchProcessor.patch(postV1, operations);
        
        Assert.assertThat(postV2, equalTo(targetPost));
    }
    
    @Test
    public void clientSyncV2() throws Exception {
    	// client post
    	Post postV1 = new Post();
    	postV1.setVersion(1L);
    	postV1.setTitle("my test title");
    	
    	// server post
    	Post serverPostV1 = new Post();
    	serverPostV1.setVersion(2L);
    	// 1st update
    	serverPostV1.setTitle("my 2nd test title");

    	// operations
    	PatchOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree("my 2nd test title"));
    	PatchOperation replaceOperation = new ReplaceOperation(JacksonUtils.toJsonPointer("/version"), mapper.valueToTree(2));
    	List<PatchOperation> operations = Arrays.asList(addOperation, replaceOperation);
        
        // expected target
    	Post targetPost = new Post();
    	targetPost.setTitle(serverPostV1.getTitle());
    	targetPost.setVersion(2L);

    	// server patch
        Post postV2 = patchProcessor.patch(serverPostV1, operations);
        
        Assert.assertThat(postV2, equalTo(targetPost));
    }
    
}
