package com.shagaba.jacksync.diff.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksync.exception.DiffProcessingException;
import com.shagaba.jacksync.operation.MergeOperation;
import com.shagaba.jacksync.operation.PatchOperation;
import com.shagaba.jacksync.operation.RemoveOperation;
import com.shagaba.jacksync.utils.JacksonUtils;

public class MergeOperationDiffStrategy implements DiffStrategy {
	
	protected DiffStrategy diffStrategy;
	
	public MergeOperationDiffStrategy() {
		diffStrategy = new SimpleDiffStrategy();
	}
	
	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @return
	 * @throws DiffProcessingException 
	 */
	@Override
	public List<PatchOperation> diff(JsonNode sourceJsonNode, JsonNode targetJsonNode) throws DiffProcessingException {
		List<PatchOperation> operations = diffStrategy.diff(sourceJsonNode, targetJsonNode);
		return optimize(targetJsonNode, operations);
	}
	
	protected class JsonPointerData {
		private List<String> fieldNames;
		private List<PatchOperation> operations;
		
		/**
		 * 
		 */
		public JsonPointerData() {
			fieldNames = new ArrayList<>();
			operations = new ArrayList<>();
		}

		/**
		 * @return the fieldNames
		 */
		public List<String> getFieldNames() {
			return fieldNames;
		}

		/**
		 * @return the operations
		 */
		public List<PatchOperation> getOperations() {
			return operations;
		}
	}
	
	/**
	 * 
	 * @param targetJsonNode
	 * @param operations
	 * @return
	 */
	protected List<PatchOperation> optimize(JsonNode targetJsonNode, List<PatchOperation> operations) {
		List<PatchOperation> optimizedOperations = new ArrayList<>();
		Map<JsonPointer, JsonPointerData> parentToJsonPointerDataMap = new HashMap<>();
		for (PatchOperation operation: operations) {
			JsonNode pathJsonNode = JacksonUtils.locateHeadContainer(targetJsonNode, operation.getPath());
			if (pathJsonNode.isObject()) {
				if (operation.getClass() == RemoveOperation.class) {
					optimizedOperations.add(operation);
				} else {
					JsonPointer parentPointer = operation.getPath().head();
					if (!parentToJsonPointerDataMap.containsKey(parentPointer)) {
						parentToJsonPointerDataMap.put(parentPointer, new JsonPointerData());
					}
					parentToJsonPointerDataMap.get(parentPointer).getOperations().add(operation);
					parentToJsonPointerDataMap.get(parentPointer).getFieldNames().add(JacksonUtils.lastFieldName(operation.getPath()));
				}
			} else if (pathJsonNode.isArray()) {
				optimizedOperations.add(operation);
			}
		}
		// merge process must start handling arrays  
		optimizedOperations.addAll(optimize(targetJsonNode, parentToJsonPointerDataMap));
		return optimizedOperations;
	}

	/**
	 * 
	 * @param targetJsonNode
	 * @param parentToJsonPointerDataMap
	 * @return
	 */
	protected List<PatchOperation> optimize(JsonNode targetJsonNode, Map<JsonPointer, JsonPointerData> parentToJsonPointerDataMap) {
		List<PatchOperation> optimizedOperations = new ArrayList<>();
		Map<JsonPointer, MergeOperation> parentToMergeOperation = new HashMap<>();
		
		for (JsonPointer parentPath : parentToJsonPointerDataMap.keySet()) {
			JsonPointerData jsonPointerData = parentToJsonPointerDataMap.get(parentPath);
			JsonNode parentJsonNode = JacksonUtils.locateContainer(targetJsonNode, parentPath);
			ObjectNode parentObjectNode = (ObjectNode) parentJsonNode.deepCopy();
			parentObjectNode.retain(jsonPointerData.getFieldNames());
			MergeOperation mergeOperation = new MergeOperation(parentPath, parentObjectNode);
			mergeOperation = parentObjectMergeOperation(targetJsonNode, mergeOperation);
			
			MergeOperation parentMergeOperation = parentToMergeOperation.get(mergeOperation.getPath());
			if (parentMergeOperation == null) {
				parentToMergeOperation.put(mergeOperation.getPath(), mergeOperation);
			} else {
				JsonNode mergedJsonNode = new MergeOperation(parentMergeOperation.getValue()).apply(mergeOperation.getValue());
				parentMergeOperation.setValue(mergedJsonNode);
			}
		}
		if (!parentToMergeOperation.isEmpty()) {
			optimizedOperations.addAll(parentToMergeOperation.values());
		}
		return optimizedOperations;
	}

	/**
	 * 
	 * @param targetJsonNode
	 * @param mergeOperation
	 * @return
	 */
	protected MergeOperation parentObjectMergeOperation(JsonNode targetJsonNode, MergeOperation mergeOperation) {
		if (JacksonUtils.isRoot(mergeOperation.getPath())) {
			return mergeOperation;
		}
		JsonNode parentJsonNode = JacksonUtils.locateHeadContainer(targetJsonNode, mergeOperation.getPath());
		if (parentJsonNode.isObject()) {
			ObjectNode parentObjectNode = (ObjectNode) parentJsonNode.deepCopy();
			parentObjectNode.removeAll();
			parentObjectNode.set(JacksonUtils.lastFieldName(mergeOperation.getPath()), mergeOperation.getValue());
			MergeOperation parentMergeOperation = new MergeOperation(mergeOperation.getPath().head(), parentObjectNode);
			return parentObjectMergeOperation(targetJsonNode, parentMergeOperation);
		} else {
			return mergeOperation;
		}
	}


}
