package com.shagaba.jacksync.diff.strategy;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.exception.DiffProcessingException;
import com.shagaba.jacksync.operation.PatchOperation;

public interface DiffStrategy {

	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @return
	 * @throws DiffProcessingException
	 */
	public List<PatchOperation> diff(JsonNode sourceJsonNode, JsonNode targetJsonNode) throws DiffProcessingException;

}
