package com.shagaba.jacksync.merge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.JacksyncData;
import com.shagaba.jacksync.Syncable;

public class JacksyncDataMasterMerger extends AbstractJacksyncDataMerger {
	
	/**
	 * @param objectMapper
	 */
	public JacksyncDataMasterMerger(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	/**
	 * 
	 * @param sourceObject
	 * @param jacksyncData
	 * @return
	 */
	@Override
	public <T extends Syncable> T apply(T sourceObject, JacksyncData jacksyncData) {
		return merge(sourceObject, jacksyncData, sourceObject.getVersion());
	}

}
