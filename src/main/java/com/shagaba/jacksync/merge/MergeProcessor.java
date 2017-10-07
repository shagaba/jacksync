package com.shagaba.jacksync.merge;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.operation.MergeOperation;

public interface MergeProcessor {
	/**
	 * 
	 * @param sourceObject
	 * @param jsonValue
	 * @return
	 */
	public <T> T merge(T sourceObject, String jsonValue);

	/**
	 * 
	 * @param sourceObject
	 * @param path
	 * @param jsonValue
	 * @return
	 */
	public <T> T merge(T sourceObject, String path, String jsonValue);

	/**
	 * 
	 * @param sourceObject
	 * @param value
	 * @return
	 */
	public <T> T merge(T sourceObject, JsonNode value);

	/**
	 * 
	 * @param sourceObject
	 * @param path
	 * @param value
	 * @return
	 */
	public <T> T merge(T sourceObject, JsonPointer path, JsonNode value);

	/**
	 * 
	 * @param sourceObject
	 * @param operation
	 * @return
	 */
	public <T> T merge(T sourceObject, MergeOperation operation);

}
