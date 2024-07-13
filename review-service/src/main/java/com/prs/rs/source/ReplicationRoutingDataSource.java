package com.prs.rs.source;

import static com.prs.rs.common.ConstantValues.READ_DB;
import static com.prs.rs.common.ConstantValues.WRITE_DB;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        log.info("current dataSourceType is ReadOnly ? : {}", isReadOnly);
        return isReadOnly ? READ_DB : WRITE_DB;
    }
}