package com.shagaba.jacksync.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.SyncCapsule;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.utils.ChecksumUtils;

public class ObjectDiffEngine {
	
	private ObjectMapper objectMapper;
	
	private DiffProcessor diffProcessor;
	
	/**
	 * 
	 */
	public ObjectDiffEngine() {
		super();
		this.diffProcessor = new SimpleDiffProcessor();
	}

	/**
	 * @param objectMapper
	 */
	public ObjectDiffEngine(ObjectMapper objectMapper) {
		this();
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
	 * @return the diffProcessor
	 */
	public DiffProcessor getDiffProcessor() {
		return diffProcessor;
	}

	/**
	 * @param diffProcessor the diffProcessor to set
	 */
	public void setDiffProcessor(DiffProcessor diffProcessor) {
		this.diffProcessor = diffProcessor;
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws JsonProcessingException 
	 */
	public <T extends Syncable> SyncCapsule diff(T source, T target) throws JsonProcessingException {
		List<PatchOperation> operations = diffOperations(source, target);
		SyncCapsule syncCapsule = new SyncCapsule();
		syncCapsule.setVersion(source.getVersion());
		syncCapsule.setApprovedVersion(target.getVersion());
		syncCapsule.setTargetChecksum(ChecksumUtils.computeChecksum(objectMapper.writeValueAsString(target)));
		syncCapsule.setOperations(operations);
		return syncCapsule;
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public <T extends Syncable> List<PatchOperation> diffOperations(T source, T target) {
		JsonNode sourceJsonNode = objectMapper.valueToTree(source);
		JsonNode targetJsonNode = objectMapper.valueToTree(target);
		return diffProcessor.diff(sourceJsonNode, targetJsonNode);
	}
	
}
