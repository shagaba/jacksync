package com.shagaba.jacksync.service;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shagaba.jacksync.AddOperation;
import com.shagaba.jacksync.ReplaceOperation;
import com.shagaba.jacksync.SyncCapsule;
import com.shagaba.jacksync.post.dto.Post;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class LocalSyncServiceTest {
	
	private ObjectMapper mapper = null;

	private LocalSyncService localSyncService = null;
	
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
    	localSyncService = new LocalSyncService(mapper);
    }

    @Test
    public void serverSyncV1() throws Exception {
    	// server post
    	Post serverPostV1 = new Post();
    	serverPostV1.setVersion(1L);

    	// client post
    	Post clientPostV1 = new Post();
    	clientPostV1.setVersion(1L);
    	// 1st update
    	clientPostV1.setTitle("my test title");
    	
        // expected target
    	Post targetPost = new Post();
    	targetPost.setTitle(clientPostV1.getTitle());
    	targetPost.setVersion(1L);

    	// sync capsule & operations
    	SyncCapsule syncCapsule = new SyncCapsule();
    	syncCapsule.setVersion(1L);
    	// operations
    	AddOperation addOperation = new AddOperation("/title", mapper.valueToTree(targetPost.getTitle()));
        syncCapsule.setOperations(Arrays.asList(addOperation));
    	// target checksum
    	syncCapsule.setTargetChecksum(ChecksumUtils.computeChecksum(mapper.writeValueAsString(targetPost)));
        
    	// server sync
        Post postV2 = localSyncService.serverSync(serverPostV1, syncCapsule);
        
        Assert.assertThat(postV2, equalTo(targetPost));
    }
    
    @Test
    public void clientSyncV2() throws Exception {
    	// client post
    	Post clientPostV1 = new Post();
    	clientPostV1.setVersion(1L);
    	clientPostV1.setTitle("my test title");
    	
    	// server post
    	Post serverPostV1 = new Post();
    	serverPostV1.setVersion(2L);
    	// 1st update
    	serverPostV1.setTitle("my 2nd test title");

        // expected target
    	Post targetPost = new Post();
    	targetPost.setTitle(serverPostV1.getTitle());
    	targetPost.setVersion(2L);

    	// sync capsule & operations
    	SyncCapsule syncCapsule = new SyncCapsule();
    	syncCapsule.setVersion(1L);
    	syncCapsule.setApprovedVersion(2L);
    	// operations
    	AddOperation addOperation = new AddOperation("/title", mapper.valueToTree(targetPost.getTitle()));
    	ReplaceOperation replaceOperation = new ReplaceOperation("/version", mapper.valueToTree(2));
        syncCapsule.setOperations(Arrays.asList(addOperation, replaceOperation));
    	// target checksum
    	syncCapsule.setTargetChecksum(ChecksumUtils.computeChecksum(mapper.writeValueAsString(targetPost)));
        
    	// server sync
        Post postV2 = localSyncService.clientSync(clientPostV1, syncCapsule);
        
        Assert.assertThat(postV2, equalTo(targetPost));
    }
    
}
