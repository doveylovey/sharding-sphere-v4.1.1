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

package org.apache.shardingsphere.shardingjdbc.executor;

import lombok.SneakyThrows;
import org.apache.shardingsphere.sharding.execute.sql.StatementExecuteUnit;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.connection.ShardingConnection;
import org.apache.shardingsphere.underlying.executor.QueryResult;
import org.apache.shardingsphere.underlying.executor.constant.ConnectionMode;
import org.apache.shardingsphere.underlying.executor.context.ExecutionUnit;
import org.apache.shardingsphere.underlying.executor.context.SQLUnit;
import org.apache.shardingsphere.underlying.executor.engine.InputGroup;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public final class PreparedStatementExecutorTest extends AbstractBaseExecutorTest {

    private static final String DQL_SQL = "SELECT * FROM table_x";

    private static final String DML_SQL = "DELETE FROM table_x";

    private PreparedStatementExecutor actual;

    @Override
    public void setUp() throws SQLException {
        super.setUp();
        PreparedStatementExecutor preparedStatementExecutor = new PreparedStatementExecutor(1, 1, 1, false, getConnection());
        preparedStatementExecutor.setSqlStatementContext(getSQLStatementContext());
        actual = spy(preparedStatementExecutor);
        doReturn(true).when(actual).isAccumulate();
    }

    @Test
    public void assertNoStatement() throws SQLException {
        assertFalse(actual.execute());
        assertThat(actual.executeUpdate(), is(0));
        assertThat(actual.executeQuery().size(), is(0));
    }

    @Test
    public void assertExecuteQueryForSinglePreparedStatementSuccess() throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement();
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSetMetaData.getColumnName(1)).thenReturn("column");
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("column");
        when(resultSetMetaData.getTableName(1)).thenReturn("table_x");
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnType(1)).thenReturn(Types.VARCHAR);
        when(resultSet.getString(1)).thenReturn("value");
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        setExecuteGroups(Collections.singletonList(preparedStatement), true);
        assertThat(actual.executeQuery().iterator().next().getValue(1, String.class), is("value"));
    }

    @Test
    public void assertExecuteQueryForMultiplePreparedStatementsSuccess() throws SQLException {
        PreparedStatement preparedStatement1 = getPreparedStatement();
        PreparedStatement preparedStatement2 = getPreparedStatement();
        ResultSet resultSet1 = mock(ResultSet.class);
        ResultSet resultSet2 = mock(ResultSet.class);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSetMetaData.getColumnName(1)).thenReturn("column");
        when(resultSetMetaData.getColumnLabel(1)).thenReturn("column");
        when(resultSetMetaData.getTableName(1)).thenReturn("table_x");
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnType(1)).thenReturn(Types.INTEGER);
        when(resultSet1.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSet2.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSet1.getInt(1)).thenReturn(1);
        when(resultSet2.getInt(1)).thenReturn(2);
        when(preparedStatement1.executeQuery()).thenReturn(resultSet1);
        when(preparedStatement2.executeQuery()).thenReturn(resultSet2);
        setExecuteGroups(Arrays.asList(preparedStatement1, preparedStatement2), true);
        List<QueryResult> result = actual.executeQuery();
        assertThat(String.valueOf(result.get(0).getValue(1, int.class)), is("1"));
        assertThat(String.valueOf(result.get(1).getValue(1, int.class)), is("2"));
        verify(preparedStatement1).executeQuery();
        verify(preparedStatement2).executeQuery();
    }

    @Test
    public void assertExecuteQueryForSinglePreparedStatementFailure() throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement();
        SQLException exp = new SQLException();
        when(preparedStatement.executeQuery()).thenThrow(exp);
        setExecuteGroups(Collections.singletonList(preparedStatement), true);
        assertThat(actual.executeQuery(), is(Collections.singletonList((QueryResult) null)));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void assertExecuteQueryForMultiplePreparedStatementsFailure() throws SQLException {
        PreparedStatement preparedStatement1 = getPreparedStatement();
        PreparedStatement preparedStatement2 = getPreparedStatement();
        SQLException exp = new SQLException();
        when(preparedStatement1.executeQuery()).thenThrow(exp);
        when(preparedStatement2.executeQuery()).thenThrow(exp);
        setExecuteGroups(Arrays.asList(preparedStatement1, preparedStatement2), true);
        List<QueryResult> actualResultSets = actual.executeQuery();
        assertThat(actualResultSets, is(Arrays.asList((QueryResult) null, null)));
        verify(preparedStatement1).executeQuery();
        verify(preparedStatement2).executeQuery();
    }

    @Test
    public void assertExecuteUpdateForSinglePreparedStatementSuccess() throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement();
        when(preparedStatement.executeUpdate()).thenReturn(10);
        setExecuteGroups(Collections.singletonList(preparedStatement), false);
        assertThat(actual.executeUpdate(), is(10));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void assertExecuteUpdateForMultiplePreparedStatementsSuccess() throws SQLException {
        PreparedStatement preparedStatement1 = getPreparedStatement();
        PreparedStatement preparedStatement2 = getPreparedStatement();
        when(preparedStatement1.executeUpdate()).thenReturn(10);
        when(preparedStatement2.executeUpdate()).thenReturn(20);
        setExecuteGroups(Arrays.asList(preparedStatement1, preparedStatement2), false);
        assertThat(actual.executeUpdate(), is(30));
        verify(preparedStatement1).executeUpdate();
        verify(preparedStatement2).executeUpdate();
    }

    @Test
    public void assertExecuteUpdateForSinglePreparedStatementFailure() throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement();
        SQLException exp = new SQLException();
        when(preparedStatement.executeUpdate()).thenThrow(exp);
        setExecuteGroups(Collections.singletonList(preparedStatement), false);
        assertThat(actual.executeUpdate(), is(0));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void assertExecuteUpdateForMultiplePreparedStatementsFailure() throws SQLException {
        PreparedStatement preparedStatement1 = getPreparedStatement();
        PreparedStatement preparedStatement2 = getPreparedStatement();
        SQLException exp = new SQLException();
        when(preparedStatement1.executeUpdate()).thenThrow(exp);
        when(preparedStatement2.executeUpdate()).thenThrow(exp);
        setExecuteGroups(Arrays.asList(preparedStatement1, preparedStatement2), false);
        assertThat(actual.executeUpdate(), is(0));
        verify(preparedStatement1).executeUpdate();
        verify(preparedStatement2).executeUpdate();
    }

    @Test
    public void assertExecuteForSinglePreparedStatementSuccessWithDML() throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement();
        when(preparedStatement.execute()).thenReturn(false);
        setExecuteGroups(Collections.singletonList(preparedStatement), false);
        assertFalse(actual.execute());
        verify(preparedStatement).execute();
    }

    @Test
    public void assertExecuteForMultiplePreparedStatementsSuccessWithDML() throws SQLException {
        PreparedStatement preparedStatement1 = getPreparedStatement();
        PreparedStatement preparedStatement2 = getPreparedStatement();
        when(preparedStatement1.execute()).thenReturn(false);
        when(preparedStatement2.execute()).thenReturn(false);
        setExecuteGroups(Arrays.asList(preparedStatement1, preparedStatement2), false);
        assertFalse(actual.execute());
        verify(preparedStatement1).execute();
        verify(preparedStatement2).execute();
    }

    @Test
    public void assertExecuteForSinglePreparedStatementFailureWithDML() throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement();
        SQLException exp = new SQLException();
        when(preparedStatement.execute()).thenThrow(exp);
        setExecuteGroups(Collections.singletonList(preparedStatement), false);
        assertFalse(actual.execute());
        verify(preparedStatement).execute();
    }

    @Test
    public void assertExecuteForMultiplePreparedStatementsFailureWithDML() throws SQLException {
        PreparedStatement preparedStatement1 = getPreparedStatement();
        PreparedStatement preparedStatement2 = getPreparedStatement();
        SQLException exp = new SQLException();
        when(preparedStatement1.execute()).thenThrow(exp);
        when(preparedStatement2.execute()).thenThrow(exp);
        setExecuteGroups(Arrays.asList(preparedStatement1, preparedStatement2), false);
        assertFalse(actual.execute());
        verify(preparedStatement1).execute();
        verify(preparedStatement2).execute();
    }

    @Test
    public void assertExecuteForSinglePreparedStatementWithDQL() throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement();
        when(preparedStatement.execute()).thenReturn(true);
        setExecuteGroups(Collections.singletonList(preparedStatement), true);
        assertTrue(actual.execute());
        verify(preparedStatement).execute();
    }

    @Test
    public void assertExecuteForMultiplePreparedStatements() throws SQLException {
        PreparedStatement preparedStatement1 = getPreparedStatement();
        PreparedStatement preparedStatement2 = getPreparedStatement();
        when(preparedStatement1.execute()).thenReturn(true);
        when(preparedStatement2.execute()).thenReturn(true);
        setExecuteGroups(Arrays.asList(preparedStatement1, preparedStatement2), true);
        assertTrue(actual.execute());
        verify(preparedStatement1).execute();
        verify(preparedStatement2).execute();
    }

    private PreparedStatement getPreparedStatement() throws SQLException {
        PreparedStatement statement = mock(PreparedStatement.class);
        ShardingConnection connection = mock(ShardingConnection.class);
        DatabaseMetaData databaseMetaData = mock(DatabaseMetaData.class);
        when(databaseMetaData.getURL()).thenReturn("jdbc:h2:mem:ds_master;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MYSQL");
        when(connection.getMetaData()).thenReturn(databaseMetaData);
        when(statement.getConnection()).thenReturn(connection);
        return statement;
    }

    @SneakyThrows
    private void setExecuteGroups(final List<PreparedStatement> preparedStatements, final boolean isQuery) {
        Collection<InputGroup<StatementExecuteUnit>> executeGroups = new LinkedList<>();
        List<StatementExecuteUnit> preparedStatementExecuteUnits = new LinkedList<>();
        executeGroups.add(new InputGroup<>(preparedStatementExecuteUnits));
        for (PreparedStatement each : preparedStatements) {
            preparedStatementExecuteUnits.add(
                    new StatementExecuteUnit(new ExecutionUnit("ds_0", new SQLUnit(isQuery ? DQL_SQL : DML_SQL, Collections.singletonList(1))), each, ConnectionMode.MEMORY_STRICTLY));
        }
        Field field = PreparedStatementExecutor.class.getSuperclass().getDeclaredField("inputGroups");
        field.setAccessible(true);
        field.set(actual, executeGroups);
    }
}