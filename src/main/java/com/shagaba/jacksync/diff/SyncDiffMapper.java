package com.shagaba.jacksync.diff;

import com.shagaba.jacksync.exception.DiffProcessingException;
import com.shagaba.jacksync.sync.SyncData;
import com.shagaba.jacksync.sync.SyncObject;

public interface SyncDiffMapper {
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 * @throws DiffProcessingException
	 */
	public <T> SyncData diff(SyncObject<T> source, SyncObject<T> target) throws DiffProcessingException;

}
