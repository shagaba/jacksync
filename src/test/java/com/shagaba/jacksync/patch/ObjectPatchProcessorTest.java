package com.shagaba.jacksync.patch;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shagaba.jacksync.BaseTest;
import com.shagaba.jacksync.diff.ObjectDiffMapper;
import com.shagaba.jacksync.operation.AddOperation;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.operation.ReplaceOperation;
import com.shagaba.jacksync.support.dto.Author;
import com.shagaba.jacksync.support.dto.Post;
import com.shagaba.jacksync.support.dto.Section;
import com.shagaba.jacksync.utils.JacksonUtils;

public class ObjectPatchProcessorTest extends BaseTest {

	private PatchProcessor patchProcessor = null;
	
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

    @Test
    public void clientSyncV3() throws Exception {
    	// client post
    	Post postV1 = new Post();
    	postV1.setVersion(1L);
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
    	
    	// expected post
    	Post postV2 = new Post();
    	postV2.setVersion(2L);
    	postV2.setTitle("my 2nd test title");
    	postV2.setSections(new ArrayList<Section>());
    	postV2.getSections().add(new Section("section-1", null));
    	postV2.getSections().add(new Section("section-3", null));

    	// [{"op":"replace","path":"/version","value":2},{"op":"replace","path":"/title","value":"my 2nd test title"},{"op":"remove","path":"/sections/1"}]
    	List<PatchOperation> operations = (new ObjectDiffMapper(mapper)).diff(postV1, postV2);
    	String jsonOperations = mapper.writerFor(new TypeReference<List<PatchOperation>>(){}).writeValueAsString(operations);
    	
    	// server patch
        Post postV2_1 = patchProcessor.patch(postV1, operations);
        Post postV2_2 = patchProcessor.patch(postV1, jsonOperations);

        Assert.assertThat(postV2_1, equalTo(postV2));
        Assert.assertThat(postV2_2, equalTo(postV2));
    }

    @Test
    public void clientSyncV4() throws Exception {
    	// client post
    	Post postV1 = new Post();
    	postV1.setId("007");
    	postV1.setTitle("Diamonds Are Forever");
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
    	
    	// expected post
    	Post postV2 = new Post();
    	postV2.setId("007");
    	postV2.setTitle("Diamonds Are Forever");
    	postV2.setAuthor(new Author("james", "bond", "james.bond@mi6.com"));
    	postV2.setSections(new ArrayList<Section>());
    	postV2.getSections().add(new Section("section-1", null));
    	postV2.getSections().add(new Section("section-3", null));
    	postV2.setTags(Arrays.asList("007", "Sean Connery", "action"));

    	List<PatchOperation> operations = (new ObjectDiffMapper(mapper)).diff(postV1, postV2);
    	String jsonOperations = mapper.writerFor(new TypeReference<List<PatchOperation>>(){}).writeValueAsString(operations);
    	
    	// server patch
        Post postV2_1 = patchProcessor.patch(postV1, operations);
        Post postV2_2 = patchProcessor.patch(postV1, jsonOperations);

        Assert.assertThat(postV2_1, equalTo(postV2));
        Assert.assertThat(postV2_2, equalTo(postV2));
    }

}
