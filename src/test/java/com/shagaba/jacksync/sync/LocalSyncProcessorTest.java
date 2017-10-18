package com.shagaba.jacksync.sync;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.shagaba.jacksync.BaseTest;
import com.shagaba.jacksync.operation.AddOperation;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.operation.ReplaceOperation;
import com.shagaba.jacksync.support.dto.Post;
import com.shagaba.jacksync.utils.ChecksumUtils;
import com.shagaba.jacksync.utils.JacksonUtils;

public class LocalSyncProcessorTest extends BaseTest {

	private SyncProcessor syncProcessor = null;
	
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	this.syncProcessor = new LocalSyncProcessor(mapper);
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

    	// sync syncData & operations
    	SyncData syncData = new SyncData();
    	syncData.setVersion(1L);
    	// operations
    	AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(targetPost.getTitle()));
        syncData.setOperations(Arrays.asList((PatchOperation) addOperation));
    	// target checksum
    	syncData.setTargetChecksum(ChecksumUtils.computeChecksum(mapper.writeValueAsString(targetPost)));
        
    	// server sync
    	SyncObject<Post> syncPostV2 = syncProcessor.clientSync(new SyncObject<Post>(1L, serverPostV1), syncData);
        
        Assert.assertThat(syncPostV2.getObject(), equalTo(targetPost));
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

    	// sync syncData & operations
    	SyncData syncData = new SyncData();
    	syncData.setVersion(1L);
    	syncData.setMasterVersion(2L);
    	// operations
    	AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(targetPost.getTitle()));
    	ReplaceOperation replaceOperation = new ReplaceOperation(JacksonUtils.toJsonPointer("/version"), mapper.valueToTree(2));
    	List<PatchOperation> operations = Arrays.asList((PatchOperation) addOperation, (PatchOperation) replaceOperation);
        syncData.setOperations(operations);
    	// target checksum
    	syncData.setTargetChecksum(ChecksumUtils.computeChecksum(mapper.writeValueAsString(targetPost)));
        
    	// server sync
    	SyncObject<Post> syncPostV2 = syncProcessor.masterSync(new SyncObject<Post>(1L, clientPostV1), syncData);
        
        Assert.assertThat(syncPostV2.getObject(), equalTo(targetPost));
    }
    

}
