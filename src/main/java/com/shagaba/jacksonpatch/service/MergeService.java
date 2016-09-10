package com.shagaba.jacksonpatch.service;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksonpatch.PatchOperation;
import com.shagaba.jacksonpatch.SyncCapsule;
import com.shagaba.jacksonpatch.Syncable;
import com.shagaba.jacksonpatch.exception.InvalidSyncVersionException;
import com.shagaba.jacksonpatch.exception.SyncProcessingException;

public class MergeService {
	
	private ObjectMapper objectMapper;
	
	/**
	 * 
	 */
	public MergeService() {
		super();
	}

	/**
	 * @param objectMapper
	 */
	public MergeService(ObjectMapper objectMapper) {
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
	 * @param sourceObject
	 * @param syncCapsule
	 * @return
	 */
	public <T extends Syncable> T merge(T sourceObject, SyncCapsule syncCapsule) {
		return merge(sourceObject, syncCapsule, false);
	}
	
	/**
	 * 
	 * @param sourceObject
	 * @param syncCapsule
	 * @return
	 */
	public <T extends Syncable> T serverMerge(T sourceObject, SyncCapsule syncCapsule) {
		return merge(sourceObject, syncCapsule, true);
	}

	/**
	 * 
	 * @param sourceObject
	 * @param syncCapsule
	 * @param isServerMerge
	 * @return
	 */
	public <T extends Syncable> T merge(T sourceObject, SyncCapsule syncCapsule, boolean isServerMerge) {
		// validate sourceObject version = syncCapsule version
		if (!Objects.equals(sourceObject.getVersion(), syncCapsule.getVersion())) {
			throw new InvalidSyncVersionException("Sync Version Mismatch");
		}
		
		T targetObject = merge(sourceObject, syncCapsule.getOperations());
		
		Long targetVersion = isServerMerge ? sourceObject.getVersion() : syncCapsule.getApprovedVersion();
		// validate targetObject version = target version
		if (!Objects.equals(targetObject.getVersion(), targetVersion)) {
			throw new InvalidSyncVersionException("Sync Version Mismatch");
		}
		return targetObject;
	}
	
	/**
	 * 
	 * @param sourceObject
	 * @param actions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T merge(T sourceObject, List<PatchOperation> actions) {
		T targetObject = null;
		try {
			JsonNode sourceJsonNode = objectMapper.valueToTree(sourceObject);
			JsonNode mergedJsonNode = merge(sourceJsonNode, actions);
			targetObject = (T) objectMapper.treeToValue(mergedJsonNode, sourceObject.getClass());
		} catch (JsonProcessingException e) {
			throw new SyncProcessingException(e);
		}
		return targetObject;
	}

	/**
	 * 
	 * @param currentJsonNode
	 * @param actions
	 * @return
	 */
	public JsonNode merge(JsonNode currentJsonNode, List<PatchOperation> actions) {
		JsonNode mergedJsonNode = currentJsonNode.deepCopy();
		for (PatchOperation action : actions) {
			mergedJsonNode = action.apply(mergedJsonNode);
		}
		return mergedJsonNode;
	}
	

}
