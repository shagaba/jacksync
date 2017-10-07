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
public abstract class PatchDualPathOperation extends PatchPathOperation {
	
	protected JsonPointer from;

	/**
	 * 
	 */
	public PatchDualPathOperation() {
		super();
	}

	/**
	 * @param from the source path to perform the operation from. ('/1/description')
	 * @param path the destination path to perform the operation on. ('/2/description')
	 */
	public PatchDualPathOperation(JsonPointer from, JsonPointer path) {
		super(path);
        if (from == null) {
            throw new IllegalArgumentException("From is null");
        }
		this.from = from;
	}

	/**
	 * @return the from
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	public JsonPointer getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	@JsonDeserialize(using = JsonPointerDeserializer.class)
	public void setFrom(JsonPointer from) {
        if (from == null) {
            throw new IllegalArgumentException("From is null");
        }
		this.from = from;
	}


}
