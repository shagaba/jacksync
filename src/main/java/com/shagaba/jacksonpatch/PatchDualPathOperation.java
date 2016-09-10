package com.shagaba.jacksonpatch;

import com.fasterxml.jackson.core.JsonPointer;

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
		this.from = from;
	}


	/**
	 * @param from the source path to perform the operation from. ('/1/description')
	 * @param path the destination path to perform the operation on. ('/2/description')
	 */
	public PatchDualPathOperation(String from, String path) {
		super(path);
		setFrom(from);
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from.toString();
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		basicPathChecks(from);
		this.from = JsonPointer.compile(from);
	}


}
