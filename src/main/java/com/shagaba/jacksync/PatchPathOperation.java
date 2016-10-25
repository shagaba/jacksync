package com.shagaba.jacksync;

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
	
    protected static final String PATH_SEPARATOR = "/";

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
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }
		this.path = path;
	}

	/**
	 * @param path the path to perform the operation on. ('/1/description')
	 */
	public PatchPathOperation(String path) {
		super();
		basicPathChecks(path);
		if (PATH_SEPARATOR.equals(path.trim())) {
			this.path = JsonPointer.compile("");
		} else {
			this.path = JsonPointer.compile(path);
		}
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

	/**
	 * 
	 * @param path
	 */
    protected final void basicPathChecks(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path is null");
        }
        if (!path.equals(path.trim())) {
            throw new IllegalArgumentException(String.format("Path has not been trimmed: '%s'", path));
        }
    }

}
