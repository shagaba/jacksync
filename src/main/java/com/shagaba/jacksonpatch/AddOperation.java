package com.shagaba.jacksonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksonpatch.utils.JacksonUtil;
import com.shagaba.jacksonpatch.utils.JsonPathUtil;

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "add" operation.
 * 
 * The "add" operation performs one of the following functions, depending upon
 * what the target location references:
 * 
 * o If the target location specifies an array index, a new value is inserted
 * into the array at the specified index.
 * 
 * o If the target location specifies an object member that does not already
 * exist, a new member is added to the object.
 * 
 * o If the target location specifies an object member that does exist, that
 * member's value is replaced.
 * 
 * The operation object MUST contain a "value" member whose content specifies
 * the value to be added.
 * 
 * Examples:
 * 
 * 1. A JSON Patch document:
 * 
 * { "op": "add", "path": "/a/b/c", "value": [ "foo", "bar" ] }.
 * 
 * 2. Adding an Object Member:
 * 
 * An example target JSON document: { "foo": "bar"}.
 * 
 * A JSON Patch document: [ { "op": "add", "path": "/baz", "value": "qux" } ].
 * 
 * The resulting JSON document: { "baz": "qux", "foo": "bar" }.
 * 
 * 3. Adding an Array Element:
 * 
 * An example target JSON document: { "foo": [ "bar", "baz" ] }.
 * 
 * A JSON Patch document: [ { "op": "add", "path": "/foo/1", "value": "qux" } ].
 * 
 * The resulting JSON document: { "foo": [ "bar", "qux", "baz" ] }.
 * 
 * @author Shagaba
 *
 */
public class AddOperation extends PatchPathOperation {

	@JsonSerialize
	private JsonNode value;

	/**
	 * 
	 */
	public AddOperation() {
		super();
	}

	/**
	 * Constructs the add operation
	 * 
	 * @param path the path where the value will be added. ('/foo/bar/4')
	 * @param value the value to add.
	 */
	public AddOperation(String path, JsonNode value) {
		super(path);
		this.value = value;
	}

	/**
	 * @return the value to add.
	 */
	public JsonNode getValue() {
		return value;
	}

	/**
	 * @param value the value to add.
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
			if (index == pathArrayNode.size()) {
				pathArrayNode.add(value);
			} else {
				pathArrayNode.insert(index, value);
			}

		} else {
			ObjectNode pathObjectNode = (ObjectNode) pathJsonNode;
			pathObjectNode.replace(JsonPathUtil.getBaseName(getPath()), value);
		}
		return objectJsonNode;
	}
}
