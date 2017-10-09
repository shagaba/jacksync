package com.shagaba.jacksync.sync;

import com.shagaba.jacksync.exception.SyncException;

public interface SyncProcessor {

	/**
	 * @return the isChecksumValidationEnabled
	 */
	public boolean isChecksumValidationEnabled();

	/**
	 * @param isChecksumValidationEnabled the isChecksumValidationEnabled to set
	 */
	public void setChecksumValidationEnabled(boolean isChecksumValidationEnabled);

	/**
	 * 
	 * @param sourceObject
	 * @param syncData
	 * @return
	 * @throws SyncException 
	 */
	public <T> SyncObject<T> clientSync(SyncObject<T> sourceObject, SyncData syncData) throws SyncException ;
	
	/**
	 * 
	 * @param sourceObject
	 * @param syncData
	 * @return
	 * @throws SyncException 
	 */
	public <T> SyncObject<T> masterSync(SyncObject<T> sourceObject, SyncData syncData) throws SyncException;

}
