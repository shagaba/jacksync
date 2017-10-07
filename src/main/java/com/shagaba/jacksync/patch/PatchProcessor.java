package com.shagaba.jacksync.patch;

import java.util.List;

import com.shagaba.jacksync.operation.PatchOperation;

public interface PatchProcessor {

	/**
	 * 
	 * @param sourceObject
	 * @param operations
	 * @return
	 */
	public <T> T patch(T sourceObject, List<PatchOperation> operations);

}
