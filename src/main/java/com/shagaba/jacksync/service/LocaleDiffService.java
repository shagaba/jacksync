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
import com.shagaba.jacksync.SyncCapsule;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.utils.ChecksumUtils;
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
	 * @throws Exception
	 */
	public SyncCapsule diff(T source, T target) throws Exception {
		List<PatchOperation> operations = diffOperations(source, target);
		SyncCapsule syncCapsule = new SyncCapsule();
		syncCapsule.setVersion(source.getVersion());
		syncCapsule.setApprovedVersion(target.getVersion());
		syncCapsule.setTargetChecksum(ChecksumUtils.computeChecksum(objectMapper.writeValueAsString(target)));
		syncCapsule.setOperations(operations);
		return syncCapsule;
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public List<PatchOperation> diffOperations(T source, T target) {
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
				List<JsonNode> commonNodes = Lists.newArrayList(sourceJsonNode);
				List<JsonNode> targetNodes = Lists.newArrayList(targetJsonNode);
				commonNodes.retainAll(targetNodes);
				
				int commonIndex = 0;
				int sourceIndex = 0;
				int targetIndex = 0;
				int maxIndex = Math.max(sourceJsonNode.size(), targetJsonNode.size());
				
				for (int index = 0; index < maxIndex; ++index) {
					JsonNode commonNode = commonNodes.get(commonIndex);
					JsonNode sourceNode = sourceJsonNode.get(sourceIndex);
					JsonNode targetNode = targetJsonNode.get(targetIndex);
					
					if (commonNode.equals(sourceNode) && commonNode.equals(targetNode)) {
						++commonIndex;
						++sourceIndex;
						++targetIndex;
					} else {
						if (commonNode.equals(sourceNode)) {
							// add missing target
							JsonPointer targetPath = JacksonUtils.append(path, Integer.toString(targetIndex++));
							patchOperations.add(new AddOperation(targetPath, targetNode.deepCopy()));
						} else if (commonNode.equals(targetNode)) {
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
