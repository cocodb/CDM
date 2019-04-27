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
package io.cdm.manager.response;

import io.cdm.CDMServer;
import io.cdm.backend.datasource.PhysicalDBNode;
import io.cdm.backend.datasource.PhysicalDBPool;
import io.cdm.config.ErrorCode;
import io.cdm.config.MycatConfig;
import io.cdm.config.model.SchemaConfig;
import io.cdm.manager.ManagerConnection;
import io.cdm.net.mysql.OkPacket;

/**
 * @author mycat
 */
public class ClearSlow {

    public static void dataNode(ManagerConnection c, String name) {
    	PhysicalDBNode dn = CDMServer.getInstance().getConfig().getDataNodes().get(name);
    	PhysicalDBPool ds = null;
        if (dn != null && ((ds = dn.getDbPool())!= null)) {
           // ds.getSqlRecorder().clear();
            c.write(c.writeToBuffer(OkPacket.OK, c.allocate()));
        } else {
            c.writeErrMessage(ErrorCode.ER_YES, "Invalid DataNode:" + name);
        }
    }

    public static void schema(ManagerConnection c, String name) {
        MycatConfig conf = CDMServer.getInstance().getConfig();
        SchemaConfig schema = conf.getSchemas().get(name);
        if (schema != null) {
//            Map<String, MySQLDataNode> dataNodes = conf.getDataNodes();
//            for (String n : schema.getAllDataNodes()) {
//                MySQLDataNode dn = dataNodes.get(n);
//                MySQLDataSource ds = null;
//                if (dn != null && (ds = dn.getSource()) != null) {
//                    ds.getSqlRecorder().clear();
//                }
//            }
            c.write(c.writeToBuffer(OkPacket.OK, c.allocate()));
        } else {
            c.writeErrMessage(ErrorCode.ER_YES, "Invalid Schema:" + name);
        }
    }

}