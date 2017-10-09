package com.shagaba.jacksync.diff;

import java.util.List;

import com.shagaba.jacksync.exception.DiffProcessingException;
import com.shagaba.jacksync.operation.PatchOperation;

public interface DiffMapper {
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws DiffProcessingException 
	 */
	public <T> List<PatchOperation> diff(T source, T target) throws DiffProcessingException;

}
