package com.shagaba.jacksync.diff;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.SyncCapsule;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.diff.processor.DiffProcessor;
import com.shagaba.jacksync.exception.JacksyncDiffException;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class SyncableObjectDiffEngine implements DiffEngine<SyncCapsule, Syncable>{
	
	private ObjectDiffEngine objectDiffEngine;
	
	/**
	 * @param objectMapper
	 */
	public SyncableObjectDiffEngine(ObjectMapper objectMapper) {
		this.objectDiffEngine = new ObjectDiffEngine(objectMapper);
	}
	
	/**
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		return this.objectDiffEngine.getObjectMapper();
	}

	/**
	 * @param objectMapper the objectMapper to set
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectDiffEngine.setObjectMapper(objectMapper);
	}
	
	

	/**
	 * @return the diffProcessor
	 */
	public DiffProcessor getDiffProcessor() {
		return objectDiffEngine.getDiffProcessor();
	}

	/**
	 * @param diffProcessor the diffProcessor to set
	 */
	public void setDiffProcessor(DiffProcessor diffProcessor) {
		this.objectDiffEngine.setDiffProcessor(diffProcessor);
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws JsonProcessingException 
	 */
	@Override
	public <T extends Syncable> SyncCapsule diff(T source, T target) throws JacksyncDiffException {
		try {
			List<PatchOperation> operations = objectDiffEngine.diff(source, target);
			SyncCapsule syncCapsule = new SyncCapsule();
			syncCapsule.setVersion(source.getVersion());
			syncCapsule.setApprovedVersion(target.getVersion());
			
			String targetJson = objectDiffEngine.getObjectMapper().writeValueAsString(target);
			syncCapsule.setTargetChecksum(ChecksumUtils.computeChecksum(targetJson));
			syncCapsule.setOperations(operations);
			return syncCapsule;
		} catch (Exception e) {
			throw new JacksyncDiffException(e);
		}
	}

}
