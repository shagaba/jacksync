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

public class ReplaceOperationTest extends BaseTest {
	
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	
    }

    @Test
    public void replaceTitle() throws Exception {
    	Post postV1 = new Post();
    	postV1.setTitle("1stTitle");
        JsonNode postV1Node = mapper.valueToTree(postV1);

    	String title = "my test title";
        ReplaceOperation replaceOperation = new ReplaceOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(title));
        String replaceValueJson = mapper.writeValueAsString(replaceOperation);

        // read operation
        PatchOperation operation = mapper.readValue(replaceValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getTitle(), equalTo(title));
    }

    @Test
    public void replaceAuthor() throws Exception {
    	Author authorV1 = new Author("harry", "potter", "harry.potter@wizard.com");
    	Post postV1 = new Post();
    	postV1.setAuthor(authorV1);
        JsonNode postV1Node = mapper.valueToTree(postV1);

    	Author author = new Author("james", "bond", "james.bond@007.com");
        ReplaceOperation replaceOperation = new ReplaceOperation(JacksonUtils.toJsonPointer("/author"), mapper.valueToTree(author));
        String replaceValueJson = mapper.writeValueAsString(replaceOperation);

        // read operation
        PatchOperation operation = mapper.readValue(replaceValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getAuthor(), equalTo(author));
    }

    @Test
    public void replaceAuthorFirstName() throws Exception {
    	Author authorV1 = new Author("harry", "potter", "harry.potter@wizard.com");
    	Post postV1 = new Post();
       	postV1.setAuthor(authorV1);
        JsonNode postV1Node = mapper.valueToTree(postV1);

        String firstName = "james";
        ReplaceOperation replaceOperation = new ReplaceOperation(JacksonUtils.toJsonPointer("/author/firstName"), mapper.valueToTree(firstName));
        String replaceValueJson = mapper.writeValueAsString(replaceOperation);

        // read operation
        PatchOperation operation = mapper.readValue(replaceValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getAuthor().getFirstName(), equalTo(firstName));
        Assert.assertThat(postV2.getAuthor().getLastName(), equalTo(authorV1.getLastName()));
        Assert.assertThat(postV2.getAuthor().getEmail(), equalTo(authorV1.getEmail()));
    }

    @Test
    public void replaceMiddleSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3333", null));
        postV1.getSections().add(new Section("section-4", null));
        postV1.getSections().add(new Section("section-5", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        Section section3 = new Section("section-3", null);
        ReplaceOperation replaceOperation = new ReplaceOperation(JacksonUtils.toJsonPointer("/sections/2"), mapper.valueToTree(section3));
        String replaceValueJson = mapper.writeValueAsString(replaceOperation);

        // read operation
        PatchOperation operation = mapper.readValue(replaceValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(5));
        Assert.assertThat(postV2.getSections().get(2), equalTo(section3));
    }

    @Test
    public void replaceLastSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        postV1.getSections().add(new Section("section-5555", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        Section section5 = new Section("section-5", null);
        ReplaceOperation replaceOperation = new ReplaceOperation(JacksonUtils.toJsonPointer("/sections/4"), mapper.valueToTree(section5));
        String replaceValueJson = mapper.writeValueAsString(replaceOperation);

        // read operation
        PatchOperation operation = mapper.readValue(replaceValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(5));
        Assert.assertThat(postV2.getSections().get(4), equalTo(section5));
    }

}
