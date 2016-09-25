package com.shagaba.jacksync.diff.processor;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.PatchOperation;

public interface DiffProcessor {
	
	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @return
	 */
	public List<PatchOperation> diff(JsonNode sourceJsonNode, JsonNode targetJsonNode);

}
