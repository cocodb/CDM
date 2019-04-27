package io.cdm.route.handler;

import java.sql.SQLNonTransientException;
import java.util.Map;

import io.cdm.CDMServer;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;

import io.cdm.backend.datasource.PhysicalDBNode;
import io.cdm.cache.LayerCachePool;
import io.cdm.config.model.SchemaConfig;
import io.cdm.config.model.SystemConfig;
import io.cdm.route.RouteResultset;
import io.cdm.route.util.RouterUtil;
import io.cdm.server.ServerConnection;

/**
 * 处理注释中类型为datanode 的情况
 * 
 * @author zhuam
 */
public class HintDataNodeHandler implements HintHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HintSchemaHandler.class);

	@Override
	public RouteResultset route(SystemConfig sysConfig, SchemaConfig schema, int sqlType, String realSQL,
			String charset, ServerConnection sc, LayerCachePool cachePool, String hintSQLValue,int hintSqlType, Map hintMap)
					throws SQLNonTransientException {
		
		String stmt = realSQL;
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("route datanode sql hint from " + stmt);
		}
		
		RouteResultset rrs = new RouteResultset(stmt, sqlType);		
		PhysicalDBNode dataNode = CDMServer.getInstance().getConfig().getDataNodes().get(hintSQLValue);
		if (dataNode != null) {			
			rrs = RouterUtil.routeToSingleNode(rrs, dataNode.getName(), stmt);
		} else {
			String msg = "can't find hint datanode:" + hintSQLValue;
			LOGGER.warn(msg);
			throw new SQLNonTransientException(msg);
		}
		
		return rrs;
	}

}
