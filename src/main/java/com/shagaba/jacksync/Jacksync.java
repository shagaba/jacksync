package com.shagaba.jacksync;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.diff.SyncDiffMapper;
import com.shagaba.jacksync.diff.SyncObjectDiffMapper;
import com.shagaba.jacksync.diff.strategy.DiffStrategy;
import com.shagaba.jacksync.diff.strategy.MergeOperationDiffStrategy;
import com.shagaba.jacksync.diff.strategy.SimpleDiffStrategy;
import com.shagaba.jacksync.sync.SyncProcessor;
import com.shagaba.jacksync.sync.LocalSyncProcessor;

public class Jacksync {
	
	private ObjectMapper objectMapper;

	private SyncProcessor syncProcessor;

	private SyncDiffMapper syncDiffMapper;

	/**
	 * 
	 * @param objectMapper
	 * @return
	 */
	public static JacksyncBuilder builder(ObjectMapper objectMapper) {
		return new JacksyncBuilder(objectMapper);
	}
	
	/**
	 * 
	 * @param jacksyncBuilder
	 */
	private Jacksync(JacksyncBuilder jacksyncBuilder) {
		this.objectMapper = jacksyncBuilder.objectMapper;
		this.syncProcessor = jacksyncBuilder.localSyncProcessor;
		this.syncDiffMapper = jacksyncBuilder.diffMapperBuilder.syncObjectDiffMapper;
	}
	
	/**
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	/**
	 * @return the localSync
	 */
	public SyncProcessor getSyncProcessor() {
		return syncProcessor;
	}

	/**
	 * @return the DiffMapper
	 */
	public SyncDiffMapper getDiffMapper() {
		return syncDiffMapper;
	}

	public static class JacksyncBuilder {

		private ObjectMapper objectMapper;
		
		private LocalSyncProcessor localSyncProcessor;

		private DiffMapperBuilder diffMapperBuilder;

		/**
		 * 
		 * @param objectMapper
		 */
		public JacksyncBuilder(ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
		}
		
		/**
		 * localSync to set
		 */
		public JacksyncBuilder syncProcessor() {
			this.localSyncProcessor = new LocalSyncProcessor(objectMapper);
			return this;
		}

		/**
		 * 
		 * @return
		 */
		public DiffMapperBuilder diffMapper() {
			diffMapperBuilder = new DiffMapperBuilder(this, objectMapper);
			return diffMapperBuilder;
		}

		/**
		 * 
		 * @return
		 */
		public Jacksync build() {
			return new Jacksync(this);
		}
	}

	public static class DiffMapperBuilder {
		
		private JacksyncBuilder jacksyncBuilder;

		private ObjectMapper objectMapper;
		
		private SyncObjectDiffMapper syncObjectDiffMapper;

		/**
		 * 
		 * @param jacksyncBuilder
		 * @param objectMapper
		 */
		public DiffMapperBuilder(JacksyncBuilder jacksyncBuilder, ObjectMapper objectMapper) {
			this.jacksyncBuilder = jacksyncBuilder;
			this.objectMapper = objectMapper;
			this.syncObjectDiffMapper = new SyncObjectDiffMapper(objectMapper);
		}
		
		/**
		 * @param diffStrategy the diffStrategy to set
		 */
		public JacksyncBuilder diffStrategy(DiffStrategy diffStrategy) {
			this.syncObjectDiffMapper = new SyncObjectDiffMapper(objectMapper, diffStrategy);
			return jacksyncBuilder;
		}
		
		/**
		 * mergeOperationDiffStrategy to set
		 */
		public JacksyncBuilder mergeOperationDiffProcessor() {
			this.syncObjectDiffMapper = new SyncObjectDiffMapper(objectMapper, new MergeOperationDiffStrategy());
			return jacksyncBuilder;
		}
		
		/**
		 * simpleDiffStrategy to set
		 */
		public JacksyncBuilder simpleDiffStrategy() {
			this.syncObjectDiffMapper = new SyncObjectDiffMapper(objectMapper, new SimpleDiffStrategy());
			return jacksyncBuilder;
		}
		
		/**
		 * @param isComputeChecksum the isComputeChecksum to set
		 */
		public JacksyncBuilder computeChecksum(boolean isComputeChecksum) {
			this.syncObjectDiffMapper.setComputeChecksum(isComputeChecksum);
			return jacksyncBuilder;
		}
	}
}
