package com.shagaba.jacksync.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shagaba.jacksync.diff.ObjectDiffEngine;
import com.shagaba.jacksync.diff.processor.DiffProcessor;
import com.shagaba.jacksync.diff.processor.MergeOperationDiffProcessor;
import com.shagaba.jacksync.diff.processor.SimpleDiffProcessor;

public class Jacksync {
	
	private ObjectMapper objectMapper;

	private LocalSyncService localSyncService;

	private ObjectDiffEngine objectDiffEngine;
	
	public static JacksyncBuilder newBuilder(ObjectMapper objectMapper) {
		return new JacksyncBuilder(objectMapper);
	}
	
	private Jacksync(JacksyncBuilder jacksyncBuilder) {
		this.objectMapper = jacksyncBuilder.objectMapper;
		this.localSyncService = jacksyncBuilder.localSyncService;
		this.objectDiffEngine = jacksyncBuilder.diffMapperBuilder.objectDiffEngine;
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
	public ObjectDiffEngine getDiffMapper() {
		return objectDiffEngine;
	}



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
		
		public DiffMapperBuilder diffMapper() {
			diffMapperBuilder = new DiffMapperBuilder(this, objectMapper);
			return diffMapperBuilder;
		}
		
		public Jacksync build() {
			return new Jacksync(this);
		}
	}

	public static class DiffMapperBuilder {
		
		private JacksyncBuilder jacksyncBuilder;

		private ObjectMapper objectMapper;
		
		private ObjectDiffEngine objectDiffEngine;

		/**
		 * 
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
			this.objectDiffEngine = new ObjectDiffEngine(objectMapper, diffProcessor);
			return jacksyncBuilder;
		}
		
		/**
		 * mergeOperationDiffProcessor to set
		 */
		public JacksyncBuilder mergeOperationDiffProcessor() {
			this.objectDiffEngine = new ObjectDiffEngine(objectMapper, new MergeOperationDiffProcessor());
			return jacksyncBuilder;
		}
		
		/**
		 * @param diffProcessor the diffProcessor to set
		 */
		public JacksyncBuilder simpleDiffProcessor() {
			this.objectDiffEngine = new ObjectDiffEngine(objectMapper, new SimpleDiffProcessor());
			return jacksyncBuilder;
		}
		
	}
}
