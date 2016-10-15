package com.shagaba.jacksync.diff;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.JacksyncData;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.diff.processor.DiffProcessor;
import com.shagaba.jacksync.diff.processor.SimpleDiffProcessor;
import com.shagaba.jacksync.exception.JacksyncDiffException;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class JacksyncDiffEngine implements SyncableDiffEngine {
	
	protected ObjectMapper objectMapper;

	protected ObjectDiffEngine objectDiffEngine;
	
	protected boolean isComputeChecksum;

	/**
	 * @param objectMapper
	 */
	public JacksyncDiffEngine(ObjectMapper objectMapper) {
		this(objectMapper, new SimpleDiffProcessor(), false);
	}

	/**
	 * 
	 * @param objectMapper
	 * @param diffProcessor
	 */
	public JacksyncDiffEngine(ObjectMapper objectMapper, DiffProcessor diffProcessor) {
		this(objectMapper, diffProcessor, false);
	}

	/**
	 * 
	 * @param objectMapper
	 * @param diffProcessor
	 * @param isComputeChecksum
	 */
	public JacksyncDiffEngine(ObjectMapper objectMapper, DiffProcessor diffProcessor, boolean isComputeChecksum) {
		this.objectMapper = objectMapper;
		this.objectDiffEngine = new ObjectDiffEngine(objectMapper, diffProcessor);
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
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws JsonProcessingException 
	 */
	@Override
	public <T extends Syncable> JacksyncData diff(T source, T target) throws JacksyncDiffException {
        if (source == null) {
            throw new IllegalArgumentException("Source object cannot be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target object cannot be null");
        }
		try {
			List<PatchOperation> operations = objectDiffEngine.diff(source, target);
			JacksyncData jacksyncData = new JacksyncData();
			jacksyncData.setVersion(source.getVersion());
			jacksyncData.setMasterVersion(target.getVersion());
			
			if (isComputeChecksum) {
				String targetJson = objectMapper.writeValueAsString(target);
				jacksyncData.setTargetChecksum(ChecksumUtils.computeChecksum(targetJson));
			}
			jacksyncData.setOperations(operations);
			return jacksyncData;
		} catch (Exception e) {
			throw new JacksyncDiffException(e);
		}
	}

}
