package com.shagaba.jacksync.service;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shagaba.jacksync.SyncCapsule;
import com.shagaba.jacksync.diff.processor.MergeOperationDiffProcessor;
import com.shagaba.jacksync.post.dto.Author;
import com.shagaba.jacksync.post.dto.Post;
import com.shagaba.jacksync.post.dto.Section;
import com.shagaba.jacksync.service.Jacksync.JacksyncBuilder;

public class JacksyncTest {
	
	private ObjectMapper mapper;
	
	private Jacksync jacksync;
	
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
    	
    	jacksync = Jacksync
    			.newBuilder(mapper)
    			.localSyncService()
    			.diffMapper()
    			.simpleDiffProcessor()
    			.build();
    	
    }

    @Test
    public void addTitle() throws Exception {
    	String title = "my test title";
    	Post postV1 = new Post();
    	postV1.setVersion(0L);
//    	postV1.setTitle(title);
    	postV1.setAuthor(new Author("1", "2", "3"));
    	postV1.setSections(new ArrayList<Section>());
    	postV1.getSections().add(new Section("section-1", null));
    	postV1.getSections().add(new Section("section-2", null));
    	postV1.getSections().add(new Section("section-3", null));
    	postV1.getSections().add(new Section("section-4", null));

    	Post postV1_1 = new Post();
    	postV1_1.setVersion(1L);
    	postV1_1.setTitle(title);
    	postV1_1.setAuthor(new Author("james", "bond", "3"));
    	postV1_1.setSections(new ArrayList<Section>());
    	postV1_1.getSections().add(new Section("section-1", null));
    	postV1_1.getSections().add(new Section("section-2", null));
    	postV1_1.getSections().add(new Section("section-X", null));
    	postV1_1.getSections().add(new Section("section-4", null));

//    	// SyncCapsule regular diff
//        SyncCapsule syncCapsule = jacksync.getDiffMapper().diff(postV1, postV1_1);
//        Post postV1_2 = jacksync.getLocalSyncService().clientSync(postV1, syncCapsule);
//        
//        // SyncCapsule with merge operations 
//        jacksync.getDiffMapper().setDiffProcessor(new MergeOperationDiffProcessor());
//        SyncCapsule syncCapsule2 = jacksync.getDiffMapper().diff(postV1, postV1_1);
//        Post postV1_21 = jacksync.getLocalSyncService().clientSync(postV1, syncCapsule2);
//        
//        
//        Assert.assertThat(postV1_2, equalTo(postV1_1));
//        Assert.assertThat(postV1_21, equalTo(postV1_1));
    }

}
