package com.shagaba.jacksonpatch.utils;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Strings;
import com.shagaba.jacksonpatch.exception.IllegalContainerException;
import com.shagaba.jacksonpatch.exception.NoSuchPathException;

/**
 * Utility class for dealing with Jackson library API.
 * 
 * @author Shagaba
 * 
 */
public class JacksonUtil {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * Checks if an input string is in valid JSON format
	 * 
	 * @param json input json string to validate
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
     * Locating the parent JsonNode container specified by given JSON path in the given objectJsonNode.
     *
     * @param objectJsonNode a jackson JsonNode of an object
     * @param path the path to process
	 * @return the parent JsonNode
	 */
	public static JsonNode parentPathContainer(JsonNode objectJsonNode, String path) {
		String parentPath = JsonPathUtil.getParent(path);
		return pathContainer(objectJsonNode, parentPath);
	}

    /**
     * Locating a JsonNode container specified by given JSON path in the given objectJsonNode.
     *
     * @param objectJsonNode a jackson JsonNode of an object
     * @param path the path to process
	 * @return the JsonNode
	 */
	public static JsonNode pathContainer(JsonNode objectJsonNode, String path) {
		JsonNode pathJsonNode = objectJsonNode;
		if (!JsonPathUtil.isRoot(path)) {
			JsonPointer pathPointer = JsonPointer.valueOf(path);
			pathJsonNode = objectJsonNode.at(pathPointer);
		}

		if (pathJsonNode.isMissingNode()) {
			throw new NoSuchPathException("No such path");
		}
		if (!pathJsonNode.isContainerNode()) {
			throw new IllegalContainerException("Path is not a container");
		}
		return pathJsonNode;
	}

	/**
	 * 
	 * @param arrayNode
	 * @param path
	 * @return
	 */
	public static int getArrayNodePathIndex(ArrayNode arrayNode, String path) {
		String basePath = JsonPathUtil.getBaseName(path);
		try {
			int index = Integer.parseInt(basePath);
			if (index < 0 || index >= arrayNode.size()) {
				throw new NoSuchPathException(String.format("No such path index - %s", index));
			}
			return index;
		} catch (NumberFormatException exception) {
			throw new NoSuchPathException(String.format("Path is not an index path - %s", path));
		}
	}

}
