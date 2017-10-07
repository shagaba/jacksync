package com.shagaba.jacksync.operation;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Base class for patch operations taking a path & value
 * 
 * @author Shagaba
 *
 */
public abstract class PatchPathValueOperation extends PatchPathOperation {
	
    protected JsonNode value;

	/**
	 * 
	 */
	public PatchPathValueOperation() {
		super();
	}

	/**
	 * @param value the value to perform the operation with.
	 */
	public PatchPathValueOperation(JsonNode value) {
		super();
		this.value = value;
	}

	/**
	 * @param path the path to perform the operation on. ('/1/description')
	 * @param value the value to perform the operation with.
	 */
	public PatchPathValueOperation(JsonPointer path, JsonNode value) {
		super(path);
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public JsonNode getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(JsonNode value) {
		this.value = value;
	}

	
}
