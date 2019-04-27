package io.cdm.route;

import java.util.Map;

import io.cdm.CDMServer;
import junit.framework.Assert;

import org.junit.Test;

import io.cdm.SimpleCachePool;
import io.cdm.cache.CacheService;
import io.cdm.cache.LayerCachePool;
import io.cdm.config.loader.SchemaLoader;
import io.cdm.config.loader.xml.XMLSchemaLoader;
import io.cdm.config.model.SchemaConfig;
import io.cdm.config.model.SystemConfig;
import io.cdm.route.factory.RouteStrategyFactory;
import io.cdm.server.parser.ServerParse;

public class HintTest {
	protected Map<String, SchemaConfig> schemaMap;
	protected LayerCachePool cachePool = new SimpleCachePool();
	protected RouteStrategy routeStrategy;

	public HintTest() {
		String schemaFile = "/route/schema.xml";
		String ruleFile = "/route/rule.xml";
		SchemaLoader schemaLoader = new XMLSchemaLoader(schemaFile, ruleFile);
		schemaMap = schemaLoader.getSchemas();
		CDMServer.getInstance().getConfig().getSchemas().putAll(schemaMap);
        RouteStrategyFactory.init();
        routeStrategy = RouteStrategyFactory.getRouteStrategy("fdbparser");
	}
	/**
     * 测试注解
     *
     * @throws Exception
     */
    @Test
    public void testHint() throws Exception {
        SchemaConfig schema = schemaMap.get("TESTDB");
       //使用注解（新注解，/*后面没有空格），路由到1个节点
        String sql = "/*!mycat: sql = select * from employee where sharding_id = 10010 */select * from employee";
        CacheService cacheService = new CacheService();
        RouteService routerService = new RouteService(cacheService);
        RouteResultset rrs = routerService.route(new SystemConfig(), schema, ServerParse.SELECT, sql, "UTF-8", null);
        Assert.assertTrue(rrs.getNodes().length == 1);

        //使用注解（新注解，/*后面有空格），路由到1个节点
        sql = "/*#mycat: sql = select * from employee where sharding_id = 10000 */select * from employee";
        rrs = routerService.route(new SystemConfig(), schema, ServerParse.SELECT, sql, "UTF-8", null);
        Assert.assertTrue(rrs.getNodes().length == 1);
        
        //不用注解，路由到2个节点
        sql = "select * from employee";
        rrs = routerService.route(new SystemConfig(), schema, ServerParse.SELECT, sql, "UTF-8", null);
        Assert.assertTrue(rrs.getNodes().length == 2);
    }
}
