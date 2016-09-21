package com.shagaba.jacksync.utils;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
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

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
	 * Checks if an input string is in valid JSON format
	 * 
	 * @param json input JSON string to validate
	 * @return true if the input string is in valid JSON format
	 */
	public static boolean isValidJson(String json) {
		if (Strings.isNullOrEmpty(json)) {
			return false;
		}
		try {
			toJsonNode(json);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Deserialize JSON content as jackson JsonNode instance.
	 * 
	 * @param json JSON content to parse to build the jackson JsonNode instance
	 * @return a jackson JsonNode if valid JSON content found
	 */
	public static JsonNode toJsonNode(String json) {
		try {
			return OBJECT_MAPPER.readTree(json);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
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
