package com.shagaba.jacksync.diff;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.diff.strategy.DiffStrategy;
import com.shagaba.jacksync.diff.strategy.SimpleDiffStrategy;
import com.shagaba.jacksync.exception.DiffProcessingException;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.sync.SyncData;
import com.shagaba.jacksync.sync.SyncObject;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class SyncObjectDiffMapper implements SyncDiffMapper {
	
	protected ObjectMapper objectMapper;

	protected ObjectDiffMapper objectDiffMapper;
	
	protected boolean isComputeChecksum;

	/**
	 * @param objectMapper
	 */
	public SyncObjectDiffMapper(ObjectMapper objectMapper) {
		this(objectMapper, new SimpleDiffStrategy(), false);
	}

	/**
	 * 
	 * @param objectMapper
	 * @param diffStrategy
	 */
	public SyncObjectDiffMapper(ObjectMapper objectMapper, DiffStrategy diffStrategy) {
		this(objectMapper, diffStrategy, false);
	}

	/**
	 * 
	 * @param objectMapper
	 * @param diffStrategy
	 * @param isComputeChecksum
	 */
	public SyncObjectDiffMapper(ObjectMapper objectMapper, DiffStrategy diffStrategy, boolean isComputeChecksum) {
		this.objectMapper = objectMapper;
		this.objectDiffMapper = new ObjectDiffMapper(objectMapper, diffStrategy);
		this.isComputeChecksum = isComputeChecksum;
	}

	/**
	 * @return the isComputeChecksum
	 */
	public boolean isComputeChecksum() {
		return isComputeChecksum;
	}

	/**
	 * @param isComputeChecksum the isComputeChecksum to set
	 */
	public void setComputeChecksum(boolean isComputeChecksum) {
		this.isComputeChecksum = isComputeChecksum;
	}

	/**
	 * @return the objectDiffMapper
	 */
	public ObjectDiffMapper getObjectDiffMapper() {
		return objectDiffMapper;
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws DiffProcessingException 
	 */
	@Override
	public <T> SyncData diff(SyncObject<T> source, SyncObject<T> target) throws DiffProcessingException {
        if (source == null || source.getObject() == null) {
            throw new IllegalArgumentException("Source object cannot be null");
        }
        if (target == null || target.getObject() == null) {
            throw new IllegalArgumentException("Target object cannot be null");
        }
		try {
			List<PatchOperation> operations = objectDiffMapper.diff(source.getObject(), target.getObject());
			SyncData syncData = new SyncData();
			syncData.setVersion(source.getVersion());
			syncData.setMasterVersion(target.getVersion());
			
			if (isComputeChecksum) {
				String targetJson = objectMapper.writeValueAsString(target);
				syncData.setTargetChecksum(ChecksumUtils.computeChecksum(targetJson));
			}
			syncData.setOperations(operations);
			return syncData;
		} catch (Exception e) {
			throw new DiffProcessingException(e);
		}
	}

}
