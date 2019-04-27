package io.cdm.route.handler;

import java.sql.SQLNonTransientException;
import java.util.Map;

import io.cdm.cache.LayerCachePool;
import io.cdm.config.model.SchemaConfig;
import io.cdm.config.model.SystemConfig;
import io.cdm.route.RouteResultset;
import io.cdm.server.ServerConnection;

/**
 * 按照注释中包含指定类型的内容做路由解析
 * 
 */
public interface HintHandler {

	public RouteResultset route(SystemConfig sysConfig, SchemaConfig schema,
                                int sqlType, String realSQL, String charset, ServerConnection sc,
                                LayerCachePool cachePool, String hintSQLValue, int hintSqlType, Map hintMap)
			throws SQLNonTransientException;
}
