package com.shagaba.jacksync.operation;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shagaba.jacksync.utils.JsonPointerDeserializer;

/**
 * Base class for patch operations taking a path
 * 
 * @author Shagaba
 *
 */
public abstract class PatchPathOperation extends PatchOperation {
	
    protected JsonPointer path;

	/**
	 * 
	 */
	public PatchPathOperation() {
		super();
		path = JsonPointer.compile("");
	}

	/**
	 * @param path the path to perform the operation on. ('/1/description')
	 */
	public PatchPathOperation(JsonPointer path) {
		super();
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }
		this.path = path;
	}

	/**
	 * @return the path
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	public JsonPointer getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	@JsonDeserialize(using = JsonPointerDeserializer.class)
	public void setPath(JsonPointer path) {
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }
		this.path = path;
	}

}
