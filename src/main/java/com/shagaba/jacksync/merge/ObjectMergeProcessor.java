package com.shagaba.jacksync.merge;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.exception.MergeProcessingException;
import com.shagaba.jacksync.operation.MergeOperation;
import com.shagaba.jacksync.utils.JacksonUtils;

public class ObjectMergeProcessor implements MergeProcessor {
	
	private ObjectMapper objectMapper;
	
	/**
	 * @param objectMapper
	 */
	public ObjectMergeProcessor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public <T> T merge(T sourceObject, String jsonValue) throws MergeProcessingException {
		return merge(sourceObject, "", jsonValue);
	}

	@Override
	public <T> T merge(T sourceObject, String path, String jsonValue) throws MergeProcessingException {
		try {
			return merge(sourceObject, JacksonUtils.toJsonPointer(path), objectMapper.readTree(jsonValue));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public <T> T merge(T sourceObject, JsonNode value) throws MergeProcessingException {
		return merge(sourceObject, new MergeOperation(value));
	}

	@Override
	public <T> T merge(T sourceObject, JsonPointer path, JsonNode value) throws MergeProcessingException {
		return merge(sourceObject, new MergeOperation(path, value));
	}
	/**
	 * 
	 * @param sourceObject
	 * @param operation
	 * @return
	 * @throws MergeProcessingException 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T merge(T sourceObject, MergeOperation operation) throws MergeProcessingException {
		T targetObject = null;
		try {
			JsonNode sourceJsonNode = objectMapper.valueToTree(sourceObject);
			JsonNode targetJsonNode = operation.apply(sourceJsonNode.deepCopy());
			
			targetObject = (T) objectMapper.treeToValue(targetJsonNode, sourceObject.getClass());
		} catch (Exception e) {
			throw new MergeProcessingException(e);
		}
		return targetObject;
	}

}
