package com.shagaba.jacksync.operation;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shagaba.jacksync.exception.InvalidPatchValueTestException;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.operation.TestOperation;
import com.shagaba.jacksync.post.dto.Post;
import com.shagaba.jacksync.post.dto.Section;
import com.shagaba.jacksync.utils.JacksonUtils;

public class TestOperationTest {
	
	private ObjectMapper mapper = null;

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

    @Test(expected=InvalidPatchValueTestException.class)
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

    @Test(expected=InvalidPatchValueTestException.class)
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
    
    @Test(expected=InvalidPatchValueTestException.class)
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
