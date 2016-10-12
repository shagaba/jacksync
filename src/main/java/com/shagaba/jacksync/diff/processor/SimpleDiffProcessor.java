package com.shagaba.jacksync.diff.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.AddOperation;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.RemoveOperation;
import com.shagaba.jacksync.ReplaceOperation;
import com.shagaba.jacksync.utils.JacksonUtils;

public class SimpleDiffProcessor implements DiffProcessor {
	
	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @return
	 */
	@Override
	public List<PatchOperation> diff(JsonNode sourceJsonNode, JsonNode targetJsonNode) {
		List<PatchOperation> operations = new ArrayList<>();
		return diff(sourceJsonNode, targetJsonNode, operations, JsonPointer.compile("/"));
	}

	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @param patchOperations
	 * @param path
	 * @return
	 */
	protected List<PatchOperation> diff(JsonNode sourceJsonNode, JsonNode targetJsonNode, List<PatchOperation> patchOperations, JsonPointer path) {
		if (!Objects.equals(sourceJsonNode, targetJsonNode)) {
			
			if (sourceJsonNode.isArray() && targetJsonNode.isArray()) {
				diffArrays(sourceJsonNode, targetJsonNode, patchOperations, path);
			} else if (sourceJsonNode.isObject() && targetJsonNode.isObject()) {
				diffObjects(sourceJsonNode, targetJsonNode, patchOperations, path);
			} else {
				patchOperations.add(new ReplaceOperation(path, targetJsonNode.deepCopy()));
			}
		}
		return patchOperations;
	}
	
	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @param patchOperations
	 * @param path
	 * @return
	 */
	protected List<PatchOperation> diffArrays(JsonNode sourceJsonNode, JsonNode targetJsonNode, List<PatchOperation> patchOperations, JsonPointer path) {
		if (sourceJsonNode.isArray() && targetJsonNode.isArray()) {
			List<JsonNode> commonNodes = new ArrayList<>();
			for (Iterator<JsonNode> iterator = sourceJsonNode.iterator(); iterator.hasNext();) {
				commonNodes.add(iterator.next());
			}
			
			List<JsonNode> targetNodes = new ArrayList<>();
			for (Iterator<JsonNode> iterator = targetJsonNode.iterator(); iterator.hasNext();) {
				targetNodes.add(iterator.next());
			}
			
			commonNodes.retainAll(targetNodes);
			
			int commonIndex = 0;
			int sourceIndex = 0;
			int targetIndex = 0;
			int maxIndex = Math.max(sourceJsonNode.size(), targetJsonNode.size());
			
			for (int index = 0; index < maxIndex; ++index) {
				JsonNode commonNode = commonNodes.size() > commonIndex ? commonNodes.get(commonIndex) : null;
				JsonNode sourceNode = sourceJsonNode.size() > sourceIndex ? sourceJsonNode.get(sourceIndex) : null;
				JsonNode targetNode = targetJsonNode.size() > targetIndex ? targetJsonNode.get(targetIndex) : null;
				
				if (Objects.equals(commonNode, sourceNode) && Objects.equals(commonNode, targetNode)) {
					++commonIndex;
					++sourceIndex;
					++targetIndex;
				} else {
					if (Objects.equals(commonNode, sourceNode)) {
						// add missing target
						JsonPointer targetPath = JacksonUtils.append(path, Integer.toString(targetIndex++));
						patchOperations.add(new AddOperation(targetPath, targetNode.deepCopy()));
					} else if (Objects.equals(commonNode, targetNode)) {
						// remove target
						JsonPointer targetPath = JacksonUtils.append(path, Integer.toString(sourceIndex++));
						patchOperations.add(new RemoveOperation(targetPath));
					} else {
						JsonPointer targetPath = JacksonUtils.append(path, Integer.toString(targetIndex++));
						diff(sourceNode, targetNode, patchOperations, targetPath);
						++sourceIndex;
					}
				}
			}
		}
		return patchOperations;
	}
	
	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @param patchOperations
	 * @param path
	 * @return
	 */
	protected List<PatchOperation> diffObjects(JsonNode sourceJsonNode, JsonNode targetJsonNode, List<PatchOperation> patchOperations, JsonPointer path) {
		if (sourceJsonNode.isObject() &&  targetJsonNode.isObject()) {
			// source iteration
			for (Iterator<String> sourceFieldNames = sourceJsonNode.fieldNames(); sourceFieldNames.hasNext();) {
				String fieldName = sourceFieldNames.next();
				JsonPointer fieldNamePath = JacksonUtils.append(path, fieldName);
				if (targetJsonNode.has(fieldName)) {
					diff(sourceJsonNode.path(fieldName), targetJsonNode.path(fieldName), patchOperations, fieldNamePath);
				} else {
					patchOperations.add(new RemoveOperation(fieldNamePath));
				}
			}
			// target iteration
			for (Iterator<String> targetFieldNames = targetJsonNode.fieldNames(); targetFieldNames.hasNext();) {
				String fieldName = targetFieldNames.next();
				if (!sourceJsonNode.has(fieldName)) {
					JsonPointer fieldNamePath = JacksonUtils.append(path, fieldName);
					patchOperations.add(new AddOperation(fieldNamePath, targetJsonNode.path(fieldName).deepCopy()));
				}
			}
		}		
		return patchOperations;
	}

}
