package com.shagaba.jacksync.operation;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksync.exception.NoSuchPathException;
import com.shagaba.jacksync.utils.JacksonUtils;

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "add" operation.
 * 
 * The "add" operation performs one of the following functions, depending upon
 * what the target location references:
 * 
 * o If the target location specifies an array index, a new value is inserted
 * into the array at the specified index.
 * 
 * o The character "-" is a new array index referenced value of a nonexistent 
 * member after the last array element "/foo/-".
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
public class AddOperation extends PatchPathValueOperation {

	/**
	 * Constructs the add operation
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
	public AddOperation(JsonPointer path, JsonNode value) {
		super(path, value);
	}

	@Override
	public JsonNode apply(JsonNode sourceJsonNode) {
		JsonNode pathJsonNode = JacksonUtils.locateHeadContainer(sourceJsonNode, path);
		if (pathJsonNode.isArray()) {
			ArrayNode pathArrayNode = (ArrayNode) pathJsonNode;
			if (JacksonUtils.isAfterLastArrayElement(path)) {
				pathArrayNode.add(value);
			} else {
				int index = JacksonUtils.parseLast(path);
				if (index < 0 || index > pathArrayNode.size()) {
					throw new NoSuchPathException(String.format("No such path index - %s", index));
				}
				
				if (index == pathArrayNode.size()) {
					pathArrayNode.add(value);
				} else {
					pathArrayNode.insert(index, value);
				}
			}

		} else {
			ObjectNode pathObjectNode = (ObjectNode) pathJsonNode;
			pathObjectNode.replace(JacksonUtils.lastFieldName(path), value);
		}
		return sourceJsonNode;
	}
}
