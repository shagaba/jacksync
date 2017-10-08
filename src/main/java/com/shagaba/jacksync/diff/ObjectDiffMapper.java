package com.shagaba.jacksync.diff;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.diff.strategy.DiffStrategy;
import com.shagaba.jacksync.diff.strategy.SimpleDiffStrategy;
import com.shagaba.jacksync.operation.PatchOperation;

public class ObjectDiffMapper implements DiffMapper {
	
	protected ObjectMapper objectMapper;
	
	protected DiffStrategy diffStrategy;
	
	/**
	 * @param objectMapper
	 */
	public ObjectDiffMapper(ObjectMapper objectMapper) {
		this(objectMapper, new SimpleDiffStrategy());
	}

	/**
	 * 
	 * @param objectMapper
	 * @param diffStrategy
	 */
	public ObjectDiffMapper(ObjectMapper objectMapper, DiffStrategy diffStrategy) {
        if (objectMapper == null) {
            throw new IllegalArgumentException("ObjectMapper cannot be null");
        }
        if (diffStrategy == null) {
            throw new IllegalArgumentException("DiffStrategy cannot be null");
        }
		this.objectMapper = objectMapper;
		this.diffStrategy = diffStrategy;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	@Override
	public <T> List<PatchOperation> diff(T source, T target) {
        if (source == null) {
            throw new IllegalArgumentException("Source object cannot be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target object cannot be null");
        }
		JsonNode sourceJsonNode = objectMapper.valueToTree(source);
		JsonNode targetJsonNode = objectMapper.valueToTree(target);
		return diffStrategy.diff(sourceJsonNode, targetJsonNode);
	}
	
}
