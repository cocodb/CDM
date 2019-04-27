package io.cdm.route;

import java.sql.SQLNonTransientException;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import io.cdm.SimpleCachePool;
import io.cdm.cache.LayerCachePool;
import io.cdm.config.loader.SchemaLoader;
import io.cdm.config.loader.xml.XMLSchemaLoader;
import io.cdm.config.model.SchemaConfig;
import io.cdm.config.model.SystemConfig;
import io.cdm.route.factory.RouteStrategyFactory;

/**
 * 测试删除
 * 
 * @author huangyiming
 *
 */
public class DeleteSqlParseTest {
	protected Map<String, SchemaConfig> schemaMap;
	protected LayerCachePool cachePool = new SimpleCachePool();
    protected RouteStrategy routeStrategy;

	public DeleteSqlParseTest() {
		String schemaFile = "/route/schema.xml";
		String ruleFile = "/route/rule.xml";
		SchemaLoader schemaLoader = new XMLSchemaLoader(schemaFile, ruleFile);
		schemaMap = schemaLoader.getSchemas();
        RouteStrategyFactory.init();
        routeStrategy = RouteStrategyFactory.getRouteStrategy("druidparser");
	}

	@Test
	public void testDeleteToRoute() throws SQLNonTransientException {
		String sql = "delete t  from offer as t  ";
		SchemaConfig schema = schemaMap.get("config");
        RouteResultset rrs = routeStrategy.route(new SystemConfig(), schema, -1, sql, null,
                null, cachePool);
        Assert.assertEquals(128, rrs.getNodes().length);
        
	}



    
}
