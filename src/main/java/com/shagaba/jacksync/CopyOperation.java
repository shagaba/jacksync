package com.shagaba.jacksync;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksync.utils.JacksonUtils;

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "copy" operation.
 * 
 * The "copy" operation copies the value at a specified location to the target
 * location.
 * 
 * The operation object MUST contain a "from" member, which is a string
 * containing a JSON Pointer value that references the location in the target
 * document to copy the value from.
 * 
 * The "from" location MUST exist for the operation to be successful.
 * 
 * This operation is functionally identical to an "add" operation at the target
 * location using the value specified in the "from" member.
 * 
 * Example:
 * 
 * 1. A JSON Patch document:
 * 
 * { "op": "copy", "from": "/a/b/c", "path": "/a/b/e" }.
 * 
 * @author Shagaba
 *
 */
public class CopyOperation extends PatchDualPathOperation {

	/**
	 * 
	 */
	public CopyOperation() {
		super();
	}

	/**
	 * 
	 * @param from the source path to perform the move operation from. ('/1/description')
	 * @param path the destination path to perform the move operation on. ('/2/description')
	 */
	public CopyOperation(JsonPointer from, JsonPointer path) {
		super(from, path);
	}

	/**
	 * 
	 * @param from the source path to perform the move operation from. ('/1/description')
	 * @param path the destination path to perform the move operation on. ('/2/description')
	 */
	public CopyOperation(String from, String path) {
		super(from, path);
	}

	@Override
	public JsonNode apply(JsonNode sourceJsonNode) {
		JsonNode pathJsonNode = JacksonUtils.locate(sourceJsonNode, from);
		JsonNode copiedValue = pathJsonNode.deepCopy();
		AddOperation addOperation = new AddOperation(path, copiedValue);
		return addOperation.apply(sourceJsonNode);
	}
}
