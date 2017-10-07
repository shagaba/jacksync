package com.shagaba.jacksync.sync;

public class SyncObject<T> {
	
	

	/**
	 * 
	 */
	public SyncObject() {
		super();
	}

	/**
	 * @param version
	 * @param object
	 */
	public SyncObject(Long version, T object) {
		super();
		this.version = version;
		this.object = object;
	}

	private Long version;
	
	private T object;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

}
