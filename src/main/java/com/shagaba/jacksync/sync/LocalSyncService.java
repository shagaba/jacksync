package com.shagaba.jacksync.sync;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.exception.ChecksumMismatchException;
import com.shagaba.jacksync.exception.InvalidSyncVersionException;
import com.shagaba.jacksync.exception.SyncProcessingException;
import com.shagaba.jacksync.patch.ObjectPatchProcessor;
import com.shagaba.jacksync.patch.PatchProcessor;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class LocalSyncService {
	
	private ObjectMapper objectMapper;
	
	protected boolean isChecksumValidationEnabled;
	
	protected PatchProcessor patchProcessor;

	/**
	 * 
	 */
	public LocalSyncService() {
		this.objectMapper = new ObjectMapper();
		this.patchProcessor = new ObjectPatchProcessor(objectMapper);
	}

	/**
	 * @param objectMapper
	 */
	public LocalSyncService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.patchProcessor = new ObjectPatchProcessor(objectMapper);
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
	 * @return the isChecksumValidationEnabled
	 */
	public boolean isChecksumValidationEnabled() {
		return isChecksumValidationEnabled;
	}

	/**
	 * @param isChecksumValidationEnabled the isChecksumValidationEnabled to set
	 */
	public void setChecksumValidationEnabled(boolean isChecksumValidationEnabled) {
		this.isChecksumValidationEnabled = isChecksumValidationEnabled;
	}

	/**
	 * 
	 * @param sourceObject
	 * @param syncData
	 * @return
	 */
	public <T> SyncObject<T> clientSync(SyncObject<T> sourceObject, SyncData syncData) {
		return sync(sourceObject, syncData, syncData.getMasterVersion());
	}
	
	/**
	 * 
	 * @param sourceObject
	 * @param syncData
	 * @return
	 */
	public <T> SyncObject<T> masterSync(SyncObject<T> sourceObject, SyncData syncData) {
		return sync(sourceObject, syncData, sourceObject.getVersion());
	}

	/**
	 * 
	 * @param sourceObject
	 * @param syncData
	 * @param targetVersion
	 * @return
	 */
	protected <T> SyncObject<T> sync(SyncObject<T> sourceObject, SyncData syncData, Long targetVersion) {
		// validate sourceObject version = jacksyncData version
		if (!Objects.equals(sourceObject.getVersion(), syncData.getVersion())) {
			throw new InvalidSyncVersionException("Sync Version Mismatch");
		}
		
		T targetObject = patchProcessor.patch(sourceObject.getObject(), syncData.getOperations());

		// verifyChecksum
		if (isChecksumValidationEnabled) {
			try {
				String targetJson = objectMapper.writeValueAsString(targetObject);
				boolean isChecksumValid =  ChecksumUtils.verifyChecksum(targetJson, syncData.getTargetChecksum());
				if (!isChecksumValid) {
					throw new ChecksumMismatchException("Checksum on target does not match checksum on JacksyncData");
				}
			} catch (JsonProcessingException e) {
				throw new SyncProcessingException(e);
			}
		}
		
		return new SyncObject<T>(targetVersion, targetObject);
	}	

}
