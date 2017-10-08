package com.shagaba.jacksync.operation;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.BaseTest;
import com.shagaba.jacksync.support.dto.Author;
import com.shagaba.jacksync.support.dto.Post;
import com.shagaba.jacksync.support.dto.Section;
import com.shagaba.jacksync.utils.JacksonUtils;

public class RemoveOperationTest extends BaseTest {
	
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	
    }

    @Test
    public void removeTitle() throws Exception {
    	String title = "my test title";
    	Post postV1 = new Post();
    	postV1.setTitle(title);
        JsonNode postV1Node = mapper.valueToTree(postV1);

        RemoveOperation removeOperation = new RemoveOperation(JacksonUtils.toJsonPointer("/title"));
        String removeValueJson = mapper.writeValueAsString(removeOperation);

        // read operation
        PatchOperation operation = mapper.readValue(removeValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getTitle(), equalTo(null));
    }

    @Test
    public void removeAuthor() throws Exception {
    	Author author = new Author("james", "bond", "james.bond@007.com");
    	Post postV1 = new Post();
    	postV1.setAuthor(author);
        JsonNode postV1Node = mapper.valueToTree(postV1);

        RemoveOperation removeOperation = new RemoveOperation(JacksonUtils.toJsonPointer("/author"));
        String removeValueJson = mapper.writeValueAsString(removeOperation);

        // read operation
        PatchOperation operation = mapper.readValue(removeValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getAuthor(), equalTo(null));
    }

    @Test
    public void removeAuthorFirstName() throws Exception {
    	Author author = new Author("james", "bond", "james.bond@007.com");
    	Post postV1 = new Post();
    	postV1.setAuthor(author);
        JsonNode postV1Node = mapper.valueToTree(postV1);

        RemoveOperation removeOperation = new RemoveOperation(JacksonUtils.toJsonPointer("/author/firstName"));
        String removeValueJson = mapper.writeValueAsString(removeOperation);

        // read operation
        PatchOperation operation = mapper.readValue(removeValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getAuthor().getFirstName(), equalTo(null));
    }

    @Test
    public void removeSections() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        RemoveOperation removeOperation = new RemoveOperation(JacksonUtils.toJsonPointer("/sections"));
        String removeValueJson = mapper.writeValueAsString(removeOperation);

        // read operation
        PatchOperation operation = mapper.readValue(removeValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections(), equalTo(null));
    }

    @Test
    public void removeFirstSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        RemoveOperation removeOperation = new RemoveOperation(JacksonUtils.toJsonPointer("/sections/0"));
        String removeValueJson = mapper.writeValueAsString(removeOperation);

        // read operation
        PatchOperation operation = mapper.readValue(removeValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(0));
    }

    @Test
    public void removeMiddleSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        RemoveOperation removeOperation = new RemoveOperation(JacksonUtils.toJsonPointer("/sections/1"));
        String removeValueJson = mapper.writeValueAsString(removeOperation);

        // read operation
        PatchOperation operation = mapper.readValue(removeValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(2));
        Assert.assertThat(postV2.getSections().get(0).getTitle(), equalTo("section-1"));
        Assert.assertThat(postV2.getSections().get(1).getTitle(), equalTo("section-3"));
    }

    @Test
    public void removeLastSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        RemoveOperation removeOperation = new RemoveOperation(JacksonUtils.toJsonPointer("/sections/3"));
        String removeValueJson = mapper.writeValueAsString(removeOperation);

        // read operation
        PatchOperation operation = mapper.readValue(removeValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(3));
        Assert.assertThat(postV2.getSections().get(2).getTitle(), equalTo("section-3"));
    }

}
