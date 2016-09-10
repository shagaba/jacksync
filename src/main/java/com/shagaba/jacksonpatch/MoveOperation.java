package com.shagaba.jacksonpatch;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.shagaba.jacksonpatch.utils.JacksonUtil;

/**
 * This is an implementation of RFC 6902 (JSON Patch) - "move" operation.
 * 
 * The "move" operation removes the value at a specified location and adds it to
 * the target location.
 * 
 * The operation object MUST contain a "from" member, which is a string
 * containing a JSON Pointer value that references the location in the target
 * document to move the value from.
 * 
 * The "from" location MUST exist for the operation to be successful.
 * 
 * This operation is functionally identical to a "remove" operation on the
 * "from" location, followed immediately by an "add" operation at the target
 * location with the value that was just removed.
 * 
 * The "from" location MUST NOT be a proper prefix of the "path" location,
 * a location cannot be moved into one of its children.
 * 
 * Example:
 * 
 * 1. A JSON Patch document:
 * 
 * { "op": "move", "from": "/a/b/c", "path": "/a/b/d" }.
 * 
 * @author Shagaba
 *
 */
public class MoveOperation extends PatchDualPathOperation {

	/**
	 * 
	 */
	public MoveOperation() {
		super();
	}

	/**
	 * 
	 * @param from the source path to perform the move operation from. ('/1/description')
	 * @param path the destination path to perform the move operation on. ('/2/description')
	 */
	public MoveOperation(JsonPointer from, JsonPointer path) {
		super(from, path);
	}

	/**
	 * 
	 * @param from the source path to perform the move operation from. ('/1/description')
	 * @param path the destination path to perform the move operation on. ('/2/description')
	 */
	public MoveOperation(String from, String path) {
		super(from, path);
	}

	@Override
	public JsonNode apply(JsonNode sourceJsonNode) {
		JsonNode pathJsonNode = JacksonUtil.locate(sourceJsonNode, from);
		JsonNode copiedValue = pathJsonNode.deepCopy();
        RemoveOperation removeOperation = new RemoveOperation(from);
		AddOperation addOperation = new AddOperation(path, copiedValue);
        return addOperation.apply(removeOperation.apply(sourceJsonNode));
      }
}
