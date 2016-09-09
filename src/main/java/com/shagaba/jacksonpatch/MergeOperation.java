package com.shagaba.jacksonpatch;

import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MergeOperation extends PatchOperation {

	@JsonSerialize
    private JsonNode value;
    
    /**
	 * 
	 */
	public MergeOperation() {
		super();
	}

	/**
	 * @param value
	 */
	public MergeOperation(JsonNode value) {
		super();
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
