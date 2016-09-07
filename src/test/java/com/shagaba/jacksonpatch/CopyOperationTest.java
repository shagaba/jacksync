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
import com.google.common.collect.Lists;
import com.shagaba.jacksonpatch.post.dto.Author;
import com.shagaba.jacksonpatch.post.dto.Post;
import com.shagaba.jacksonpatch.post.dto.Section;

public class CopyOperationTest {
	
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
    public void copyFromTitleToAuthorFirstName() throws Exception {
    	String copyMe = "please copy me";
    	Author author = new Author("james", "bond", "james.bond@007.com");
    	Post postV1 = new Post();
    	postV1.setTitle(copyMe);
    	postV1.setAuthor(author);
        JsonNode postV1Node = mapper.valueToTree(postV1);

        CopyOperation copyOperation = new CopyOperation("/title", "/author/firstName");
        String addValueJson = mapper.writeValueAsString(copyOperation);

        // read action
        PatchOperation action = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
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
    	postV1.setTags(Lists.newArrayList("tag1", "tag2", copyMe, "tag3"));
    	postV1.setCategories(Lists.newArrayList());
        JsonNode postV1Node = mapper.valueToTree(postV1);

        CopyOperation copyOperation = new CopyOperation("/tags/2", "/categories/0");
        String addValueJson = mapper.writeValueAsString(copyOperation);

        // read action
        PatchOperation action = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
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

        CopyOperation copyOperation = new CopyOperation("/sections/1", "/sections/3");
        String addValueJson = mapper.writeValueAsString(copyOperation);

        // read action
        PatchOperation action = mapper.readValue(addValueJson, PatchOperation.class);
        JsonNode postV2Node = action.apply(postV1Node);
        Post postV2 = mapper.treeToValue(postV2Node, Post.class);

        Assert.assertThat(postV2.getSections().size(), equalTo(6));
        Assert.assertThat(postV2.getSections().get(3), equalTo(postV1.getSections().get(1)));
    }

}
