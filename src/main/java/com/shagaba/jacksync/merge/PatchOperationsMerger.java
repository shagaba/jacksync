package com.shagaba.jacksync.merge;

import java.util.List;

import com.shagaba.jacksync.PatchOperation;

public interface PatchOperationsMerger {

	/**
	 * 
	 * @param sourceObject
	 * @param operations
	 * @return
	 */
	public <T> T apply(T sourceObject, List<PatchOperation> operations);

}
