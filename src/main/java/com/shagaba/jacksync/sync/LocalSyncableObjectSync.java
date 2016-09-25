package com.shagaba.jacksync.sync;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.SyncCapsule;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.exception.ChecksumMismatchException;
import com.shagaba.jacksync.exception.InvalidSyncVersionException;
import com.shagaba.jacksync.exception.SyncProcessingException;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class LocalSyncableObjectSync {
	
	private LocalObjectSync localObjectSync;
	
	/**
	 * @param objectMapper
	 */
	public LocalSyncableObjectSync(ObjectMapper objectMapper) {
		this.localObjectSync = new LocalObjectSync(objectMapper);
	}

	/**
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		return this.localObjectSync.getObjectMapper();
	}

	/**
	 * @param objectMapper the objectMapper to set
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.localObjectSync.setObjectMapper(objectMapper);
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
	public <T extends Syncable> T sync(T sourceObject, SyncCapsule syncCapsule, boolean isClientSync) {
		// validate sourceObject version = syncCapsule version
		if (!Objects.equals(sourceObject.getVersion(), syncCapsule.getVersion())) {
			throw new InvalidSyncVersionException("Sync Version Mismatch");
		}
		
		T targetObject = localObjectSync.sync(sourceObject, syncCapsule.getOperations());

		// verifyChecksum
		try {
			String targetJson = localObjectSync.getObjectMapper().writeValueAsString(targetObject);
			boolean isChecksumValid =  ChecksumUtils.verifyChecksum(targetJson, syncCapsule.getTargetChecksum());
			if (!isChecksumValid) {
				throw new ChecksumMismatchException("Checksum on target does not match checksum on sync capsule");
			}
		} catch (JsonProcessingException e) {
			throw new SyncProcessingException(e);
		}
		
		// validate targetObject version = target version
		Long targetVersion = isClientSync ? syncCapsule.getApprovedVersion() : sourceObject.getVersion();
		if (!Objects.equals(targetObject.getVersion(), targetVersion)) {
			throw new InvalidSyncVersionException("Target Version Mismatch");
		}
		return targetObject;
	}
	
}
