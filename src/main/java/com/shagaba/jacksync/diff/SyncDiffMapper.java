package com.shagaba.jacksync.diff;

import com.shagaba.jacksync.exception.JacksyncDiffException;
import com.shagaba.jacksync.sync.SyncData;
import com.shagaba.jacksync.sync.SyncObject;

public interface SyncDiffMapper {
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws JacksyncDiffException
	 */
	public <T> SyncData diff(SyncObject<T> source, SyncObject<T> target) throws JacksyncDiffException;

}
