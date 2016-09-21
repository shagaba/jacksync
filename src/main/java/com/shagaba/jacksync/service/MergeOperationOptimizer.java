package com.shagaba.jacksync.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksync.MergeOperation;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.utils.JacksonUtils;

public class MergeOperationOptimizer {
	
	/**
	 * 
	 * @param targetJsonNode
	 * @param operations
	 * @return
	 */
	public List<PatchOperation> optimize(JsonNode targetJsonNode, List<PatchOperation> operations) {
		Map<JsonPointer, List<String>> parentToFieldNamesMap = new HashMap<>();
		Map<JsonPointer, List<PatchOperation>> parentToOperationsMap = new HashMap<>();
		
		for (PatchOperation operation: operations) {
			JsonPointer path = JsonPointer.compile(operation.getPath());
			JsonNode pathJsonNode = JacksonUtils.locateHeadContainer(targetJsonNode, path);
			if (pathJsonNode.isObject()) {
				JsonPointer parentPointer = path.head();
				if (!parentToFieldNamesMap.containsKey(parentPointer)) {
					parentToFieldNamesMap.put(parentPointer, new ArrayList<>());
					parentToOperationsMap.put(parentPointer, new ArrayList<>());
				}
				parentToFieldNamesMap.get(parentPointer).add(JacksonUtils.lastFieldName(path));
				parentToOperationsMap.get(parentPointer).add(operation);
			}
		}
		System.out.println(parentToFieldNamesMap);
		return optimize(targetJsonNode, parentToFieldNamesMap, parentToOperationsMap);
	}
	
	/**
	 * 
	 * @param targetJsonNode
	 * @param parentToFieldNamesMap
	 * @return
	 */
	public List<PatchOperation> optimize(JsonNode targetJsonNode, Map<JsonPointer, List<String>> parentToFieldNamesMap, Map<JsonPointer, List<PatchOperation>> parentToOperationsMap) {
		List<PatchOperation> optimizedOperations = new ArrayList<>();
		for (JsonPointer parentPath : parentToFieldNamesMap.keySet()) {
			Collection<String> fieldNames = parentToFieldNamesMap.get(parentPath);
			if (fieldNames.size() == 1) {
				optimizedOperations.add(parentToOperationsMap.get(parentPath).get(0));
			} else {
				JsonNode parentJsonNode = JacksonUtils.locateContainer(targetJsonNode, parentPath);
				ObjectNode parentObjectNode = (ObjectNode) parentJsonNode.deepCopy();
				parentObjectNode.retain(fieldNames);
				MergeOperation mergeOperation = new MergeOperation(parentPath, parentObjectNode);
				optimizedOperations.add(mergeOperation);
			}
		}
		return optimizedOperations;
		
	}
}
