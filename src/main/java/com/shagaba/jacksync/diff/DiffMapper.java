package com.shagaba.jacksync.diff;

import java.util.List;

import com.shagaba.jacksync.exception.JacksyncDiffException;
import com.shagaba.jacksync.operation.PatchOperation;

public interface DiffMapper {
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws JacksyncDiffException
	 */
	public <T> List<PatchOperation> diff(T source, T target) throws JacksyncDiffException;

}
