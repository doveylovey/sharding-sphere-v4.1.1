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

package org.apache.shardingsphere.shardingproxy.frontend.mysql.command.query.binary.prepare;

import org.apache.shardingsphere.sql.parser.sql.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.SetStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.dialect.mysql.*;
import org.apache.shardingsphere.sql.parser.sql.statement.dcl.*;
import org.apache.shardingsphere.sql.parser.sql.statement.ddl.*;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.*;
import org.apache.shardingsphere.sql.parser.sql.statement.rl.ChangeMasterStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.rl.StartSlaveStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.rl.StopSlaveStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.tcl.CommitStatement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * COM_STMT_PREPARE command statement checker for MySQL.
 *
 * @see <a href="https://dev.mysql.com/doc/refman/5.7/en/sql-prepared-statements.html">SQL Syntax Allowed in Prepared Statements</a>
 */
public final class MySQLComStmtPrepareChecker {

    private static final Set<Class> SQL_STATEMENTS_ALLOWED = new HashSet<>();

    static {
        SQL_STATEMENTS_ALLOWED.addAll(Arrays.asList(AlterTableStatement.class, AlterUserStatement.class, AnalyzeTableStatement.class,
                CacheIndexStatement.class, CallStatement.class, ChangeMasterStatement.class, ChecksumTableStatement.class, CommitStatement.class,
                CreateIndexStatement.class, DropIndexStatement.class, CreateDatabaseStatement.class, DropDatabaseStatement.class,
                CreateTableStatement.class, DropTableStatement.class, CreateUserStatement.class, RenameUserStatement.class, DropUserStatement.class,
                CreateViewStatement.class, DropViewStatement.class, DeleteStatement.class, DoStatement.class, FlushStatement.class,
                GrantStatement.class, InsertStatement.class, InstallPluginStatement.class, KillStatement.class, LoadIndexInfoStatement.class,
                OptimizeTableStatement.class, RenameTableStatement.class, RepairTableStatement.class, ReplaceStatement.class, ResetStatement.class,
                RevokeStatement.class, SelectStatement.class, SetStatement.class, ShowWarningsStatement.class, ShowErrorsStatement.class,
                ShowBinlogStatement.class, ShowCreateProcedureStatement.class, ShowCreateFunctionStatement.class, ShowCreateEventStatement.class,
                ShowCreateTableStatement.class, ShowCreateViewStatement.class, ShowBinaryLogsStatement.class, ShowStatusStatement.class,
                StartSlaveStatement.class, StopSlaveStatement.class, TruncateStatement.class, UninstallPluginStatement.class, UpdateStatement.class));
    }

    /**
     * Judge if SQL statement is allowed.
     *
     * @param sqlStatement SQL statement
     * @return sql statement is allowed or not
     */
    public static boolean isStatementAllowed(final SQLStatement sqlStatement) {
        return SQL_STATEMENTS_ALLOWED.contains(sqlStatement.getClass());
    }
}
