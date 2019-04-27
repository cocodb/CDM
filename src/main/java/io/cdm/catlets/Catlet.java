package io.cdm.catlets;

import io.cdm.cache.LayerCachePool;
import io.cdm.config.model.SchemaConfig;
import io.cdm.config.model.SystemConfig;
import io.cdm.server.ServerConnection;
import io.cdm.sqlengine.EngineCtx;
/**
 * mycat catlet ,used to execute sql and return result to client,some like
 * database's procedure.
 * must implemented as a stateless class and can process many SQL concurrently 
 * 
 * @author wuzhih
 * 
 */
public interface Catlet {

	/*
	 * execute sql in EngineCtx and return result to client
	 */
	void processSQL(String sql, EngineCtx ctx);
	
	void route(SystemConfig sysConfig, SchemaConfig schema,
			int sqlType, String realSQL, String charset, ServerConnection sc,
			LayerCachePool cachePool) ;
	//void setRoute(RouteResultset rrs);
}
