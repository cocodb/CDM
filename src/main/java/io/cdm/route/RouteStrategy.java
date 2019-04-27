package io.cdm.route;

import java.sql.SQLNonTransientException;

import io.cdm.cache.LayerCachePool;
import io.cdm.config.model.SchemaConfig;
import io.cdm.config.model.SystemConfig;
import io.cdm.server.ServerConnection;

/**
 * 路由策略接口
 * @author wang.dw
 *
 */
public interface RouteStrategy {
	public RouteResultset route(SystemConfig sysConfig,
			SchemaConfig schema,int sqlType, String origSQL, String charset, ServerConnection sc, LayerCachePool cachePool)
			throws SQLNonTransientException;
}
