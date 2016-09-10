package com.shagaba.jacksonpatch;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shagaba.jacksonpatch.post.dto.Author;
import com.shagaba.jacksonpatch.post.dto.Post;
import com.shagaba.jacksonpatch.post.dto.Section;

public class MergeOperationTest {
	
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
    public void addTitle() throws Exception {
    	Post postV1 = new Post();
        JsonNode postV1Node = mapper.valueToTree(postV1);

    	String title = "my test title";
    	Post postV1_1 = new Post();
    	postV1_1.setTitle(title);

        MergeOperation mergeOperation = new MergeOperation(mapper.valueToTree(postV1_1));
        String mergeValueJson = mapper.writeValueAsString(mergeOperation);

        // read action
        PatchOperation action = mapper.readValue(mergeValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getTitle(), equalTo(title));
    }

    @Test
    public void addAuthor() throws Exception {
    	Post postV1 = new Post();
        JsonNode postV1Node = mapper.valueToTree(postV1);

    	Author author = new Author("james", "bond", "james.bond@007.com");
    	Post postV1_1 = new Post();
    	postV1_1.setAuthor(author);
    	
        MergeOperation mergeOperation = new MergeOperation(mapper.valueToTree(postV1_1));
        String mergeValueJson = mapper.writeValueAsString(mergeOperation);

        // read action
        PatchOperation action = mapper.readValue(mergeValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);
        
        Assert.assertThat(postV2.getAuthor(), equalTo(author));
    }

    @Test
    public void replaceAuthorFirstName() throws Exception {
    	Post postV1 = new Post();
    	postV1.setAuthor(new Author("1", "2", "3"));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        MergeOperation mergeOperation = new MergeOperation(mapper.readTree("{\"author\":{\"firstName\":\"james\"}}"));
        String mergeValueJson = mapper.writeValueAsString(mergeOperation);

        // read action
        PatchOperation action = mapper.readValue(mergeValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getAuthor().getFirstName(), equalTo("james"));
    }

    @Test
    public void replaceAuthorFirstNameToNull() throws Exception {
    	Post postV1 = new Post();
    	postV1.setAuthor(new Author("1", "2", "3"));
        JsonNode postV1Node = mapper.valueToTree(postV1);

        MergeOperation mergeOperation = new MergeOperation(mapper.readTree("{\"author\":{\"firstName\":null}}"));
        String mergeValueJson = mapper.writeValueAsString(mergeOperation);

        // read action
        PatchOperation action = mapper.readValue(mergeValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getAuthor().getFirstName(), equalTo(null));
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
       	Post postV1_1 = new Post();
       	postV1_1.setSections(new ArrayList<Section>());
       	postV1_1.getSections().add(new Section("section-1", null));
       	postV1_1.getSections().add(new Section("section-2", null));
       	postV1_1.getSections().add(section2_5);
       	postV1_1.getSections().add(new Section("section-3", null));
       	postV1_1.getSections().add(new Section("section-4", null));

        MergeOperation mergeOperation = new MergeOperation(mapper.valueToTree(postV1_1));
        String mergeValueJson = mapper.writeValueAsString(mergeOperation);

        // read action
        PatchOperation action = mapper.readValue(mergeValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
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
       	Post postV1_1 = new Post();
       	postV1_1.setSections(new ArrayList<Section>());
       	postV1_1.getSections().add(new Section("section-1", null));
       	postV1_1.getSections().add(new Section("section-2", null));
       	postV1_1.getSections().add(new Section("section-3", null));
       	postV1_1.getSections().add(new Section("section-4", null));
       	postV1_1.getSections().add(section5);

        MergeOperation mergeOperation = new MergeOperation(mapper.valueToTree(postV1_1));
        String mergeValueJson = mapper.writeValueAsString(mergeOperation);

        // read action
        PatchOperation action = mapper.readValue(mergeValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(5));
        Assert.assertThat(postV2.getSections().get(4), equalTo(section5));
    }
}
