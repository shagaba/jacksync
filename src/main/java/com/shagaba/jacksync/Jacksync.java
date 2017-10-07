package com.shagaba.jacksync;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.diff.SyncObjectDiffMapper;
import com.shagaba.jacksync.diff.processor.DiffProcessor;
import com.shagaba.jacksync.diff.processor.MergeOperationDiffProcessor;
import com.shagaba.jacksync.diff.processor.SimpleDiffProcessor;
import com.shagaba.jacksync.sync.LocalSyncService;

public class Jacksync {
	
	private ObjectMapper objectMapper;

	private LocalSyncService localSyncService;

	private SyncObjectDiffMapper syncObjectDiffMapper;

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
		this.localSyncService = jacksyncBuilder.localSyncService;
		this.syncObjectDiffMapper = jacksyncBuilder.diffMapperBuilder.syncObjectDiffMapper;
	}
	
	/**
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	/**
	 * @return the localSyncService
	 */
	public LocalSyncService getLocalSyncService() {
		return localSyncService;
	}

	/**
	 * @return the DiffMapper
	 */
	public SyncObjectDiffMapper getDiffMapper() {
		return syncObjectDiffMapper;
	}

	/**
	 * 
	 */
	public static class JacksyncBuilder {

		private ObjectMapper objectMapper;
		
		private LocalSyncService localSyncService;

		private DiffMapperBuilder diffMapperBuilder;

		/**
		 * 
		 * @param objectMapper
		 */
		public JacksyncBuilder(ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
		}
		
		/**
		 * localSyncService to set
		 */
		public JacksyncBuilder localSyncService() {
			this.localSyncService = new LocalSyncService();
			this.localSyncService.setObjectMapper(objectMapper);
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

	/**
	 * 
	 * @author Shai
	 *
	 */
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
		}
		
		/**
		 * @param diffProcessor the diffProcessor to set
		 */
		public JacksyncBuilder diffProcessor(DiffProcessor diffProcessor) {
			this.syncObjectDiffMapper = new SyncObjectDiffMapper(objectMapper, diffProcessor);
			return jacksyncBuilder;
		}
		
		/**
		 * mergeOperationDiffProcessor to set
		 */
		public JacksyncBuilder mergeOperationDiffProcessor() {
			this.syncObjectDiffMapper = new SyncObjectDiffMapper(objectMapper, new MergeOperationDiffProcessor());
			return jacksyncBuilder;
		}
		
		/**
		 * @param diffProcessor the diffProcessor to set
		 */
		public JacksyncBuilder simpleDiffProcessor() {
			this.syncObjectDiffMapper = new SyncObjectDiffMapper(objectMapper, new SimpleDiffProcessor());
			return jacksyncBuilder;
		}
	}
}
