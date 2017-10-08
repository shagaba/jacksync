package com.shagaba.jacksync.diff.strategy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.BaseTest;
import com.shagaba.jacksync.diff.strategy.MergeOperationDiffStrategy;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.support.dto.Author;
import com.shagaba.jacksync.support.dto.Post;
import com.shagaba.jacksync.support.dto.Section;

public class MergeOperationDiffStrategyTest extends BaseTest {

	private MergeOperationDiffStrategy mergeOperationDiffStrategy;

    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	
    	mergeOperationDiffStrategy = new MergeOperationDiffStrategy();
    }

    @Test
	public void addTitle() throws Exception {
		Post postV1 = new Post();
	
		Post postV1_1 = new Post();
		postV1_1.setTitle("my test title");
	
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(postV1);
		JsonNode targetJsonNode = mapper.valueToTree(postV1_1);
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);
		
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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);
		
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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

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
	
		List<PatchOperation> operations = mergeOperationDiffStrategy.diff(sourceJsonNode, targetJsonNode);

		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

}
