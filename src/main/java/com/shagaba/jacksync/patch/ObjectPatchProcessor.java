package com.shagaba.jacksync.patch;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
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
	 * @param sourceObject
	 * @param jsonOperations
	 * @return
	 * @throws PatchProcessingException 
	 */
	public <T> T patch(T sourceObject, String jsonOperations) throws PatchProcessingException {
		List<PatchOperation> operations;
		try {
			operations = this.objectMapper.readValue(jsonOperations, new TypeReference<List<PatchOperation>>() {});
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return patch(sourceObject, operations);
	}

	/**
	 * 
	 * @param sourceObject
	 * @param operations
	 * @return
	 * @throws PatchProcessingException 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T patch(T sourceObject, List<PatchOperation> operations) throws PatchProcessingException {
		T targetObject = null;
		try {
			JsonNode sourceJsonNode = objectMapper.valueToTree(sourceObject);
			JsonNode targetJsonNode = patch(sourceJsonNode, operations);
			
			targetObject = (T) objectMapper.treeToValue(targetJsonNode, sourceObject.getClass());
		} catch (Exception e) {
			throw new PatchProcessingException(e);
		}
		return targetObject;
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

}
