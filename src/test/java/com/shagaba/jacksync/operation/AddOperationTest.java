package com.shagaba.jacksync.operation;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.BaseTest;
import com.shagaba.jacksync.support.dto.Author;
import com.shagaba.jacksync.support.dto.Post;
import com.shagaba.jacksync.support.dto.Section;
import com.shagaba.jacksync.utils.JacksonUtils;

public class AddOperationTest extends BaseTest {
	
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	
    }

    @Test
    public void addTitle() throws Exception {
    	Post postV1 = new Post();
        JsonNode postV1Node = mapper.valueToTree(postV1);

    	String title = "my test title";
        AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(title));
        String addValueJson = mapper.writeValueAsString(addOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getTitle(), equalTo(title));
    }

    @Test
    public void addAuthor() throws Exception {
    	Post postV1 = new Post();
        JsonNode postV1Node = mapper.valueToTree(postV1);

    	Author author = new Author("james", "bond", "james.bond@007.com");
        AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/author"), mapper.valueToTree(author));
        String addValueJson = mapper.writeValueAsString(addOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getAuthor(), equalTo(author));
    }

    @Test
    public void addAuthorFirstName() throws Exception {
    	Post postV1 = new Post();
    	postV1.setAuthor(new Author());
        JsonNode postV1Node = mapper.valueToTree(postV1);

        String firstName = "james";
        AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/author/firstName"), mapper.valueToTree(firstName));
        String addValueJson = mapper.writeValueAsString(addOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getAuthor().getFirstName(), equalTo(firstName));
    }

    @Test
    public void addFirstSection() throws Exception {
    	Post postV1 = new Post();
        JsonNode postV1Node = mapper.valueToTree(postV1);

        Section section = new Section("section-1", null);
        List<Section> sections = new ArrayList<Section>();
        sections.add(section);
        AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/sections"), mapper.valueToTree(sections));
        String addValueJson = mapper.writeValueAsString(addOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(1));
        Assert.assertThat(postV2.getSections().get(0), equalTo(section));
    }

    @Test
    public void addMiddleSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        Section section2_5 = new Section("section-2.5", null);
        AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/sections/2"), mapper.valueToTree(section2_5));
        String addValueJson = mapper.writeValueAsString(addOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(5));
        Assert.assertThat(postV2.getSections().get(2), equalTo(section2_5));
    }

    @Test
    public void addLastSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        Section section5 = new Section("section-5", null);
        AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/sections/4"), mapper.valueToTree(section5));
        String addValueJson = mapper.writeValueAsString(addOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(5));
        Assert.assertThat(postV2.getSections().get(4), equalTo(section5));
    }

    @Test
    public void addAfterLastSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        Section section5 = new Section("section-5", null);
        AddOperation addOperation = new AddOperation(JacksonUtils.toJsonPointer("/sections/-"), mapper.valueToTree(section5));
        String addValueJson = mapper.writeValueAsString(addOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(5));
        Assert.assertThat(postV2.getSections().get(4), equalTo(section5));
    }

}
