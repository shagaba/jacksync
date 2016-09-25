package com.shagaba.jacksync.diff;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.diff.processor.DiffProcessor;
import com.shagaba.jacksync.diff.processor.SimpleDiffProcessor;

public class ObjectDiffEngine implements DiffEngine<List<PatchOperation>, Object>{
	
	private ObjectMapper objectMapper;
	
	private DiffProcessor diffProcessor;
	
	/**
	 * @param objectMapper
	 */
	public ObjectDiffEngine(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.diffProcessor = new SimpleDiffProcessor();
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
	 */
	@Override
	public <T extends Object> List<PatchOperation> diff(T source, T target) {
		JsonNode sourceJsonNode = objectMapper.valueToTree(source);
		JsonNode targetJsonNode = objectMapper.valueToTree(target);
		return diffProcessor.diff(sourceJsonNode, targetJsonNode);
	}
	
}
