/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.shardingscaling.core.execute.executor.writer;

import lombok.SneakyThrows;
import org.apache.shardingsphere.shardingscaling.core.config.DataSourceConfiguration;
import org.apache.shardingsphere.shardingscaling.core.config.RdbmsConfiguration;
import org.apache.shardingsphere.shardingscaling.core.datasource.DataSourceManager;
import org.apache.shardingsphere.shardingscaling.core.execute.executor.channel.Channel;
import org.apache.shardingsphere.shardingscaling.core.execute.executor.position.NopLogPosition;
import org.apache.shardingsphere.shardingscaling.core.execute.executor.record.Column;
import org.apache.shardingsphere.shardingscaling.core.execute.executor.record.DataRecord;
import org.apache.shardingsphere.shardingscaling.core.execute.executor.record.FinishedRecord;
import org.apache.shardingsphere.shardingscaling.core.execute.executor.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractJDBCWriterTest {

    private static final String TABLE_NAME = "test_table";

    private static final String INSERT_SQL = "INSERT INTO test_table (id,user,status) VALUES(?,?,?)";

    private static final String DELETE_SQL = "DELETE FROM test_table WHERE id = ?";

    private static final String UPDATE_SQL = "UPDATE test_table SET user = ?,status = ? WHERE id = ?";

    @Mock
    private DataSourceManager dataSourceManager;

    @Mock
    private AbstractSqlBuilder sqlBuilder;

    @Mock
    private DataSourceConfiguration dataSourceConfiguration;

    @Mock
    private Channel channel;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    private AbstractJDBCWriter jdbcWriter;

    @Before
    public void setUp() throws Exception {
        jdbcWriter = new AbstractJDBCWriter(getRdbmsConfiguration(), dataSourceManager) {

            @Override
            protected AbstractSqlBuilder createSqlBuilder() {
                return sqlBuilder;
            }
        };
        jdbcWriter.setChannel(channel);
        when(dataSourceManager.getDataSource(dataSourceConfiguration)).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    @SneakyThrows
    public void assertWriteInsertDataRecord() {
        DataRecord insertRecord = getDataRecord("INSERT");
        when(sqlBuilder.buildInsertSQL(insertRecord)).thenReturn(INSERT_SQL);
        when(connection.prepareStatement(INSERT_SQL)).thenReturn(preparedStatement);
        when(channel.fetchRecords(100, 3)).thenReturn(mockRecords(insertRecord));
        jdbcWriter.run();
        verify(preparedStatement).setObject(1, 1);
        verify(preparedStatement).setObject(2, 10);
        verify(preparedStatement).setObject(3, "INSERT");
        verify(preparedStatement).execute();
    }

    @Test
    public void assertDeleteDataRecord() throws SQLException {
        DataRecord deleteRecord = getDataRecord("DELETE");
        when(sqlBuilder.buildDeleteSQL(deleteRecord)).thenReturn(DELETE_SQL);
        when(connection.prepareStatement(DELETE_SQL)).thenReturn(preparedStatement);
        when(channel.fetchRecords(100, 3)).thenReturn(mockRecords(deleteRecord));
        jdbcWriter.run();
        verify(preparedStatement).setObject(1, 1);
        verify(preparedStatement).execute();
    }

    @Test
    public void assertUpdateDataRecord() throws SQLException {
        DataRecord updateRecord = getDataRecord("UPDATE");
        when(sqlBuilder.buildUpdateSQL(updateRecord)).thenReturn(UPDATE_SQL);
        when(connection.prepareStatement(UPDATE_SQL)).thenReturn(preparedStatement);
        when(channel.fetchRecords(100, 3)).thenReturn(mockRecords(updateRecord));
        jdbcWriter.run();
        verify(preparedStatement).setObject(1, 10);
        verify(preparedStatement).setObject(2, "UPDATE");
        verify(preparedStatement).setObject(3, 1);
        verify(preparedStatement).execute();
    }

    private List<Record> mockRecords(final DataRecord dataRecord) {
        List<Record> result = new LinkedList<>();
        result.add(dataRecord);
        result.add(new FinishedRecord(new NopLogPosition()));
        return result;
    }

    private DataRecord getDataRecord(final String recordType) {
        DataRecord result = new DataRecord(new NopLogPosition(), 3);
        result.setTableName(TABLE_NAME);
        result.setType(recordType);
        result.addColumn(new Column("id", 1, false, true));
        result.addColumn(new Column("user", 10, true, false));
        result.addColumn(new Column("status", recordType, true, false));
        return result;
    }

    private RdbmsConfiguration getRdbmsConfiguration() {
        RdbmsConfiguration result = new RdbmsConfiguration();
        result.setTableName(TABLE_NAME);
        result.setDataSourceConfiguration(dataSourceConfiguration);
        return result;
    }
}
