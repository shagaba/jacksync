package com.shagaba.jacksonpatch;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksonpatch.exception.NoSuchPathException;
import com.shagaba.jacksonpatch.utils.JacksonUtil;

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "remove" operation.
 * 
 * The "remove" operation removes the value at the target location.
 * 
 * The target location MUST exist for the operation to be successful.
 * 
 * If removing an element from an array, any elements above the specified index
 * are shifted one position to the left.
 * 
 * Example:
 * 
 * 1. A JSON Patch document:
 * 
 * { "op": "remove", "path": "/a/b/c"}.
 * 
 * @author Shagaba
 *
 */
public class RemoveOperation extends PatchPathOperation {

	/**
	 * 
	 */
	public RemoveOperation() {
		super();
	}

	/**
	 * Constructs the add operation
	 * 
	 * @param path the path where the value will be removed. ('/foo/bar/4')
	 */
	public RemoveOperation(JsonPointer path) {
		super(path);
	}

	/**
	 * Constructs the add operation
	 * 
	 * @param path the path where the value will be removed. ('/foo/bar/4')
	 */
	public RemoveOperation(String path) {
		super(path);
	}

	@Override
	public JsonNode apply(JsonNode sourceJsonNode) {
		JsonNode pathJsonNode = JacksonUtil.locateHeadContainer(sourceJsonNode, path);
		if (pathJsonNode.isArray()) {
			ArrayNode pathArrayNode = (ArrayNode) pathJsonNode;
			int index = JacksonUtil.parseLast(path);
			if (index < 0 || index > pathArrayNode.size()) {
				throw new NoSuchPathException(String.format("No such path index - %s", index));
			}
			pathArrayNode.remove(index);

		} else {
			ObjectNode pathObjectNode = (ObjectNode) pathJsonNode;
			pathObjectNode.remove(JacksonUtil.lastFieldName(path));
		}
		return sourceJsonNode;
	}

}
