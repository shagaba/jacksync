package com.shagaba.jacksync.patch;

import java.util.List;

import com.shagaba.jacksync.exception.PatchProcessingException;
import com.shagaba.jacksync.operation.PatchOperation;

public interface PatchProcessor {

	/**
	 * 
	 * @param sourceObject
	 * @param jsonOperations
	 * @return
	 * @throws PatchProcessingException 
	 */
	public <T> T patch(T sourceObject, String jsonOperations) throws PatchProcessingException;

	/**
	 * 
	 * @param sourceObject
	 * @param operations
	 * @return
	 * @throws PatchProcessingException 
	 */
	public <T> T patch(T sourceObject, List<PatchOperation> operations) throws PatchProcessingException;

}
