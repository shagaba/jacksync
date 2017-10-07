package com.shagaba.jacksync.sync;

import java.util.List;

import com.shagaba.jacksync.operation.PatchOperation;

public class SyncData {
	
	private Long version;
	
	private Long masterVersion;
	
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
	 * @return the masterVersion
	 */
	public Long getMasterVersion() {
		return masterVersion;
	}

	/**
	 * @param masterVersion the masterVersion to set
	 */
	public void setMasterVersion(Long masterVersion) {
		this.masterVersion = masterVersion;
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
