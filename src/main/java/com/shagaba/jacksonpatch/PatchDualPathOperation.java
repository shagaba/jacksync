package com.shagaba.jacksonpatch;

/**
 * Base class for patch operations taking a path
 * 
 * @author Shagaba
 *
 */
public abstract class PatchDualPathOperation extends PatchPathOperation {
	
	private String from;

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
	public PatchDualPathOperation(String from, String path) {
		super(path);
		this.from = from;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}


}
