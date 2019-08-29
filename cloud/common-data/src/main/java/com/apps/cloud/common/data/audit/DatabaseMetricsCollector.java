package com.apps.cloud.common.data.audit;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class DatabaseMetricsCollector implements QueryExecutionListener {

    private static final Logger logger = getLogger(DatabaseMetricsCollector.class);

    @Override
    public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfos) {
    }

    @Override
    public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfos) {
        logQueryInfo(queryInfos);
        enrichAuditContext(execInfo, queryInfos);
    }

    private void logQueryInfo(List<QueryInfo> queryInfos) {
        queryInfos.forEach(queryInfo -> logger.debug(queryInfo.getQuery()));
    }

    private void enrichAuditContext(ExecutionInfo execInfo, List<QueryInfo> queryInfos) {
//        AuditContext context = AuditContext.get();
//
//        context.incDbTimeMillis(execInfo.getElapsedTime());
//        context.incDbQueriesCount(queryInfos.size());
    }

}