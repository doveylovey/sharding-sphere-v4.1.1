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

package org.apache.shardingsphere.shardingjdbc.jdbc.core.statement;

import org.apache.shardingsphere.shardingjdbc.common.base.AbstractShadowJDBCDatabaseAndTableTest;
import org.apache.shardingsphere.underlying.common.database.type.DatabaseTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class ShadowPreparedStatementTest extends AbstractShadowJDBCDatabaseAndTableTest {

    private static final String INIT_INSERT_SQL = "INSERT INTO t_encrypt (id, cipher_pwd, plain_pwd) VALUES (99, 'cipher', 'plain')";

    private static final String INSERT_SQL = "INSERT INTO t_encrypt (id, cipher_pwd, plain_pwd) VALUES (?, ?, ?)";

    private static final String SHADOW_INSERT_SQL = "INSERT INTO t_encrypt (id, cipher_pwd, plain_pwd, shadow) VALUES (?, ?, ?, ?)";

    private static final String DELETE_SQL = "DELETE FROM t_encrypt WHERE plain_pwd = ?";

    private static final String SHADOW_DELETE_SQL = "DELETE FROM t_encrypt WHERE plain_pwd = ? AND shadow = ?";

    private static final String SELECT_SQL = "SELECT id, cipher_pwd, plain_pwd FROM t_encrypt";

    private static final String SELECT_SQL_BY_ID = "SELECT id, cipher_pwd, plain_pwd FROM t_encrypt WHERE id = ?";

    private static final String CLEAN_SQL = "DELETE FROM t_encrypt";

    private static final String UPDATE_SQL = "UPDATE t_encrypt SET cipher_pwd = ? WHERE id = ?";

    private static final String SHADOW_UPDATE_SQL = "UPDATE t_encrypt SET cipher_pwd = ? WHERE id = ? AND shadow = ?";

    @Test
    public void assertInsertWithExecute() throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_SQL)) {
            statement.setObject(1, 2);
            statement.setString(2, "cipher");
            statement.setString(3, "plain");
            statement.execute();
        }
        assertResultSet(false, 2, "cipher");
        assertResultSet(true, 1, "cipher");
    }

    @Test
    public void assertShadowInsertWithExecute() throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SHADOW_INSERT_SQL)) {
            statement.setObject(1, 1);
            statement.setString(2, "cipher");
            statement.setString(3, "plain");
            statement.setBoolean(4, true);
            statement.execute();
        }
        assertResultSet(false, 1, "cipher");
        assertResultSet(true, 2, "cipher");
    }

    @Test
    public void assertDeleteWithExecute() throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_SQL)) {
            statement.setObject(1, "plain");
            statement.executeUpdate();
        }
        assertResultSet(false, 0, "cipher");
    }

    @Test
    public void assertShadowDeleteWithExecute() throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SHADOW_DELETE_SQL)) {
            statement.setObject(1, "plain");
            statement.setBoolean(2, true);
            statement.executeUpdate();
        }
        assertResultSet(true, 0, "cipher");
    }

    @Test
    public void assertUpdateWithExecuteUpdate() throws SQLException {
        int result;
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setString(1, "cipher_pwd");
            statement.setInt(2, 99);
            result = statement.executeUpdate();
        }

        assertThat(result, is(1));
        assertResultSet(false, 99, 1, "cipher_pwd");
    }

    @Test
    public void assertShadowUpdateWithExecuteUpdate() throws SQLException {
        int result;
        try (PreparedStatement statement = getConnection().prepareStatement(SHADOW_UPDATE_SQL)) {
            statement.setString(1, "cipher_pwd");
            statement.setInt(2, 99);
            statement.setBoolean(3, true);
            result = statement.executeUpdate();
        }

        assertThat(result, is(1));
        assertResultSet(true, 99, 1, "cipher_pwd");
    }

    private void assertResultSet(final boolean isShadow, final int resultSetCount, final Object cipherPwd) throws SQLException {
        final Map<String, DataSource> dataMaps = getDatabaseTypeMap().get(DatabaseTypes.getActualDatabaseType("H2"));
        DataSource dataSource = isShadow ? dataMaps.get("jdbc_1") : dataMaps.get("jdbc_0");
        try (Statement statement = dataSource.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_SQL);
            int count = 1;
            while (resultSet.next()) {
                assertThat(resultSet.getObject("cipher_pwd"), is(cipherPwd));
                count += 1;
            }
            assertThat(count - 1, is(resultSetCount));
        }
    }

    private void assertResultSet(final boolean isShadow, final int id, final int resultSetCount, final Object cipherPwd) throws SQLException {
        final Map<String, DataSource> dataMaps = getDatabaseTypeMap().get(DatabaseTypes.getActualDatabaseType("H2"));
        DataSource dataSource = isShadow ? dataMaps.get("jdbc_1") : dataMaps.get("jdbc_0");
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(SELECT_SQL_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            int count = 1;
            while (resultSet.next()) {
                assertThat(resultSet.getObject("cipher_pwd"), is(cipherPwd));
                count += 1;
            }
            assertThat(count - 1, is(resultSetCount));
        }
    }

    @Before
    public void init() throws SQLException {
        try (Statement statement = getActualConnection().createStatement()) {
            statement.execute(INIT_INSERT_SQL);
        }
        try (Statement statement = getShadowConnection().createStatement()) {
            statement.execute(INIT_INSERT_SQL);
        }
    }

    @After
    public void clean() throws SQLException {
        try (Statement statement = getActualConnection().createStatement()) {
            statement.execute(CLEAN_SQL);

        }
        try (Statement statement = getShadowConnection().createStatement()) {
            statement.execute(CLEAN_SQL);
        }
    }
}
