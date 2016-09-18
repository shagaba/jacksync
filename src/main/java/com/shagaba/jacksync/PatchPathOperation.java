package com.shagaba.jacksync;

import com.fasterxml.jackson.core.JsonPointer;

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
	}

	/**
	 * @param path the path to perform the operation on. ('/1/description')
	 */
	public PatchPathOperation(JsonPointer path) {
		super();
		this.path = path;
	}

	/**
	 * @param path the path to perform the operation on. ('/1/description')
	 */
	public PatchPathOperation(String path) {
		super();
		setPath(path);
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path.toString();
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		basicPathChecks(path);
		this.path = JsonPointer.compile(path);
	}

    /**
     * 
     */
    protected void basicPathChecks(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }
        if (!path.equals(path.trim())) {
            throw new IllegalArgumentException(String.format("Path has not been trimmed: '%s'", path));
        }
    }

}
