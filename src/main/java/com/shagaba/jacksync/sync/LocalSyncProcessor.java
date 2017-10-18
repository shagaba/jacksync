package com.shagaba.jacksync.sync;

import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.exception.ChecksumMismatchException;
import com.shagaba.jacksync.exception.InvalidSyncVersionException;
import com.shagaba.jacksync.exception.SyncException;
import com.shagaba.jacksync.exception.SyncProcessingException;
import com.shagaba.jacksync.patch.ObjectPatchProcessor;
import com.shagaba.jacksync.patch.PatchProcessor;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class LocalSyncProcessor implements SyncProcessor {
	
	private ObjectMapper objectMapper;
	
	protected boolean isChecksumValidationEnabled;
	
	protected PatchProcessor patchProcessor;

	/**
	 * @param objectMapper
	 */
	public LocalSyncProcessor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.patchProcessor = new ObjectPatchProcessor(objectMapper);
	}

	/**
	 * @return the isChecksumValidationEnabled
	 */
	@Override
	public boolean isChecksumValidationEnabled() {
		return isChecksumValidationEnabled;
	}

	/**
	 * @param isChecksumValidationEnabled the isChecksumValidationEnabled to set
	 */
	@Override
	public void setChecksumValidationEnabled(boolean isChecksumValidationEnabled) {
		this.isChecksumValidationEnabled = isChecksumValidationEnabled;
	}

	/**
	 * 
	 * @param sourceObject
	 * @param syncData
	 * @return
	 * @throws SyncException 
	 */
	@Override
	public <T> SyncObject<T> clientSync(SyncObject<T> sourceObject, SyncData syncData) throws SyncException {
		return sync(sourceObject, syncData, syncData.getMasterVersion());
	}
	
	/**
	 * 
	 * @param sourceObject
	 * @param syncData
	 * @return
	 * @throws SyncException 
	 */
	@Override
	public <T> SyncObject<T> masterSync(SyncObject<T> sourceObject, SyncData syncData) throws SyncException {
		return sync(sourceObject, syncData, sourceObject.getVersion());
	}

	/**
	 * 
	 * @param sourceObject
	 * @param syncData
	 * @param targetVersion
	 * @return
	 * @throws SyncException 
	 */
	protected <T> SyncObject<T> sync(SyncObject<T> sourceObject, SyncData syncData, Long targetVersion) throws SyncException {
		// validate sourceObject version = syncData version
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
					throw new ChecksumMismatchException("Checksum on target does not match checksum on syncData");
				}
			} catch (Exception e) {
				throw new SyncProcessingException(e);
			}
		}
		
		return new SyncObject<T>(targetVersion, targetObject);
	}	

}
