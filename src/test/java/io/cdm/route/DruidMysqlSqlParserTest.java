package io.cdm.route;

import java.sql.SQLNonTransientException;
import java.util.Map;

import io.cdm.CDMServer;
import org.junit.Test;

import io.cdm.SimpleCachePool;
import io.cdm.cache.LayerCachePool;
import io.cdm.config.loader.SchemaLoader;
import io.cdm.config.loader.xml.XMLSchemaLoader;
import io.cdm.config.model.SchemaConfig;
import io.cdm.config.model.SystemConfig;
import io.cdm.route.factory.RouteStrategyFactory;
import io.cdm.server.parser.ServerParse;
import junit.framework.Assert;

public class DruidMysqlSqlParserTest
{
	protected Map<String, SchemaConfig> schemaMap;
	protected LayerCachePool cachePool = new SimpleCachePool();
    protected RouteStrategy routeStrategy;

	public DruidMysqlSqlParserTest() {
		String schemaFile = "/route/schema.xml";
		String ruleFile = "/route/rule.xml";
		SchemaLoader schemaLoader = new XMLSchemaLoader(schemaFile, ruleFile);
		schemaMap = schemaLoader.getSchemas();
		CDMServer.getInstance().getConfig().getSchemas().putAll(schemaMap);
        RouteStrategyFactory.init();
        routeStrategy = RouteStrategyFactory.getRouteStrategy("druidparser");
	}

	@Test
	public void testLimitPage() throws SQLNonTransientException {
		String sql = "select * from offer order by id desc limit 5,10";
		SchemaConfig schema = schemaMap.get("mysqldb");
        RouteResultset rrs = routeStrategy.route(new SystemConfig(), schema, -1, sql, null,
                null, cachePool);
        Assert.assertEquals(2, rrs.getNodes().length);
        Assert.assertEquals(5, rrs.getLimitStart());
        Assert.assertEquals(10, rrs.getLimitSize());
        Assert.assertEquals(0, rrs.getNodes()[0].getLimitStart());
        Assert.assertEquals(15, rrs.getNodes()[0].getLimitSize());
        Assert.assertEquals("dn1", rrs.getNodes()[0].getName());
        Assert.assertEquals("dn2", rrs.getNodes()[1].getName());

        sql= rrs.getNodes()[0].getStatement() ;
        rrs = routeStrategy.route(new SystemConfig(), schema, -1, sql, null,
                null, cachePool);
        Assert.assertEquals(0, rrs.getNodes()[0].getLimitStart());
        Assert.assertEquals(15, rrs.getNodes()[0].getLimitSize());
        Assert.assertEquals(0, rrs.getLimitStart());
        Assert.assertEquals(15, rrs.getLimitSize());
		
        sql="select * from offer1 order by id desc limit 5,10" ;
        rrs = routeStrategy.route(new SystemConfig(), schema, -1, sql, null,
                null, cachePool);
        Assert.assertEquals(1, rrs.getNodes().length);
        Assert.assertEquals(5, rrs.getLimitStart());
        Assert.assertEquals(10, rrs.getLimitSize());
        Assert.assertEquals(5, rrs.getNodes()[0].getLimitStart());
        Assert.assertEquals(10, rrs.getNodes()[0].getLimitSize());
        Assert.assertEquals("dn1", rrs.getNodes()[0].getName());


        sql="select * from offer1 order by id desc limit 10" ;
        rrs = routeStrategy.route(new SystemConfig(), schema, -1, sql, null,
                null, cachePool);
        Assert.assertEquals(1, rrs.getNodes().length);
        Assert.assertEquals(0, rrs.getLimitStart());
        Assert.assertEquals(10, rrs.getLimitSize());
        Assert.assertEquals(0, rrs.getNodes()[0].getLimitStart());
        Assert.assertEquals(10, rrs.getNodes()[0].getLimitSize());
        Assert.assertEquals("dn1", rrs.getNodes()[0].getName());
	}

	@Test
	public void testLockTableSql() throws SQLNonTransientException{
		String sql = "lock tables goods write";
		SchemaConfig schema = schemaMap.get("TESTDB");
		RouteResultset rrs = routeStrategy.route(new SystemConfig(), schema, ServerParse.LOCK, sql, null, null, cachePool);
		Assert.assertEquals(3, rrs.getNodes().length);
	}


}
