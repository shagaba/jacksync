package com.shagaba.jacksonpatch;

import java.io.Serializable;
import java.util.List;

public class SyncCapsule<ID extends Serializable> implements Syncable<ID> {
	
	private ID id;
	
	private Long version;
	
	private String checksum;
	
	private Long adminVersion;
	
	private List<PatchOperation> operations;

	/**
	 * @return the id
	 */
	public ID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(ID id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the checksum
	 */
	public String getChecksum() {
		return checksum;
	}

	/**
	 * @param checksum the checksum to set
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	/**
	 * @return the adminVersion
	 */
	public Long getAdminVersion() {
		return adminVersion;
	}

	/**
	 * @param adminVersion the adminVersion to set
	 */
	public void setAdminVersion(Long adminVersion) {
		this.adminVersion = adminVersion;
	}

	/**
	 * @return the operations
	 */
	public List<PatchOperation> getOperations() {
		return operations;
	}

	/**
	 * @param operations the operations to set
	 */
	public void setOperations(List<PatchOperation> operations) {
		this.operations = operations;
	}

}
