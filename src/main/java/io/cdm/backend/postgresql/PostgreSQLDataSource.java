package io.cdm.backend.postgresql;

import io.cdm.backend.datasource.PhysicalDatasource;
import io.cdm.backend.heartbeat.DBHeartbeat;
import io.cdm.backend.mysql.nio.handler.ResponseHandler;
import io.cdm.backend.postgresql.heartbeat.PostgreSQLHeartbeat;
import io.cdm.config.model.DBHostConfig;
import io.cdm.config.model.DataHostConfig;

import java.io.IOException;

/*******************
 * PostgreSQL 后端数据源实现
 * @author Coollf
 *
 */
public class PostgreSQLDataSource extends PhysicalDatasource {
	private final PostgreSQLBackendConnectionFactory factory;

	public PostgreSQLDataSource(DBHostConfig config, DataHostConfig hostConfig,
			boolean isReadNode) {
		super(config, hostConfig, isReadNode);
		this.factory = new PostgreSQLBackendConnectionFactory();
	}

	@Override
	public DBHeartbeat createHeartBeat() {
		return new PostgreSQLHeartbeat(this);
	}

	@Override
	public void createNewConnection(ResponseHandler handler, String schema)
			throws IOException {
		factory.make(this, handler, schema);
	}

	@Override
	public boolean testConnection(String schema) throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

}
