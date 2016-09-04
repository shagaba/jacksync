package com.shagaba.jacksonpatch;

import java.io.Serializable;

public interface Syncable<ID extends Serializable> {

	/**
	 * @return the id
	 */
	public ID getId();

	/**
	 * @param id the id to set
	 */
	public void setId(ID id);

	/**
     * @return the version
     */
    public Long getVersion();

    /**
     * @param version the version to set
     */
    public void setVersion(Long version);

}
