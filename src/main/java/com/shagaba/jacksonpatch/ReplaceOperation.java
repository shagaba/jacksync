package com.shagaba.jacksonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shagaba.jacksonpatch.utils.JacksonUtil;
import com.shagaba.jacksonpatch.utils.JsonPathUtil;

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
            int index = JacksonUtil.getArrayNodePathIndex(pathArrayNode, getPath());
            pathArrayNode.set(index, value);

        } else {
        	ObjectNode pathObjectNode = (ObjectNode) pathJsonNode;
        	pathObjectNode.replace(JsonPathUtil.getBaseName(getPath()), value);
        }
        return objectJsonNode;
      }
}
