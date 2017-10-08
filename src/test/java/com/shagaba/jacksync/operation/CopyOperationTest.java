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

public class CopyOperationTest extends BaseTest {
	
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	
    }

    @Test
    public void copyFromTitleToAuthorFirstName() throws Exception {
    	String copyMe = "please copy me";
    	Author author = new Author("james", "bond", "james.bond@007.com");
    	Post postV1 = new Post();
    	postV1.setTitle(copyMe);
    	postV1.setAuthor(author);
        JsonNode postV1Node = mapper.valueToTree(postV1);

        CopyOperation copyOperation = new CopyOperation(JacksonUtils.toJsonPointer("/title"), JacksonUtils.toJsonPointer("/author/firstName"));
        String addValueJson = mapper.writeValueAsString(copyOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getTitle(), equalTo(copyMe));
        Assert.assertThat(postV2.getAuthor().getFirstName(), equalTo(copyMe));
        Assert.assertThat(postV2.getAuthor().getLastName(), equalTo(postV1.getAuthor().getLastName()));
        Assert.assertThat(postV2.getAuthor().getEmail(), equalTo(postV1.getAuthor().getEmail()));
    }

    @Test
    public void copyFromTagToCategory() throws Exception {
    	String copyMe = "please copy me";
    	Post postV1 = new Post();
    	postV1.setTags(Arrays.asList("tag1", "tag2", copyMe, "tag3"));
    	postV1.setCategories(new ArrayList<String>());
        JsonNode postV1Node = mapper.valueToTree(postV1);

        CopyOperation copyOperation = new CopyOperation(JacksonUtils.toJsonPointer("/tags/2"), JacksonUtils.toJsonPointer("/categories/0"));
        String addValueJson = mapper.writeValueAsString(copyOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getCategories().get(0), equalTo(copyMe));
        Assert.assertThat(postV2.getCategories().size(), equalTo(1));
        Assert.assertThat(postV2.getTags().size(), equalTo(4));
    }

    
    @Test
    public void copySection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        postV1.getSections().add(new Section("section-5", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        CopyOperation copyOperation = new CopyOperation(JacksonUtils.toJsonPointer("/sections/1"), JacksonUtils.toJsonPointer("/sections/3"));
        String addValueJson = mapper.writeValueAsString(copyOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(6));
        Assert.assertThat(postV2.getSections().get(3), equalTo(postV1.getSections().get(1)));
    }
    
    @Test
    public void copyAfterLastSection() throws Exception {
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        postV1.getSections().add(new Section("section-5", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        CopyOperation copyOperation = new CopyOperation(JacksonUtils.toJsonPointer("/sections/1"), JacksonUtils.toJsonPointer("/sections/-"));
        String addValueJson = mapper.writeValueAsString(copyOperation);

        // read operation
        PatchOperation operation = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(6));
        Assert.assertThat(postV2.getSections().get(5), equalTo(postV1.getSections().get(1)));
    }

}
