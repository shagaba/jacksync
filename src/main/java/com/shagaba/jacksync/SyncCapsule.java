package com.shagaba.jacksync;

import java.util.List;

public class SyncCapsule {
	
	private Long version;
	
	private Long approvedVersion;
	
	private String targetChecksum;
	
	private List<PatchOperation> operations;

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
	 * @return the approvedVersion
	 */
	public Long getApprovedVersion() {
		return approvedVersion;
	}

	/**
	 * @param approvedVersion the approvedVersion to set
	 */
	public void setApprovedVersion(Long approvedVersion) {
		this.approvedVersion = approvedVersion;
	}

	/**
	 * @return the targetChecksum
	 */
	public String getTargetChecksum() {
		return targetChecksum;
	}

	/**
	 * @param targetChecksum the targetChecksum to set
	 */
	public void setTargetChecksum(String targetChecksum) {
		this.targetChecksum = targetChecksum;
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
