package io.cdm.backend.postgresql;

import io.cdm.CDMServer;
import io.cdm.backend.mysql.nio.handler.ResponseHandler;
import io.cdm.config.model.DBHostConfig;
import io.cdm.net.NIOConnector;
import io.cdm.net.factory.BackendConnectionFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.NetworkChannel;

public class PostgreSQLBackendConnectionFactory extends
		BackendConnectionFactory {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PostgreSQLBackendConnection make(PostgreSQLDataSource pool,
			ResponseHandler handler, final String schema) throws IOException {

		final DBHostConfig dsc = pool.getConfig();
		NetworkChannel channel = this.openSocketChannel(CDMServer
				.getInstance().isAIO());

		final PostgreSQLBackendConnection c = new PostgreSQLBackendConnection(
				channel, pool.isReadNode());
		CDMServer.getInstance().getConfig().setSocketParams(c, false);
		// 设置NIOHandler
		c.setHandler(new PostgreSQLBackendConnectionHandler(c));
		c.setHost(dsc.getIp());
		c.setPort(dsc.getPort());
		c.setUser(dsc.getUser());
		c.setPassword(dsc.getPassword());
		c.setSchema(schema);
		c.setPool(pool);
		c.setResponseHandler(handler);
		c.setIdleTimeout(pool.getConfig().getIdleTimeout());
		if (channel instanceof AsynchronousSocketChannel) {
			((AsynchronousSocketChannel) channel).connect(
					new InetSocketAddress(dsc.getIp(), dsc.getPort()), c,
					(CompletionHandler) CDMServer.getInstance()
							.getConnector());
		} else {
			((NIOConnector) CDMServer.getInstance().getConnector())
					.postConnect(c);

		}
		return c;
	}
	
	
}
