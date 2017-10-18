package com.shagaba.jacksync.utils;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.exception.IllegalContainerException;
import com.shagaba.jacksync.exception.NoSuchPathException;

/**
 * Utility class for dealing with Jackson library API.
 * 
 * @author Shagaba
 * 
 */
public class JacksonUtils {

    public static final String SEPARATOR = "/";
    public static final String AFTER_LAST_ARRAY_ELEMENT = "-";

    /**
     * Checks if a path is a root path.
     * 
     * @param jsonPointer the path to check
     * @return true if the path is a root path, false otherwise
     */
    public static boolean isRoot(JsonPointer jsonPointer) {
    	try {
    		return jsonPointer.head() == null;
    	} catch (Exception e) {
    		return false;
    	}
    }
    
	/**
	 * 
	 * @param path
	 * @return 
	 */
	public static JsonPointer toJsonPointer(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        if (!path.equals(path.trim())) {
            throw new IllegalArgumentException(String.format("Path has not been trimmed: '%s'", path));
        }
		if (SEPARATOR.equals(path.trim())) {
			return JsonPointer.compile("");
		}
		return JsonPointer.compile(path);
	}
	
    /**
     * Locating the head (parent) JsonNode container specified by given JSON path in the given sourceJsonNode.
     *
     * @param sourceJsonNode a jackson JsonNode of an object
     * @param path the path to process
	 * @return the head (parent) JsonNode container
	 */
	public static JsonNode locateHeadContainer(JsonNode sourceJsonNode, JsonPointer path) {
		return locateContainer(sourceJsonNode, path.head());
	}

    /**
     * Locating a JsonNode container specified by given JSON path in the given sourceJsonNode.
     *
     * @param sourceJsonNode a jackson JsonNode of an object
     * @param path the path to process
	 * @return the JsonNode
	 */
	public static JsonNode locateContainer(JsonNode sourceJsonNode, JsonPointer path) {
		JsonNode pathJsonNode = locate(sourceJsonNode, path);
		if (!pathJsonNode.isContainerNode()) {
			throw new IllegalContainerException(String.format("Path is not a container - %s", path));
		}
		return pathJsonNode;
	}

    /**
     * Locating a JsonNode specified by given JSON path in the given sourceJsonNode.
     *
     * @param sourceJsonNode a jackson JsonNode of an object
     * @param path the path to process
	 * @return the JsonNode
	 */
	public static JsonNode locate(JsonNode sourceJsonNode, JsonPointer path) {
		JsonNode pathJsonNode = sourceJsonNode;
		if (!isRoot(path)) {
			pathJsonNode = sourceJsonNode.at(path);
		}
		if (pathJsonNode.isMissingNode()) {
			throw new NoSuchPathException(String.format("No such path - %s", path));
		}
		return pathJsonNode;
	}

    /**
     * Returns the base name of the path.
     *
     * @param path the path to process
     * @return The base name of the path.
     */
    public static String lastFieldName(JsonPointer jsonPointer) {
    	String lastPath = jsonPointer.last().toString();
    	// remove path separator "/" and return a clean field name
    	return lastPath.substring(1);
    }
    
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static int parseLast(JsonPointer path) {
		String fieldName = lastFieldName(path);
		try {
			return Integer.parseInt(fieldName);
		} catch (NumberFormatException exception) {
			throw new NoSuchPathException(String.format("Path is not an index path - %s", path));
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isAfterLastArrayElement(JsonPointer path) {
		String fieldName = lastFieldName(path);
		return AFTER_LAST_ARRAY_ELEMENT.equals(fieldName);
	}

	/**
	 * 
	 * @param path
	 * @param fieldName
	 * @return
	 */
	public static JsonPointer append(JsonPointer path, String fieldName) {
		return path.append(JsonPointer.compile(SEPARATOR.concat(fieldName)));
	}

}
