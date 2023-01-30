/**
 * Copyright (c) 2020 Cisco Systems, Inc. See LICENSE file. 
 */
package com.cisco.app.dbconnector.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility for converting ResultSets into some Output formats
 * 
 */
public class Convertor {
	Logger logger = LoggerFactory.getLogger(Convertor.class);

	/**
	 * Convert a result set into a JSON Array
	 * 
	 * @param resultSet
	 * @return a JSONArray
	 * @throws Exception
	 */
	public Object convertToJSON(ResultSet resultSet) throws Exception {
		Map<String, Serializable> mapList = new HashMap<String, Serializable>();

		int recordCount = 0;
		while (resultSet.next()) {
			Map<String, Serializable> map = new HashMap<String, Serializable>();
			recordCount++;
			int total_rows = resultSet.getMetaData().getColumnCount();
			for (int i = 0; i < total_rows; i++) {
				String columnType = resultSet.getMetaData().getColumnTypeName(i + 1);
				String columnLabel = resultSet.getMetaData().getColumnLabel(i + 1);
				logger.debug("columnType:" + columnType);
				logger.debug("getColumnLabel :" + columnLabel);
				logger.debug("getObject :" + resultSet.getObject(i + 1));
//				if ("DATETIME".equalsIgnoreCase(columnType)) {
//					logger.warn("DATETIME: Adding quotes to the json");
//					map.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), "" + (Serializable) resultSet.getObject(i + 1));
//				} else if ("VARCHAR".equalsIgnoreCase(columnType)) {
//					map.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), (Serializable) resultSet.getObject(i + 1));
//				} else if ("INT".equalsIgnoreCase(columnType)) {
//					logger.warn("INT: Adding quotes to the json");
//					map.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), "" + (Serializable) resultSet.getObject(i + 1));
//				} else if ("BIGINT".equalsIgnoreCase(columnType)) {
//					logger.warn("BIGINT: Adding quotes to the json");
//					map.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), "" + (Serializable) resultSet.getObject(i + 1));
//				} else if ("INT UNSIGNED".equalsIgnoreCase(columnType)) {
//					logger.warn("INT: Adding quotes to the json");
//					map.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), "" + (Serializable) resultSet.getObject(i + 1));
//				} else if ("TIMESTAMP".equalsIgnoreCase(columnType)) {
//					logger.warn("TIMESTAMP: Adding quotes to the json");
//					map.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), "" + (Serializable) resultSet.getObject(i + 1));
//				} else if ("CHAR".equalsIgnoreCase(columnType)) {
//					map.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), (Serializable) resultSet.getObject(i + 1));
//				} else {
//					logger.warn("{}: {}: Adding quotes to the json",columnType,  columnLabel);
					map.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), "" + (Serializable) resultSet.getObject(i + 1));
//				}
			}
			mapList.put("record" + recordCount, (Serializable) map);
		}
		
		if(recordCount == 0) {
			return "{}";
		}
		else if(recordCount == 1) {
			return new ObjectMapper().writeValueAsString(mapList.values().toArray()[0]); 			
		}
		else  {
			List<String> myList = new ArrayList<String>();
			StringBuffer sb = new StringBuffer("[");
			for (int i = 0; i < mapList.size(); i++) {
				myList.add((new ObjectMapper().writeValueAsString(mapList.values().toArray()[i])));
			}
			sb.append("]");
			return myList; //sb.toString();
 		}
	}

	/**
	 * Convert a result set into a XML List
	 * 
	 * @param resultSet
	 * @return a XML String with list elements
	 * @throws Exception if something happens
	 */

}
