package com.shagaba.jacksonpatch.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shagaba.jacksonpatch.AddOperation;
import com.shagaba.jacksonpatch.SyncCapsule;
import com.shagaba.jacksonpatch.post.dto.Post;

public class MergeServiceTest {
	
	private ObjectMapper mapper = null;

	private MergeService mergeService = null;
	
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
    	mergeService = new MergeService(mapper);
    }

    @Test
    public void mergeVanila() throws Exception {
    	SyncCapsule syncCapsule = new SyncCapsule();
    	Post postV1 = new Post();

    	String title = "my test title";
        AddOperation addOperation = new AddOperation("/title", mapper.valueToTree(title));
        syncCapsule.setOperations(Arrays.asList(addOperation));
        
        Post postV2 = mergeService.serverMerge(postV1, syncCapsule);
        
        System.out.println(postV2);
        
        
        Map<String, Integer> map = new HashMap<>();
        map.put("yugoug", 3);
    }
    
}
