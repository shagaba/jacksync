package com.shagaba.jacksync.sync;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.exception.SyncProcessingException;

public class LocalObjectSync {
	
	private ObjectMapper objectMapper;
	
	/**
	 * @param objectMapper
	 */
	public LocalObjectSync(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * @param objectMapper the objectMapper to set
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * 
	 * @param currentJsonNode
	 * @param actions
	 * @return
	 */
	protected JsonNode sync(JsonNode currentJsonNode, List<PatchOperation> actions) {
		JsonNode syncdJsonNode = currentJsonNode.deepCopy();
		for (PatchOperation action : actions) {
			syncdJsonNode = action.apply(syncdJsonNode);
		}
		return syncdJsonNode;
	}

	/**
	 * 
	 * @param sourceObject
	 * @param syncCapsule
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T sync(T sourceObject, List<PatchOperation> operations) {
		T targetObject = null;
		try {
			JsonNode sourceJsonNode = objectMapper.valueToTree(sourceObject);
			JsonNode targetJsonNode = sync(sourceJsonNode, operations);
			
			targetObject = (T) objectMapper.treeToValue(targetJsonNode, sourceObject.getClass());
		} catch (JsonProcessingException e) {
			throw new SyncProcessingException(e);
		}
		return targetObject;
	}

}
