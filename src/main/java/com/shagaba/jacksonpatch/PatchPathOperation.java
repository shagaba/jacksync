package com.shagaba.jacksonpatch;

/**
 * Base class for patch operations taking a path
 * 
 * @author Shagaba
 *
 */
public abstract class PatchPathOperation extends PatchOperation {
	
    private String path;

	/**
	 * 
	 */
	public PatchPathOperation() {
		super();
	}

	/**
	 * @param path the path to perform the operation on. ('/1/description')
	 */
	public PatchPathOperation(String path) {
		super();
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
