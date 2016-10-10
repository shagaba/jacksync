package com.shagaba.jacksync.service;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.JacksyncData;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.exception.ChecksumMismatchException;
import com.shagaba.jacksync.exception.InvalidSyncVersionException;
import com.shagaba.jacksync.exception.SyncProcessingException;
import com.shagaba.jacksync.utils.ChecksumUtils;

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
	 * @param jacksyncData
	 * @return
	 */
	public <T extends Syncable> T clientSync(T sourceObject, JacksyncData jacksyncData) {
		return sync(sourceObject, jacksyncData, true);
	}
	
	/**
	 * 
	 * @param sourceObject
	 * @param jacksyncData
	 * @return
	 */
	public <T extends Syncable> T serverSync(T sourceObject, JacksyncData jacksyncData) {
		return sync(sourceObject, jacksyncData, false);
	}

	/**
	 * 
	 * @param sourceObject
	 * @param jacksyncData
	 * @param isClientSync
	 * @return
	 */
	protected <T extends Syncable> T sync(T sourceObject, JacksyncData jacksyncData, boolean isClientSync) {
		// validate sourceObject version = jacksyncData version
		if (!Objects.equals(sourceObject.getVersion(), jacksyncData.getVersion())) {
			throw new InvalidSyncVersionException("Sync Version Mismatch");
		}
		
		T targetObject = sync(sourceObject, jacksyncData);
		
		// validate targetObject version = target version
		Long targetVersion = isClientSync ? jacksyncData.getMasterVersion() : sourceObject.getVersion();
		if (!Objects.equals(targetObject.getVersion(), targetVersion)) {
			throw new InvalidSyncVersionException("Target Version Mismatch");
		}
		return targetObject;
	}
	
	/**
	 * 
	 * @param sourceObject
	 * @param jacksyncData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T sync(T sourceObject, JacksyncData jacksyncData) {
		T targetObject = null;
		try {
			JsonNode sourceJsonNode = objectMapper.valueToTree(sourceObject);
			JsonNode targetJsonNode = sync(sourceJsonNode, jacksyncData.getOperations());
			
			// verifyChecksum
			targetObject = (T) objectMapper.treeToValue(targetJsonNode, sourceObject.getClass());
			String targetJson = objectMapper.writeValueAsString(targetObject);
			boolean isChecksumValid =  ChecksumUtils.verifyChecksum(targetJson, jacksyncData.getTargetChecksum());
			if (!isChecksumValid) {
				throw new ChecksumMismatchException("Checksum on target does not match checksum on sync jacksyncData");
			}
			
		} catch (JsonProcessingException e) {
			throw new SyncProcessingException(e);
		}
		return targetObject;
	}

	/**
	 * 
	 * @param currentJsonNode
	 * @param operations
	 * @return
	 */
	protected JsonNode sync(JsonNode currentJsonNode, List<PatchOperation> operations) {
		JsonNode syncdJsonNode = currentJsonNode.deepCopy();
		for (PatchOperation operation : operations) {
			syncdJsonNode = operation.apply(syncdJsonNode);
		}
		return syncdJsonNode;
	}
	

}
