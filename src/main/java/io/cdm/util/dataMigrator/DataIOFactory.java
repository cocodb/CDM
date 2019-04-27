package io.cdm.util.dataMigrator;

import io.cdm.util.dataMigrator.dataIOImpl.MysqlDataIO;
import io.cdm.util.exception.DataMigratorException;

public class DataIOFactory {

	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";
	
	public static DataIO createDataIO(String dbType){
		switch (dbType) {
		case MYSQL:
			return new MysqlDataIO();
		default:
			throw new DataMigratorException("dbType:"+dbType+" is not support for the moment!");
		}
	}
}
