package io.cdm.backend.mysql.xa.recovery;

import io.cdm.backend.mysql.xa.CoordinatorLogEntry;

import java.util.Collection;

/**
 * Created by zhangchao on 2016/10/13.
 */
public interface Repository {

    void init() ;

    void put(String id, CoordinatorLogEntry coordinatorLogEntry);

    CoordinatorLogEntry get(String coordinatorId);

    Collection<CoordinatorLogEntry> findAllCommittingCoordinatorLogEntries() ;

    Collection<CoordinatorLogEntry>  getAllCoordinatorLogEntries() ;

    void writeCheckpoint(String id, Collection<CoordinatorLogEntry> checkpointContent) ;

    void close();

}
