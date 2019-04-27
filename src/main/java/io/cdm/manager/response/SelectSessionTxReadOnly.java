package io.cdm.manager.response;

import java.nio.ByteBuffer;

import io.cdm.backend.mysql.PacketUtil;
import io.cdm.config.Fields;
import io.cdm.manager.ManagerConnection;
import io.cdm.net.mysql.EOFPacket;
import io.cdm.net.mysql.FieldPacket;
import io.cdm.net.mysql.ResultSetHeaderPacket;
import io.cdm.net.mysql.RowDataPacket;
import io.cdm.util.LongUtil;

public final class SelectSessionTxReadOnly {
	
	private static final String SESSION_TX_READ_ONLY = "@@SESSION.TX_READ_ONLY";
	private static final int FIELD_COUNT = 1;
    private static final ResultSetHeaderPacket header = PacketUtil.getHeader(FIELD_COUNT);
    private static final FieldPacket[] fields = new FieldPacket[FIELD_COUNT];
    private static final EOFPacket eof = new EOFPacket();
    static {
        int i = 0;
        byte packetId = 0;
        header.packetId = ++packetId;

        fields[i] = PacketUtil.getField(SESSION_TX_READ_ONLY, Fields.FIELD_TYPE_INT24);
        fields[i++].packetId = ++packetId;

        eof.packetId = ++packetId;
    }

    public static void execute(ManagerConnection c) {
        ByteBuffer buffer = c.allocate();

        // write header
        buffer = header.write(buffer, c,true);

        // write fields
        for (FieldPacket field : fields) {
            buffer = field.write(buffer, c,true);
        }

        // write eof
        buffer = eof.write(buffer, c,true);

        // write rows
        byte packetId = eof.packetId;
        RowDataPacket row = new RowDataPacket(FIELD_COUNT);
        row.packetId = ++packetId;
        row.add(LongUtil.toBytes(0));
        buffer = row.write(buffer, c,true);

        // write last eof
        EOFPacket lastEof = new EOFPacket();
        lastEof.packetId = ++packetId;
        buffer = lastEof.write(buffer, c,true);

        // post write
        c.write(buffer);
    }

}
