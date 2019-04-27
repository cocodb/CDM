/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * https://code.google.com/p/opencloudb/.
 *
 */
package io.cdm.manager.handler;

import io.cdm.config.ErrorCode;
import io.cdm.manager.ManagerConnection;
import io.cdm.manager.response.CheckGlobalTable;
import io.cdm.manager.response.ShowBackend;
import io.cdm.manager.response.ShowBackendOld;
import io.cdm.manager.response.ShowCollation;
import io.cdm.manager.response.ShowCommand;
import io.cdm.manager.response.ShowConnection;
import io.cdm.manager.response.ShowConnectionSQL;
import io.cdm.manager.response.ShowDataNode;
import io.cdm.manager.response.ShowDataSource;
import io.cdm.manager.response.ShowDatabase;
import io.cdm.manager.response.ShowDatasourceCluster;
import io.cdm.manager.response.ShowDatasourceSyn;
import io.cdm.manager.response.ShowDatasourceSynDetail;
import io.cdm.manager.response.ShowHeartbeat;
import io.cdm.manager.response.ShowHeartbeatDetail;
import io.cdm.manager.response.ShowHelp;
import io.cdm.manager.response.ShowParser;
import io.cdm.manager.response.ShowProcessor;
import io.cdm.manager.response.ShowRouter;
import io.cdm.manager.response.ShowSQL;
import io.cdm.manager.response.ShowSQLCondition;
import io.cdm.manager.response.ShowSQLDetail;
import io.cdm.manager.response.ShowSQLExecute;
import io.cdm.manager.response.ShowSQLHigh;
import io.cdm.manager.response.ShowSQLLarge;
import io.cdm.manager.response.ShowSQLSlow;
import io.cdm.manager.response.ShowSQLSumTable;
import io.cdm.manager.response.ShowSQLSumUser;
import io.cdm.manager.response.ShowServer;
import io.cdm.manager.response.ShowSession;
import io.cdm.manager.response.ShowSqlResultSet;
import io.cdm.manager.response.ShowSysLog;
import io.cdm.manager.response.ShowSysParam;
import io.cdm.manager.response.ShowThreadPool;
import io.cdm.manager.response.ShowTime;
import io.cdm.manager.response.ShowVariables;
import io.cdm.manager.response.ShowVersion;
import io.cdm.manager.response.ShowWhiteHost;
import io.cdm.manager.response.ShowDirectMemory;
import io.cdm.route.parser.ManagerParseShow;
import io.cdm.route.parser.util.ParseUtil;
import io.cdm.server.handler.ShowCache;
import io.cdm.util.StringUtil;

/**
 * @author mycat
 */
public final class ShowHandler {

	public static void handle(String stmt, ManagerConnection c, int offset) {
		int rs = ManagerParseShow.parse(stmt, offset);
		switch (rs & 0xff) {
		case ManagerParseShow.SYSPARAM://add rainbow
			ShowSysParam.execute(c);
			break;
		case ManagerParseShow.SYSLOG: //add by zhuam
			String lines = stmt.substring(rs >>> 8).trim();
			ShowSysLog.execute(c, Integer.parseInt( lines ) );
			break;
		case ManagerParseShow.COMMAND:
			ShowCommand.execute(c);
			break;
		case ManagerParseShow.COLLATION:
			ShowCollation.execute(c);
			break;
		case ManagerParseShow.CONNECTION:
			ShowConnection.execute(c);
			break;
		case ManagerParseShow.BACKEND:
			ShowBackend.execute(c);
			break;
		case ManagerParseShow.BACKEND_OLD:
			ShowBackendOld.execute(c);
			break;
		case ManagerParseShow.CONNECTION_SQL:
			ShowConnectionSQL.execute(c);
			break;
		case ManagerParseShow.DATABASE:
			ShowDatabase.execute(c);
			break;
		case ManagerParseShow.DATANODE:
			ShowDataNode.execute(c, null);
			break;
		case ManagerParseShow.DATANODE_WHERE: {
			String name = stmt.substring(rs >>> 8).trim();
			if (StringUtil.isEmpty(name)) {
				c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
			} else {
				ShowDataNode.execute(c, name);
			}
			break;
		}
		case ManagerParseShow.DATASOURCE:
			ShowDataSource.execute(c, null);
			break;
		case ManagerParseShow.DATASOURCE_WHERE: {
			String name = stmt.substring(rs >>> 8).trim();
			if (StringUtil.isEmpty(name)) {
				c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
			} else {
				ShowDataSource.execute(c, name);
			}
			break;
		}
		case ManagerParseShow.HELP:
			ShowHelp.execute(c);
			break;
		case ManagerParseShow.HEARTBEAT:
			ShowHeartbeat.response(c);
			break;
		case ManagerParseShow.PARSER:
			ShowParser.execute(c);
			break;
		case ManagerParseShow.PROCESSOR:
			ShowProcessor.execute(c);
			break;
		case ManagerParseShow.ROUTER:
			ShowRouter.execute(c);
			break;
		case ManagerParseShow.SERVER:
			ShowServer.execute(c);
			break;
		case ManagerParseShow.WHITE_HOST:
			ShowWhiteHost.execute(c);
			break;
		case ManagerParseShow.WHITE_HOST_SET:
			ShowWhiteHost.setHost(c,ParseUtil.parseString(stmt));
			break;					
		case ManagerParseShow.SQL:
			boolean isClearSql = Boolean.valueOf( stmt.substring(rs >>> 8).trim() );
			ShowSQL.execute(c, isClearSql);
			break;
		case ManagerParseShow.SQL_DETAIL:
			ShowSQLDetail.execute(c, ParseUtil.getSQLId(stmt));
			break;
		case ManagerParseShow.SQL_EXECUTE:
			ShowSQLExecute.execute(c);
			break;
		case ManagerParseShow.SQL_SLOW:
			boolean isClearSlow = Boolean.valueOf( stmt.substring(rs >>> 8).trim() );
			ShowSQLSlow.execute(c, isClearSlow);
			break;
		case ManagerParseShow.SQL_HIGH:
			boolean isClearHigh = Boolean.valueOf( stmt.substring(rs >>> 8).trim() );
			ShowSQLHigh.execute(c, isClearHigh);
			break;
		case ManagerParseShow.SQL_LARGE:
			boolean isClearLarge = Boolean.valueOf( stmt.substring(rs >>> 8).trim() );
			ShowSQLLarge.execute(c, isClearLarge);
			break;
		case ManagerParseShow.SQL_CONDITION:
			ShowSQLCondition.execute(c);
			break;
		case ManagerParseShow.SQL_RESULTSET:
			ShowSqlResultSet.execute(c);
			break;	
		case ManagerParseShow.SQL_SUM_USER:
			boolean isClearSum = Boolean.valueOf( stmt.substring(rs >>> 8).trim() );
			ShowSQLSumUser.execute(c,isClearSum);
			break;
		case ManagerParseShow.SQL_SUM_TABLE:
			boolean isClearTable = Boolean.valueOf( stmt.substring(rs >>> 8).trim() );
			ShowSQLSumTable.execute(c, isClearTable);
			break;
		case ManagerParseShow.SLOW_DATANODE: {
			String name = stmt.substring(rs >>> 8).trim();
			if (StringUtil.isEmpty(name)) {
				c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
			} else {
				// ShowSlow.dataNode(c, name);
				c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
			}
			break;
		}
		case ManagerParseShow.SLOW_SCHEMA: {
			String name = stmt.substring(rs >>> 8).trim();
			if (StringUtil.isEmpty(name)) {
				c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
			} else {
				c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
				// ShowSlow.schema(c, name);
			}
			break;
		}
		case ManagerParseShow.THREADPOOL:
			ShowThreadPool.execute(c);
			break;
		case ManagerParseShow.CACHE:
			ShowCache.execute(c);
			break;
		case ManagerParseShow.SESSION:
			ShowSession.execute(c);
			break;
		case ManagerParseShow.TIME_CURRENT:
			ShowTime.execute(c, ManagerParseShow.TIME_CURRENT);
			break;
		case ManagerParseShow.TIME_STARTUP:
			ShowTime.execute(c, ManagerParseShow.TIME_STARTUP);
			break;
		case ManagerParseShow.VARIABLES:
			ShowVariables.execute(c);
			break;
		case ManagerParseShow.VERSION:
			ShowVersion.execute(c);
			break;
		case ManagerParseShow.HEARTBEAT_DETAIL://by songwie
			ShowHeartbeatDetail.response(c,stmt);
			break;
		case ManagerParseShow.DATASOURCE_SYNC://by songwie
			ShowDatasourceSyn.response(c,stmt);
			break;	
		case ManagerParseShow.DATASOURCE_SYNC_DETAIL://by songwie
			ShowDatasourceSynDetail.response(c,stmt);
			break;	
		case ManagerParseShow.DATASOURCE_CLUSTER://by songwie
			ShowDatasourceCluster.response(c,stmt);
			break;	
		case ManagerParseShow.DIRECTMEMORY_DETAILl:
			ShowDirectMemory.execute(c,2);
			break;
		case ManagerParseShow.DIRECTMEMORY_TOTAL:
			ShowDirectMemory.execute(c,1);
			break;
		case ManagerParseShow.CHECK_GLOBAL:
			CheckGlobalTable.execute(c, stmt);
			break;
		default:
			c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
		}
	}
}
