package com.shagaba.jacksonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksonpatch.utils.JacksonUtil;
import com.shagaba.jacksonpatch.utils.JsonPathUtil;

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "replace" operation.
 * 
 * The "replace" operation replaces the value at the target location with a new
 * value. The operation object MUST contain a "value" member whose content
 * specifies the replacement value.
 * 
 * The target location MUST exist for the operation to be successful.
 * 
 * This operation is functionally identical to a "remove" operation for a value,
 * followed immediately by an "add" operation at the same location with the
 * replacement value.
 * 
 * Example:
 * 
 * 1. A JSON Patch document:
 * 
 * { "op": "replace", "path": "/a/b/c", "value": 42 }.
 * 
 * @author Shagaba
 *
 */
public class ReplaceOperation extends PatchPathOperation {

	@JsonSerialize
	private JsonNode value;

	/**
	 * 
	 */
	public ReplaceOperation() {
		super();
	}

	/**
	 * Constructs the replace operation
	 * 
	 * @param path the path where the value will be replaced. ('/foo/bar/4')
	 * @param value the value to replace.
	 */
	public ReplaceOperation(String path, JsonNode value) {
		super(path);
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public JsonNode getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(JsonNode value) {
		this.value = value;
	}

	@Override
	public JsonNode apply(JsonNode objectJsonNode) {
		JsonNode pathJsonNode = JacksonUtil.parentPathContainer(objectJsonNode, getPath());
		if (pathJsonNode.isArray()) {
			ArrayNode pathArrayNode = (ArrayNode) pathJsonNode;
			int index = JacksonUtil.parseBasePathIndex(pathArrayNode, getPath());
			pathArrayNode.set(index, value);

		} else {
			ObjectNode pathObjectNode = (ObjectNode) pathJsonNode;
			pathObjectNode.replace(JsonPathUtil.getBaseName(getPath()), value);
		}
		return objectJsonNode;
	}
}
