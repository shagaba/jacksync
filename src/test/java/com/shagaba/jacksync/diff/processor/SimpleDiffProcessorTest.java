package com.shagaba.jacksync.diff.processor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.post.dto.Author;
import com.shagaba.jacksync.post.dto.Post;
import com.shagaba.jacksync.post.dto.Section;

public class SimpleDiffProcessorTest {

	private ObjectMapper mapper;

	private SimpleDiffProcessor simpleDiffProcessor;

	

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
    	
    	simpleDiffProcessor = new SimpleDiffProcessor();
    }

    @Test
	public void addTitle() throws Exception {
		Post postV1 = new Post();
	
		Post postV1_1 = new Post();
		postV1_1.setTitle("my test title");
	
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	}

    @Test
    public void addAuthor() throws Exception {
    	Post postV1 = new Post();

		Post postV1_1 = new Post();
		postV1_1.setAuthor(new Author("james", "bond", "james.bond@007.com"));
		
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void removeTitle() throws Exception {
		Post postV1 = new Post();
		postV1.setTitle("my test title");
	
		Post postV1_1 = new Post();
		
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);
		
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }
    
    @Test
    public void removeAuthor() throws Exception {
		Post postV1 = new Post();
		postV1.setAuthor(new Author("james", "bond", "james.bond@007.com"));
		
    	Post postV1_1 = new Post();

		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));   	
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
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
	
		List<PatchOperation> operations = simpleDiffProcessor.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

}
