package com.shagaba.jacksync.patch;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.exception.PatchProcessingException;
import com.shagaba.jacksync.operation.PatchOperation;

public class ObjectPatchProcessor implements PatchProcessor {
	private ObjectMapper objectMapper;
	
	/**
	 * @param objectMapper
	 */
	public ObjectPatchProcessor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * 
	 * @param currentJsonNode
	 * @param operations
	 * @return
	 */
	protected JsonNode patch(JsonNode currentJsonNode, List<PatchOperation> operations) {
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
	public <T> T patch(T sourceObject, List<PatchOperation> operations) {
		T targetObject = null;
		try {
			JsonNode sourceJsonNode = objectMapper.valueToTree(sourceObject);
			JsonNode targetJsonNode = patch(sourceJsonNode, operations);
			
			targetObject = (T) objectMapper.treeToValue(targetJsonNode, sourceObject.getClass());
		} catch (JsonProcessingException e) {
			throw new PatchProcessingException(e);
		}
		return targetObject;
	}

}
