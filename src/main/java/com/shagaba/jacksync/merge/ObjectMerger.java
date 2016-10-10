package com.shagaba.jacksync.merge;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.exception.SyncProcessingException;

public class ObjectMerger implements PatchOperationsMerger {
	
	private ObjectMapper objectMapper;
	
	/**
	 * @param objectMapper
	 */
	public ObjectMerger(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * 
	 * @param currentJsonNode
	 * @param operations
	 * @return
	 */
	protected JsonNode merge(JsonNode currentJsonNode, List<PatchOperation> operations) {
		JsonNode syncdJsonNode = currentJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		return syncdJsonNode;
	}

	/**
	 * 
	 * @param sourceObject
	 * @param operations
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T apply(T sourceObject, List<PatchOperation> operations) {
		T targetObject = null;
		try {
			JsonNode sourceJsonNode = objectMapper.valueToTree(sourceObject);
			JsonNode targetJsonNode = merge(sourceJsonNode, operations);
			
			targetObject = (T) objectMapper.treeToValue(targetJsonNode, sourceObject.getClass());
		} catch (JsonProcessingException e) {
			throw new SyncProcessingException(e);
		}
		return targetObject;
	}

}
