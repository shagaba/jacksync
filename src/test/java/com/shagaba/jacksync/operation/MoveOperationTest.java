package com.shagaba.jacksync.operation;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.BaseTest;
import com.shagaba.jacksync.support.dto.Author;
import com.shagaba.jacksync.support.dto.Post;
import com.shagaba.jacksync.support.dto.Section;
import com.shagaba.jacksync.utils.JacksonUtils;

public class MoveOperationTest extends BaseTest {
	
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	
    }

    @Test
    public void moveFromTitleToAuthorFirstName() throws Exception {
    	String moveMe = "please move me";
    	Author author = new Author("james", "bond", "james.bond@007.com");
    	Post postV1 = new Post();
    	postV1.setTitle(moveMe);
    	postV1.setAuthor(author);
        JsonNode postV1Node = mapper.valueToTree(postV1);

        MoveOperation moveOperation = new MoveOperation(JacksonUtils.toJsonPointer("/title"), JacksonUtils.toJsonPointer("/author/firstName"));
        String addValueJson = mapper.writeValueAsString(moveOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getTitle(), equalTo(null));
        Assert.assertThat(postV2.getAuthor().getFirstName(), equalTo(moveMe));
        Assert.assertThat(postV2.getAuthor().getLastName(), equalTo(postV1.getAuthor().getLastName()));
        Assert.assertThat(postV2.getAuthor().getEmail(), equalTo(postV1.getAuthor().getEmail()));
    }

    @Test
    public void moveFromTagToCategory() throws Exception {
    	String moveMe = "please move me";
    	Post postV1 = new Post();
    	postV1.setTags(Arrays.asList("tag1", "tag2", moveMe, "tag3"));
    	postV1.setCategories(new ArrayList<String>());
        JsonNode postV1Node = mapper.valueToTree(postV1);

        MoveOperation moveOperation = new MoveOperation(JacksonUtils.toJsonPointer("/tags/2"), JacksonUtils.toJsonPointer("/categories/0"));
        String addValueJson = mapper.writeValueAsString(moveOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getCategories().get(0), equalTo(moveMe));
        Assert.assertThat(postV2.getCategories().size(), equalTo(1));
        Assert.assertThat(postV2.getTags().size(), equalTo(3));
    }

    
    @Test
    public void moveSection() throws Exception {
    	String moveMe = "please move me";
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section(moveMe, null));
    	postV1.getSections().add(new Section("section-2", null));
        postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-5", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        MoveOperation moveOperation = new MoveOperation(JacksonUtils.toJsonPointer("/sections/1"), JacksonUtils.toJsonPointer("/sections/3"));
        String addValueJson = mapper.writeValueAsString(moveOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(5));
        Assert.assertThat(postV2.getSections().get(3).getTitle(), equalTo(moveMe));
    }
    
    @Test
    public void moveAfterLastSection() throws Exception {
    	String moveMe = "please move me";
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section(moveMe, null));
    	postV1.getSections().add(new Section("section-2", null));
        postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-5", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        MoveOperation moveOperation = new MoveOperation(JacksonUtils.toJsonPointer("/sections/1"), JacksonUtils.toJsonPointer("/sections/-"));
        String addValueJson = mapper.writeValueAsString(moveOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(5));
        Assert.assertThat(postV2.getSections().get(4).getTitle(), equalTo(moveMe));
    }

}
