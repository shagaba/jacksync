package com.shagaba.jacksonpatch.service;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksonpatch.PatchOperation;
import com.shagaba.jacksonpatch.SyncCapsule;
import com.shagaba.jacksonpatch.Syncable;
import com.shagaba.jacksonpatch.exception.ChecksumMismatchException;
import com.shagaba.jacksonpatch.exception.InvalidSyncVersionException;
import com.shagaba.jacksonpatch.exception.SyncProcessingException;
import com.shagaba.jacksonpatch.utils.ChecksumUtil;

public class LocalSyncService {
	
	private ObjectMapper objectMapper;
	
	/**
	 * 
	 */
	public LocalSyncService() {
		super();
	}

	/**
	 * @param objectMapper
	 */
	public LocalSyncService(ObjectMapper objectMapper) {
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
	public <T extends Syncable> T clientSync(T sourceObject, SyncCapsule syncCapsule) {
		return sync(sourceObject, syncCapsule, true);
	}
	
	/**
	 * 
	 * @param sourceObject
	 * @param syncCapsule
	 * @return
	 */
	public <T extends Syncable> T serverSync(T sourceObject, SyncCapsule syncCapsule) {
		return sync(sourceObject, syncCapsule, false);
	}

	/**
	 * 
	 * @param sourceObject
	 * @param syncCapsule
	 * @param isClientSync
	 * @return
	 */
	protected <T extends Syncable> T sync(T sourceObject, SyncCapsule syncCapsule, boolean isClientSync) {
		// validate sourceObject version = syncCapsule version
		if (!Objects.equals(sourceObject.getVersion(), syncCapsule.getVersion())) {
			throw new InvalidSyncVersionException("Sync Version Mismatch");
		}
		
		T targetObject = sync(sourceObject, syncCapsule);
		
		// validate targetObject version = target version
		Long targetVersion = isClientSync ? syncCapsule.getApprovedVersion() : sourceObject.getVersion();
		if (!Objects.equals(targetObject.getVersion(), targetVersion)) {
			throw new InvalidSyncVersionException("Target Version Mismatch");
		}
		return targetObject;
	}
	
	/**
	 * 
	 * @param sourceObject
	 * @param syncCapsule
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T sync(T sourceObject, SyncCapsule syncCapsule) {
		T targetObject = null;
		try {
			JsonNode sourceJsonNode = objectMapper.valueToTree(sourceObject);
			JsonNode targetJsonNode = sync(sourceJsonNode, syncCapsule.getOperations());
			
			// verifyChecksum
			String targetJson = objectMapper.writeValueAsString(targetJsonNode);
			boolean isChecksumValid =  ChecksumUtil.verifyChecksum(targetJson, syncCapsule.getTargetChecksum());
			if (!isChecksumValid) {
				throw new ChecksumMismatchException("Checksum on target does not match checksum on sync capsule");
			}
			
			targetObject = (T) objectMapper.treeToValue(targetJsonNode, sourceObject.getClass());
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
	protected JsonNode sync(JsonNode currentJsonNode, List<PatchOperation> actions) {
		JsonNode syncdJsonNode = currentJsonNode.deepCopy();
		for (PatchOperation action : actions) {
			syncdJsonNode = action.apply(syncdJsonNode);
		}
		return syncdJsonNode;
	}
	

}
