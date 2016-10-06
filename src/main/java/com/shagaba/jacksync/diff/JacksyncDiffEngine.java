package com.shagaba.jacksync.diff;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.JacksyncData;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.diff.processor.DiffProcessor;
import com.shagaba.jacksync.exception.JacksyncDiffException;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class JacksyncDiffEngine implements SyncableDiffEngine {
	
	protected ObjectDiffEngine objectDiffEngine;
	
	protected ObjectMapper objectMapper;
	
	/**
	 * @param objectMapper
	 */
	public JacksyncDiffEngine(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.objectDiffEngine = new ObjectDiffEngine(objectMapper);
	}

	/**
	 * 
	 * @param objectMapper
	 * @param diffProcessor
	 */
	public JacksyncDiffEngine(ObjectMapper objectMapper, DiffProcessor diffProcessor) {
		this.objectMapper = objectMapper;
		this.objectDiffEngine = new ObjectDiffEngine(objectMapper, diffProcessor);
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
		try {
			List<PatchOperation> operations = objectDiffEngine.diff(source, target);
			JacksyncData jacksyncData = new JacksyncData();
			jacksyncData.setVersion(source.getVersion());
			jacksyncData.setMasterVersion(target.getVersion());
			
			String targetJson = objectMapper.writeValueAsString(target);
			jacksyncData.setTargetChecksum(ChecksumUtils.computeChecksum(targetJson));
			jacksyncData.setOperations(operations);
			return jacksyncData;
		} catch (Exception e) {
			throw new JacksyncDiffException(e);
		}
	}

}
