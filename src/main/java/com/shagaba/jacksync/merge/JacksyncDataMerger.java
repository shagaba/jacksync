package com.shagaba.jacksync.merge;

import com.shagaba.jacksync.JacksyncData;
import com.shagaba.jacksync.Syncable;

public interface JacksyncDataMerger {

	/**
	 * 
	 * @param sourceObject
	 * @param jacksyncData
	 * @return
	 */
	public <T extends Syncable> T apply(T sourceObject, JacksyncData jacksyncData);

}
