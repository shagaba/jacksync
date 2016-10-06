package com.shagaba.jacksync.merge;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.JacksyncData;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.exception.ChecksumMismatchException;
import com.shagaba.jacksync.exception.InvalidSyncVersionException;
import com.shagaba.jacksync.exception.SyncProcessingException;
import com.shagaba.jacksync.utils.ChecksumUtils;

public abstract class AbstractJacksyncDataMerger implements JacksyncDataMerger {
	
	protected boolean isChecksumValidationEnabled;
	
	protected ObjectMerger objectMerger;
	
	protected ObjectMapper objectMapper;
	
	/**
	 * @param objectMapper
	 */
	public AbstractJacksyncDataMerger(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.objectMerger = new ObjectMerger(objectMapper);
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
	 * @param jacksyncData
	 * @param targetVersion
	 * @return
	 */
	protected <T extends Syncable> T merge(T sourceObject, JacksyncData jacksyncData, Long targetVersion) {
		// validate sourceObject version = syncCapsule version
		if (!Objects.equals(sourceObject.getVersion(), jacksyncData.getVersion())) {
			throw new InvalidSyncVersionException("Sync Version Mismatch");
		}
		
		T targetObject = objectMerger.apply(sourceObject, jacksyncData.getOperations());

		// verifyChecksum
		if (isChecksumValidationEnabled) {
			try {
				String targetJson = objectMapper.writeValueAsString(targetObject);
				boolean isChecksumValid =  ChecksumUtils.verifyChecksum(targetJson, jacksyncData.getTargetChecksum());
				if (!isChecksumValid) {
					throw new ChecksumMismatchException("Checksum on target does not match checksum on sync capsule");
				}
			} catch (JsonProcessingException e) {
				throw new SyncProcessingException(e);
			}
		}
		
		// validate targetObject version = target version
		if (!Objects.equals(targetObject.getVersion(), targetVersion)) {
			throw new InvalidSyncVersionException("Target Version Mismatch");
		}
		return targetObject;
	}
	
}
