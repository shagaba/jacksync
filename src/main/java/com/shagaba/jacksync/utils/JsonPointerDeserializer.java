package com.shagaba.jacksync.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonPointerDeserializer extends JsonDeserializer<JsonPointer> {

	@Override
	public JsonPointer deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		try {
			return JsonPointer.compile(jsonParser.getText());
		} catch (Exception e) {
			throw new JsonMappingException(jsonParser, "cannot deserialize JSON Pointer", e);
		}
	}

}
