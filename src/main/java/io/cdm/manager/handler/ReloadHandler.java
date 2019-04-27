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
import io.cdm.manager.response.ReloadConfig;
import io.cdm.manager.response.ReloadQueryCf;
import io.cdm.manager.response.ReloadSqlSlowTime;
import io.cdm.manager.response.ReloadUser;
import io.cdm.manager.response.ReloadUserStat;
import io.cdm.route.parser.ManagerParseReload;
import io.cdm.route.parser.util.ParseUtil;

/**
 * @author mycat
 */
public final class ReloadHandler
{

    public static void handle(String stmt, ManagerConnection c, int offset)
    {
        int rs = ManagerParseReload.parse(stmt, offset);
        switch (rs)
        {
            case ManagerParseReload.CONFIG:
                ReloadConfig.execute(c,false);
                break;
            case ManagerParseReload.CONFIG_ALL:
                ReloadConfig.execute(c,true);
                break;
            case ManagerParseReload.ROUTE:
                c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
                break;
            case ManagerParseReload.USER:
                ReloadUser.execute(c);
                break;
            case ManagerParseReload.USER_STAT:
                ReloadUserStat.execute(c);
                break;
            case ManagerParseReload.SQL_SLOW:
            	ReloadSqlSlowTime.execute(c, ParseUtil.getSQLId(stmt));
                break;           
            case ManagerParseReload.QUERY_CF:            	
            	String filted = ParseUtil.parseString(stmt) ;
            	ReloadQueryCf.execute(c, filted);
            	break;                
            default:
                c.writeErrMessage(ErrorCode.ER_YES, "Unsupported statement");
        }
    }

}