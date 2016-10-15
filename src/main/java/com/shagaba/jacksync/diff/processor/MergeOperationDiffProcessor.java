package com.shagaba.jacksync.diff.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksync.MergeOperation;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.RemoveOperation;
import com.shagaba.jacksync.utils.JacksonUtils;

public class MergeOperationDiffProcessor implements DiffProcessor {
	
	protected DiffProcessor diffProcessor;
	
	public MergeOperationDiffProcessor() {
		diffProcessor = new SimpleDiffProcessor();
	}
	
	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @return
	 */
	@Override
	public List<PatchOperation> diff(JsonNode sourceJsonNode, JsonNode targetJsonNode) {
		List<PatchOperation> operations = diffProcessor.diff(sourceJsonNode, targetJsonNode);
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
		Map<JsonPointer, JsonPointerData> parentToJsonPointerDataMap = new HashMap<>();
		List<PatchOperation> arrayOperations = new ArrayList<>();
		for (PatchOperation operation: operations) {
			JsonNode pathJsonNode = JacksonUtils.locateHeadContainer(targetJsonNode, operation.getPath());
			if (pathJsonNode.isObject()) {
				if (operation.getClass() == RemoveOperation.class) {
					// temp
					arrayOperations.add(operation);
				} else {
					JsonPointer parentPointer = operation.getPath().head();
					if (!parentToJsonPointerDataMap.containsKey(parentPointer)) {
						parentToJsonPointerDataMap.put(parentPointer, new JsonPointerData());
					}
					parentToJsonPointerDataMap.get(parentPointer).getOperations().add(operation);
					parentToJsonPointerDataMap.get(parentPointer).getFieldNames().add(JacksonUtils.lastFieldName(operation.getPath()));
				}
			} else if (pathJsonNode.isArray()) {
				arrayOperations.add(operation);
			}
		}
		// merge process must start handling arrays  
		List<PatchOperation> mergeOperations = arrayOperations;
		mergeOperations.addAll(optimize(targetJsonNode, parentToJsonPointerDataMap));
		return mergeOperations;
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
				JsonNode mergedJsonNode = JacksonUtils.merge(parentMergeOperation.getValue(), mergeOperation.getValue());
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
