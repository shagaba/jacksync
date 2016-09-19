package com.shagaba.jacksync;

import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MergeOperation extends PatchPathOperation {

    private JsonNode value;
    
    /**
	 * Constructs the merge operation
	 */
	public MergeOperation() {
		super();
	}

	/**
	 * Constructs the merge operation
	 * 
	 * @param path the path where the value will be added. ('/foo/bar/4')
	 * @param value the value to add.
	 */
	public MergeOperation(JsonPointer path, JsonNode value) {
		super(path);
		this.value = value;
	}

	/**
	 * Constructs the merge operation
	 * 
	 * @param path the path where the value will be added. ('/foo/bar/4')
	 * @param value the value to add.
	 */
	public MergeOperation(String path, JsonNode value) {
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
		merge(objectJsonNode, value);
        return objectJsonNode;
      }
	
	/**
	 * 
	 * @param sourceJsonNode
	 * @param elementJsonNode
	 */
	private void merge(JsonNode sourceJsonNode, JsonNode elementJsonNode) {
 		for (Iterator<Map.Entry<String, JsonNode>> iterator = elementJsonNode.fields() ; iterator.hasNext();) {
     		Map.Entry<String, JsonNode> elementMapEntry = iterator.next();
 			if (sourceJsonNode.isArray()) {
 				sourceJsonNode = elementJsonNode;
 			} else {
 				ObjectNode sourceObjectNode = (ObjectNode) sourceJsonNode;
	     		JsonNode nextValueJsonNode = elementMapEntry.getValue();
	     		JsonNode nextSourceJsonNode = sourceJsonNode.get(elementMapEntry.getKey());

	     		if (sourceObjectNode.has(elementMapEntry.getKey())) {
	     			if (!nextValueJsonNode.isMissingNode() && !nextSourceJsonNode.isObject()) {
	     				sourceObjectNode.replace(elementMapEntry.getKey(), elementMapEntry.getValue());
		     			continue;
	     			} 
	     		} else {
	     			sourceObjectNode.replace(elementMapEntry.getKey(), elementMapEntry.getValue());
	     			continue;
	     		}
	 			merge(nextSourceJsonNode, nextValueJsonNode);
			}
		}
	}

}
