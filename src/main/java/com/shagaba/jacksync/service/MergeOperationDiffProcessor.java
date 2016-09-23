package com.shagaba.jacksync.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksync.MergeOperation;
import com.shagaba.jacksync.PatchOperation;
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
		return mergeOperationOptimize(targetJsonNode, operations);
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
		 * @param fieldNames
		 * @param operations
		 */
		public JsonPointerData(List<String> fieldNames, List<PatchOperation> operations) {
			this.fieldNames = fieldNames;
			this.operations = operations;
		}
		/**
		 * @return the fieldNames
		 */
		public List<String> getFieldNames() {
			return fieldNames;
		}
		/**
		 * @param fieldNames the fieldNames to set
		 */
		public void setFieldNames(List<String> fieldNames) {
			this.fieldNames = fieldNames;
		}
		/**
		 * @return the operations
		 */
		public List<PatchOperation> getOperations() {
			return operations;
		}
		/**
		 * @param operations the operations to set
		 */
		public void setOperations(List<PatchOperation> operations) {
			this.operations = operations;
		}
	}

	/**
	 * 
	 * @param targetJsonNode
	 * @param operations
	 * @return
	 */
	protected List<PatchOperation> mergeOperationOptimize(JsonNode targetJsonNode, List<PatchOperation> operations) {
		Map<JsonPointer, JsonPointerData> parentToJsonPointerDataMap = new HashMap<>();
		
		for (PatchOperation operation: operations) {
			JsonPointer path = JsonPointer.compile(operation.getPath());
			JsonNode pathJsonNode = JacksonUtils.locateHeadContainer(targetJsonNode, path);
			if (pathJsonNode.isObject()) {
				JsonPointer parentPointer = path.head();
				if (!parentToJsonPointerDataMap.containsKey(parentPointer)) {
					parentToJsonPointerDataMap.put(parentPointer, new JsonPointerData());
				}
				parentToJsonPointerDataMap.get(parentPointer).getOperations().add(operation);
				parentToJsonPointerDataMap.get(parentPointer).getFieldNames().add(JacksonUtils.lastFieldName(path));
			}
		}
		return optimize(targetJsonNode, parentToJsonPointerDataMap);
	}

	/**
	 * 
	 * @param targetJsonNode
	 * @param parentToJsonPointerDataMap
	 * @return
	 */
	protected List<PatchOperation> optimize(JsonNode targetJsonNode, Map<JsonPointer, JsonPointerData> parentToJsonPointerDataMap) {
		List<PatchOperation> optimizedOperations = new ArrayList<>();
		for (JsonPointer parentPath : parentToJsonPointerDataMap.keySet()) {
			JsonPointerData jsonPointerData = parentToJsonPointerDataMap.get(parentPath);
			if (jsonPointerData.getOperations().size() == 1) {
				optimizedOperations.add(jsonPointerData.getOperations().get(0));
			} else {
				JsonNode parentJsonNode = JacksonUtils.locateContainer(targetJsonNode, parentPath);
				ObjectNode parentObjectNode = (ObjectNode) parentJsonNode.deepCopy();
				parentObjectNode.retain(jsonPointerData.getFieldNames());
				MergeOperation mergeOperation = new MergeOperation(parentPath, parentObjectNode);
				optimizedOperations.add(mergeOperation);
			}
		}
		return optimizedOperations;
		
	}

}
