/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * utility class to print out memory usage
 * @author jiwyatt
 * @since 12/12/2020
 *
 */
public class Memory {
	private static final Logger logger = LoggerFactory.getLogger(Memory.class);

	public Memory() {
		super();
 
	}

	public static void main(String[] args) {
		try {

			int mb = 1024 * 1024;

			// Getting the runtime reference from system
			Runtime runtime = Runtime.getRuntime();

			logger.info("##### Heap utilization statistics [MB] #####");

			// Print used memory
			logger.info("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);

			// Print free memory
			logger.info("Free Memory:" + runtime.freeMemory() / mb);

			// Print total available memory
			logger.info("Total Memory:" + runtime.totalMemory() / mb);

			// Print Maximum available memory
			logger.info("Max Memory:" + runtime.maxMemory() / mb);

		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
