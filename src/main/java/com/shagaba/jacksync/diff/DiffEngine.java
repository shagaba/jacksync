package com.shagaba.jacksync.diff;

import java.util.List;

import com.shagaba.jacksync.PatchOperation;
import com.shagaba.jacksync.exception.JacksyncDiffException;

public interface DiffEngine {
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws JacksyncDiffException
	 */
	public <T> List<PatchOperation> diff(T source, T target) throws JacksyncDiffException;

}
