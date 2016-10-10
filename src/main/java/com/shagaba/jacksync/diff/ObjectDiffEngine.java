package com.shagaba.jacksync.diff;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.diff.processor.DiffProcessor;
import com.shagaba.jacksync.diff.processor.SimpleDiffProcessor;

public class ObjectDiffEngine implements DiffEngine {
	
	protected ObjectMapper objectMapper;
	
	protected DiffProcessor diffProcessor;
	
	/**
	 * @param objectMapper
	 */
	public ObjectDiffEngine(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.diffProcessor = new SimpleDiffProcessor();
	}

	/**
	 * 
	 * @param objectMapper
	 * @param diffProcessor
	 */
	public ObjectDiffEngine(ObjectMapper objectMapper, DiffProcessor diffProcessor) {
		this.objectMapper = objectMapper;
		this.diffProcessor = diffProcessor;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	@Override
	public <T> List<PatchOperation> diff(T source, T target) {
		JsonNode sourceJsonNode = objectMapper.valueToTree(source);
		JsonNode targetJsonNode = objectMapper.valueToTree(target);
		return diffProcessor.diff(sourceJsonNode, targetJsonNode);
	}
	
}
