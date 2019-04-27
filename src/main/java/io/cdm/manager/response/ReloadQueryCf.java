package io.cdm.manager.response;


import org.slf4j.Logger; import org.slf4j.LoggerFactory;

import io.cdm.manager.ManagerConnection;
import io.cdm.net.mysql.OkPacket;
import io.cdm.statistic.stat.QueryConditionAnalyzer;

public class ReloadQueryCf {
	
	private static final Logger logger = LoggerFactory.getLogger(ReloadSqlSlowTime.class);

    public static void execute(ManagerConnection c, String cf) {
    	
    	if ( cf == null ) {
            cf = "NULL";
        }
    	
    	QueryConditionAnalyzer.getInstance().setCf(cf);
    	
        StringBuilder s = new StringBuilder();
        s.append(c).append("Reset show  @@sql.condition="+ cf +" success by manager");
        
        logger.warn(s.toString());
        
        OkPacket ok = new OkPacket();
        ok.packetId = 1;
        ok.affectedRows = 1;
        ok.serverStatus = 2;
        ok.message = "Reset show  @@sql.condition success".getBytes();
        ok.write(c);
        
        System.out.println(s.toString());
    }

}
