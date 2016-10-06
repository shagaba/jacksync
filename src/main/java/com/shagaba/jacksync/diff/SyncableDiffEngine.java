package com.shagaba.jacksync.diff;

import com.shagaba.jacksync.JacksyncData;
import com.shagaba.jacksync.Syncable;
import com.shagaba.jacksync.exception.JacksyncDiffException;

public interface SyncableDiffEngine {
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws JacksyncDiffException
	 */
	public <T extends Syncable> JacksyncData diff(T source, T target) throws JacksyncDiffException;

}
