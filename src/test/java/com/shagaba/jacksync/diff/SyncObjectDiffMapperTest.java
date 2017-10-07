package com.shagaba.jacksync.diff;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shagaba.jacksync.diff.processor.MergeOperationDiffProcessor;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.post.dto.Author;
import com.shagaba.jacksync.post.dto.Post;
import com.shagaba.jacksync.post.dto.Section;
import com.shagaba.jacksync.sync.SyncData;
import com.shagaba.jacksync.sync.SyncObject;

public class SyncObjectDiffMapperTest {
	private ObjectMapper mapper;

	private SyncObjectDiffMapper syncObjectDiffMapper;

	

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
    	
    	syncObjectDiffMapper = new SyncObjectDiffMapper(mapper);
    }

    @Test(expected=IllegalArgumentException.class)
	public void diffFailsObjectMapperIsNull() throws Exception {
		// operations simple diff
    	syncObjectDiffMapper = new SyncObjectDiffMapper(null);
	}

    @Test(expected=IllegalArgumentException.class)
	public void diffFailsDiffProcessorIsNull() throws Exception {
		// operations simple diff
    	syncObjectDiffMapper = new SyncObjectDiffMapper(mapper, null);
	}

    @Test(expected=IllegalArgumentException.class)
	public void diffFailsSourceIsNull() throws Exception {
		// operations simple diff
    	Post source = null;
    	Post target = new Post();
	
    	syncObjectDiffMapper.diff(new SyncObject<Post>(1L, source), new SyncObject<Post>(1L, target));
	}

    @Test(expected=IllegalArgumentException.class)
	public void diffFailsTargetIsNull() throws Exception {
		// operations simple diff
    	Post source = new Post();
    	Post target = null;
	
    	syncObjectDiffMapper.diff(new SyncObject<Post>(1L, source), new SyncObject<Post>(1L, target));
	}

    @Test
	public void addTitle() throws Exception {
		Post postV1 = new Post();
	
		Post postV1_1 = new Post();
		postV1_1.setTitle("my test title");
	
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
	}

    @Test
    public void addAuthor() throws Exception {
    	Post postV1 = new Post();

		Post postV1_1 = new Post();
		postV1_1.setAuthor(new Author("james", "bond", "james.bond@007.com"));
		
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void addAuthorFirstName() throws Exception {
    	Post postV1 = new Post();
    	postV1.setAuthor(new Author());

		Post postV1_1 = new Post();
		postV1_1.setAuthor(new Author("james", null, null));

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void addFirstSection() throws Exception {
    	Post postV1 = new Post();

    	Post postV1_1 = new Post();
    	postV1_1.setSections(new ArrayList<Section>());
    	postV1_1.getSections().add(new Section("section-1", null));

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void addMiddleSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));

    	Post postV1_1 = new Post();
    	postV1_1.setSections(new ArrayList<Section>());
    	postV1_1.getSections().add(new Section("section-1", null));
    	postV1_1.getSections().add(new Section("section-2", null));
    	postV1_1.getSections().add(new Section("section-2.5", null));
    	postV1_1.getSections().add(new Section("section-3", null));
    	postV1_1.getSections().add(new Section("section-4", null));

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void addLastSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));

    	Post postV1_1 = new Post();
    	postV1_1.setSections(new ArrayList<Section>());
    	postV1_1.getSections().add(new Section("section-1", null));
    	postV1_1.getSections().add(new Section("section-2", null));
    	postV1_1.getSections().add(new Section("section-3", null));
    	postV1_1.getSections().add(new Section("section-4", null));
    	postV1_1.getSections().add(new Section("section-5", null));

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void removeTitle() throws Exception {
		Post postV1 = new Post();
		postV1.setTitle("my test title");
	
		Post postV1_1 = new Post();
		
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }
    
    @Test
    public void removeAuthor() throws Exception {
		Post postV1 = new Post();
		postV1.setAuthor(new Author("james", "bond", "james.bond@007.com"));
		
    	Post postV1_1 = new Post();

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void removeAuthorFirstName() throws Exception {
		Post postV1 = new Post();
		postV1.setAuthor(new Author("james", null, null));

    	Post postV1_1 = new Post();
    	postV1_1.setAuthor(new Author());

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void removeSections() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));

    	Post postV1_1 = new Post();

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }
    
    @Test
    public void removeFirstSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));

    	Post postV1_1 = new Post();

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void removeMiddleSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-2.5", null));
    	postV1.getSections().add(new Section("section-3", null));
    	postV1.getSections().add(new Section("section-4", null));

    	Post postV1_1 = new Post();
    	postV1_1.setSections(new ArrayList<Section>());
    	postV1_1.getSections().add(new Section("section-1", null));
    	postV1_1.getSections().add(new Section("section-2", null));
    	postV1_1.getSections().add(new Section("section-3", null));
        postV1_1.getSections().add(new Section("section-4", null));

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void removeLastSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
    	postV1.getSections().add(new Section("section-4", null));
    	postV1.getSections().add(new Section("section-5", null));

    	Post postV1_1 = new Post();
    	postV1_1.setSections(new ArrayList<Section>());
    	postV1_1.getSections().add(new Section("section-1", null));
    	postV1_1.getSections().add(new Section("section-2", null));
    	postV1_1.getSections().add(new Section("section-3", null));
        postV1_1.getSections().add(new Section("section-4", null));

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, postV1), new SyncObject<Post>(1L, postV1_1));
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(jacksyncData.getOperations(), hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	    Assert.assertThat(mapper.treeToValue(syncdJsonNode, Post.class), equalTo(postV1_1));
    }

    @Test
    public void complicated() throws Exception {
    	
    	syncObjectDiffMapper = new SyncObjectDiffMapper(mapper, new MergeOperationDiffProcessor());
    	
    	Post source = new Post();
    	source.setId("1");
    	source.setVersion(1L);
    	source.setAuthor(new Author("firstName", "lastName", "email"));
    	source.setSections(new ArrayList<Section>());
    	source.getSections().add(new Section("section-1", null));
    	source.getSections().add(new Section("section-2", null));
    	source.getSections().add(new Section("section-x", null));
    	source.getSections().add(new Section("section-4", null));
    	source.getSections().add(new Section("section-5", null));

    	Post target = new Post();
    	target.setId("1");
    	target.setTitle("A Title");
    	target.setVersion(2L);
    	target.setAuthor(new Author("firstName", "lastName", "email@email.com"));
    	target.setSections(new ArrayList<Section>());
    	target.getSections().add(new Section("section-1", null));
    	target.getSections().add(new Section("section-2", null));
    	target.getSections().add(new Section("section-3", null));
        target.getSections().add(new Section("section-4 update", null, "private note"));

        SyncData jacksyncData = syncObjectDiffMapper.diff(new SyncObject<Post>(1L, source), new SyncObject<Post>(1L, target));
        
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : jacksyncData.getOperations()) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

}
