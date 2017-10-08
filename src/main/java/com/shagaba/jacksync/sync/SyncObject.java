package com.shagaba.jacksync.sync;

public class SyncObject<T> {

	private Long version;
	
	private T object;

	/**
	 * @param version
	 * @param object
	 */
	public SyncObject(Long version, T object) {
		super();
		this.version = version;
		this.object = object;
	}

	public Long getVersion() {
		return version;
	}

	public T getObject() {
		return object;
	}
}
