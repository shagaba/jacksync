package com.shagaba.jacksync.operation;

import java.util.function.UnaryOperator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Abstract class providing support methods for one patch operation.
 * 
 * @author Shagaba
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "op")
@JsonSubTypes({ 
	@JsonSubTypes.Type(value = AddOperation.class, name = "add"),
	@JsonSubTypes.Type(value = RemoveOperation.class, name = "remove"),
	@JsonSubTypes.Type(value = ReplaceOperation.class, name = "replace"),
	@JsonSubTypes.Type(value = CopyOperation.class, name = "copy"),
	@JsonSubTypes.Type(value = MoveOperation.class, name = "move"),
	@JsonSubTypes.Type(value = TestOperation.class, name = "test"),
	@JsonSubTypes.Type(value = MergeOperation.class, name = "merge")}
)
public abstract class PatchOperation implements UnaryOperator<JsonNode> {

	public abstract JsonPointer getPath();

}
