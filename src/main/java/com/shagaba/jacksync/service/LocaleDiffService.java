package com.shagaba.jacksync.service;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.shagaba.jacksync.AddOperation;
import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.RemoveOperation;
import com.shagaba.jacksync.ReplaceOperation;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.utils.JacksonUtils;

public class LocaleDiffService<T extends Syncable> {
	
	private ObjectMapper objectMapper;
	
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
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public List<PatchOperation> diff(T source, T target) {
		List<PatchOperation> operations = Lists.newArrayList();
		
		JsonNode sourceJsonNode = objectMapper.valueToTree(source);
		JsonNode targetJsonNode = objectMapper.valueToTree(target);
		operations = diff(sourceJsonNode, targetJsonNode, operations, JsonPointer.compile("/"));
		return operations;
	}
	
	/**
	 * 
	 * @param sourceJsonNode
	 * @param targetJsonNode
	 * @param patchOperations
	 * @param path
	 * @return
	 */
	private List<PatchOperation> diff(JsonNode sourceJsonNode, JsonNode targetJsonNode, List<PatchOperation> patchOperations, JsonPointer path) {
		if (!Objects.equals(sourceJsonNode, targetJsonNode)) {

			if (sourceJsonNode.isArray() && targetJsonNode.isArray()) {
				
			} else if (sourceJsonNode.isObject() &&  targetJsonNode.isObject()) {
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
				
			} else {
				patchOperations.add(new ReplaceOperation(path, targetJsonNode.deepCopy()));
			}
		}
		return patchOperations;
	}

}
