package com.shagaba.jacksync.diff;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.BaseTest;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.support.dto.Author;
import com.shagaba.jacksync.support.dto.Post;
import com.shagaba.jacksync.support.dto.Section;

public class ObjectDiffMapperTest extends BaseTest {

	private DiffMapper diffMapper;
    
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	
    	diffMapper = new ObjectDiffMapper(mapper);
    }

    @Test(expected=IllegalArgumentException.class)
	public void diffFailsObjectMapperIsNull() throws Exception {
		// operations simple diff
    	diffMapper = new ObjectDiffMapper(null);
	}

    @Test(expected=IllegalArgumentException.class)
	public void diffFailsDiffProcessorIsNull() throws Exception {
		// operations simple diff
    	diffMapper = new ObjectDiffMapper(mapper, null);
	}

    @Test(expected=IllegalArgumentException.class)
	public void diffFailsSourceIsNull() throws Exception {
		// operations simple diff
    	Post source = null;
    	Post target = new Post();
	
		diffMapper.diff(source, target);
	}

    @Test(expected=IllegalArgumentException.class)
	public void diffFailsTargetIsNull() throws Exception {
		// operations simple diff
    	Post source = new Post();
    	Post target = null;
	
		diffMapper.diff(source, target);
	}

    @Test
	public void addTitle() throws Exception {
		Post source = new Post();
	
		Post target = new Post();
		target.setTitle("my test title");
	
		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
	}

    @Test
    public void addAuthor() throws Exception {
    	Post source = new Post();

		Post target = new Post();
		target.setAuthor(new Author("james", "bond", "james.bond@007.com"));
		
		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void addAuthorFirstName() throws Exception {
    	Post source = new Post();
    	source.setAuthor(new Author());

		Post target = new Post();
		target.setAuthor(new Author("james", null, null));

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void addFirstSection() throws Exception {
    	Post source = new Post();

    	Post target = new Post();
    	target.setSections(new ArrayList<Section>());
    	target.getSections().add(new Section("section-1", null));

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void addMiddleSection() throws Exception {
    	Post source = new Post();
    	source.setSections(new ArrayList<Section>());
    	source.getSections().add(new Section("section-1", null));
    	source.getSections().add(new Section("section-2", null));
    	source.getSections().add(new Section("section-3", null));
        source.getSections().add(new Section("section-4", null));

    	Post target = new Post();
    	target.setSections(new ArrayList<Section>());
    	target.getSections().add(new Section("section-1", null));
    	target.getSections().add(new Section("section-2", null));
    	target.getSections().add(new Section("section-2.5", null));
    	target.getSections().add(new Section("section-3", null));
    	target.getSections().add(new Section("section-4", null));

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void addLastSection() throws Exception {
    	Post source = new Post();
    	source.setSections(new ArrayList<Section>());
    	source.getSections().add(new Section("section-1", null));
    	source.getSections().add(new Section("section-2", null));
    	source.getSections().add(new Section("section-3", null));
        source.getSections().add(new Section("section-4", null));

    	Post target = new Post();
    	target.setSections(new ArrayList<Section>());
    	target.getSections().add(new Section("section-1", null));
    	target.getSections().add(new Section("section-2", null));
    	target.getSections().add(new Section("section-3", null));
    	target.getSections().add(new Section("section-4", null));
    	target.getSections().add(new Section("section-5", null));

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void removeTitle() throws Exception {
		Post source = new Post();
		source.setTitle("my test title");
	
		Post target = new Post();
		
		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		
	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }
    
    @Test
    public void removeAuthor() throws Exception {
		Post source = new Post();
		source.setAuthor(new Author("james", "bond", "james.bond@007.com"));
		
    	Post target = new Post();

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void removeAuthorFirstName() throws Exception {
		Post source = new Post();
		source.setAuthor(new Author("james", null, null));

    	Post target = new Post();
    	target.setAuthor(new Author());

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void removeSections() throws Exception {
    	Post source = new Post();
    	source.setSections(new ArrayList<Section>());
    	source.getSections().add(new Section("section-1", null));
    	source.getSections().add(new Section("section-2", null));
    	source.getSections().add(new Section("section-3", null));

    	Post target = new Post();

    	List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));   	
    }
    
    @Test
    public void removeFirstSection() throws Exception {
    	Post source = new Post();
    	source.setSections(new ArrayList<Section>());
    	source.getSections().add(new Section("section-1", null));

    	Post target = new Post();

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void removeMiddleSection() throws Exception {
    	Post source = new Post();
    	source.setSections(new ArrayList<Section>());
    	source.getSections().add(new Section("section-1", null));
    	source.getSections().add(new Section("section-2", null));
    	source.getSections().add(new Section("section-2.5", null));
    	source.getSections().add(new Section("section-3", null));
    	source.getSections().add(new Section("section-4", null));

    	Post target = new Post();
    	target.setSections(new ArrayList<Section>());
    	target.getSections().add(new Section("section-1", null));
    	target.getSections().add(new Section("section-2", null));
    	target.getSections().add(new Section("section-3", null));
        target.getSections().add(new Section("section-4", null));

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void removeLastSection() throws Exception {
    	Post source = new Post();
    	source.setSections(new ArrayList<Section>());
    	source.getSections().add(new Section("section-1", null));
    	source.getSections().add(new Section("section-2", null));
    	source.getSections().add(new Section("section-3", null));
    	source.getSections().add(new Section("section-4", null));
    	source.getSections().add(new Section("section-5", null));

    	Post target = new Post();
    	target.setSections(new ArrayList<Section>());
    	target.getSections().add(new Section("section-1", null));
    	target.getSections().add(new Section("section-2", null));
    	target.getSections().add(new Section("section-3", null));
        target.getSections().add(new Section("section-4", null));

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(operations, hasSize(1));
	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

    @Test
    public void complicated() throws Exception {
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

		List<PatchOperation> operations = diffMapper.diff(source, target);
		// operations simple diff
		JsonNode sourceJsonNode = mapper.valueToTree(source);
		JsonNode targetJsonNode = mapper.valueToTree(target);
		JsonNode syncdJsonNode = sourceJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}

	    Assert.assertThat(syncdJsonNode, equalTo(targetJsonNode));
    }

}
