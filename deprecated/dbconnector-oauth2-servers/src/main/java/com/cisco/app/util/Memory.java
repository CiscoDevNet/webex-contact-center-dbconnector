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

 

	// Here's a simple Java method that uses the `Runtime` class to print out information about the JVM's memory usage:
	public static void logMemory() {
		try {
			var bytesPerMegabyte = 1024 * 1024;
			Runtime runtime = Runtime.getRuntime();
			long maxMemory = runtime.maxMemory() / bytesPerMegabyte; // Max memory JVM will attempt to use
			long allocatedMemory = runtime.totalMemory() / bytesPerMegabyte; // Total memory currently in use by the JVM
			long freeMemory = runtime.freeMemory() / bytesPerMegabyte; // Free memory out of the total memory
			long totalFreeMemory = (freeMemory + (maxMemory - allocatedMemory));
			logger.info("Max memory:{} Meg Allocated memory:{} Meg Free memory:{} Meg Total free memory:{} Meg", maxMemory, allocatedMemory, freeMemory, totalFreeMemory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
