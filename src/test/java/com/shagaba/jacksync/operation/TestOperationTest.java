package com.shagaba.jacksync.operation;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.BaseTest;
import com.shagaba.jacksync.exception.InvalidTestValueException;
import com.shagaba.jacksync.support.dto.Post;
import com.shagaba.jacksync.support.dto.Section;
import com.shagaba.jacksync.utils.JacksonUtils;

public class TestOperationTest extends BaseTest {
	
    @Before
    public void beforeEach() {
    	mapper = newObjectMapper();
    	
    }

    @Test
    public void testTitle() throws Exception {
    	String title = "please test me";
    	Post postV1 = new Post();
    	postV1.setTitle(title);
        JsonNode postV1Node = mapper.valueToTree(postV1);

        TestOperation testOperation = new TestOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(title));
        String testValueJson = mapper.writeValueAsString(testOperation);

        // read operation
        PatchOperation operation = mapper.readValue(testValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2, equalTo(postV1));
    }

    @Test(expected=InvalidTestValueException.class)
    public void testInvalidTitle() throws Exception {
    	String title = "please test me";
    	Post postV1 = new Post();
    	postV1.setTitle(title + ", im different");
        JsonNode postV1Node = mapper.valueToTree(postV1);

        TestOperation testOperation = new TestOperation(JacksonUtils.toJsonPointer("/title"), mapper.valueToTree(title));
        String testValueJson = mapper.writeValueAsString(testOperation);

        // read operation
        PatchOperation operation = mapper.readValue(testValueJson, PatchOperation.class);
        operation.apply(postV1Node);
    }

    @Test
    public void testTags() throws Exception {
    	String testMe = "please test me";
    	Post postV1 = new Post();
    	postV1.setTags(Arrays.asList("tag1", "tag2", testMe, "tag3"));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        TestOperation testOperation = new TestOperation(JacksonUtils.toJsonPointer("/tags/2"), mapper.valueToTree(testMe));
        String testValueJson = mapper.writeValueAsString(testOperation);

        // read operation
        PatchOperation operation = mapper.readValue(testValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2, equalTo(postV1));
    }

    @Test(expected=InvalidTestValueException.class)
    public void testInvalidTag() throws Exception {
    	String testMe = "please test me";
    	Post postV1 = new Post();
    	postV1.setTags(Arrays.asList("tag1", "tag2", testMe + ", im different", "tag3"));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        TestOperation testOperation = new TestOperation(JacksonUtils.toJsonPointer("/tags/2"), mapper.valueToTree(testMe));
        String testValueJson = mapper.writeValueAsString(testOperation);

        // read operation
        PatchOperation operation = mapper.readValue(testValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        mapper.treeToValue(postV2Node, Post.class);
    }
    
    @Test
    public void testSection() throws Exception {
    	Section section2 = new Section("section-2", null);
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        postV1.getSections().add(new Section("section-5", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        TestOperation testOperation = new TestOperation(JacksonUtils.toJsonPointer("/sections/1"), mapper.valueToTree(section2));
        String testValueJson = mapper.writeValueAsString(testOperation);

        // read operation
        PatchOperation operation = mapper.readValue(testValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2, equalTo(postV1));
    }
    
    @Test(expected=InvalidTestValueException.class)
    public void testInvalidSection() throws Exception {
    	Section section2 = new Section("section-2Invalid", null);
    	Post postV1 = new Post();
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
        postV1.getSections().add(new Section("section-4", null));
        postV1.getSections().add(new Section("section-5", null));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        TestOperation testOperation = new TestOperation(JacksonUtils.toJsonPointer("/sections/1"), mapper.valueToTree(section2));
        String testValueJson = mapper.writeValueAsString(testOperation);

        // read operation
        PatchOperation operation = mapper.readValue(testValueJson, PatchOperation.class);
        JsonNode postV2Node = operation.apply(postV1Node);
        mapper.treeToValue(postV2Node, Post.class);
    }

}
