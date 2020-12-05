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

package org.apache.shardingsphere.sql.parser.mysql.visitor.impl;

import org.apache.shardingsphere.sql.parser.api.ASTNode;
import org.apache.shardingsphere.sql.parser.api.visitor.statement.DALVisitor;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.*;
import org.apache.shardingsphere.sql.parser.mysql.visitor.MySQLVisitor;
import org.apache.shardingsphere.sql.parser.sql.segment.dal.FromSchemaSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dal.FromTableSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dal.ShowLikeSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dal.VariableSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.SchemaSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.SetStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.dialect.mysql.*;
import org.apache.shardingsphere.sql.parser.sql.value.identifier.IdentifierValue;
import org.apache.shardingsphere.sql.parser.sql.value.literal.impl.StringLiteralValue;

/**
 * DAL visitor for MySQL.
 */
public final class MySQLDALVisitor extends MySQLVisitor implements DALVisitor {

    @Override
    public ASTNode visitUninstallPlugin(final UninstallPluginContext ctx) {
        return new UninstallPluginStatement();
    }

    @Override
    public ASTNode visitShowBinaryLogs(final ShowBinaryLogsContext ctx) {
        return new ShowBinaryLogsStatement();
    }

    @Override
    public ASTNode visitShowStatus(final ShowStatusContext ctx) {
        return new ShowStatusStatement();
    }

    @Override
    public ASTNode visitShowCreateView(final ShowCreateViewContext ctx) {
        return new ShowCreateViewStatement();
    }

    @Override
    public ASTNode visitShowCreateEvent(final ShowCreateEventContext ctx) {
        return new ShowCreateEventStatement();
    }

    @Override
    public ASTNode visitShowCreateFunction(final ShowCreateFunctionContext ctx) {
        return new ShowCreateFunctionStatement();
    }

    @Override
    public ASTNode visitShowCreateProcedure(final ShowCreateProcedureContext ctx) {
        return new ShowCreateProcedureStatement();
    }

    @Override
    public ASTNode visitShowBinlogEvents(final ShowBinlogEventsContext ctx) {
        return new ShowBinlogStatement();
    }

    @Override
    public ASTNode visitShowErrors(final ShowErrorsContext ctx) {
        return new ShowErrorsStatement();
    }

    @Override
    public ASTNode visitShowWarnings(final ShowWarningsContext ctx) {
        return new ShowWarningsStatement();
    }

    @Override
    public ASTNode visitResetStatement(final ResetStatementContext ctx) {
        return new ResetStatement();
    }

    @Override
    public ASTNode visitRepairTable(final RepairTableContext ctx) {
        return new RepairTableStatement();
    }

    @Override
    public ASTNode visitAnalyzeTable(final AnalyzeTableContext ctx) {
        return new AnalyzeTableStatement();
    }

    @Override
    public ASTNode visitCacheIndex(final CacheIndexContext ctx) {
        return new CacheIndexStatement();
    }

    @Override
    public ASTNode visitChecksumTable(final ChecksumTableContext ctx) {
        return new ChecksumTableStatement();
    }

    @Override
    public ASTNode visitFlush(final FlushContext ctx) {
        return new FlushStatement();
    }

    @Override
    public ASTNode visitKill(final KillContext ctx) {
        return new KillStatement();
    }

    @Override
    public ASTNode visitLoadIndexInfo(final LoadIndexInfoContext ctx) {
        return new LoadIndexInfoStatement();
    }

    @Override
    public ASTNode visitInstallPlugin(final InstallPluginContext ctx) {
        return new InstallPluginStatement();
    }

    @Override
    public ASTNode visitOptimizeTable(final OptimizeTableContext ctx) {
        return new OptimizeTableStatement();
    }

    @Override
    public ASTNode visitUse(final UseContext ctx) {
        UseStatement result = new UseStatement();
        result.setSchema(((IdentifierValue) visit(ctx.schemaName())).getValue());
        return result;
    }

    @Override
    public ASTNode visitDesc(final DescContext ctx) {
        DescribeStatement result = new DescribeStatement();
        result.setTable((SimpleTableSegment) visit(ctx.tableName()));
        return result;
    }

    @Override
    public ASTNode visitShowDatabases(final ShowDatabasesContext ctx) {
        return new ShowDatabasesStatement();
    }

    @Override
    public ASTNode visitShowTables(final ShowTablesContext ctx) {
        ShowTablesStatement result = new ShowTablesStatement();
        if (null != ctx.fromSchema()) {
            result.setFromSchema((FromSchemaSegment) visit(ctx.fromSchema()));
        }
        return result;
    }

    @Override
    public ASTNode visitShowTableStatus(final ShowTableStatusContext ctx) {
        ShowTableStatusStatement result = new ShowTableStatusStatement();
        if (null != ctx.fromSchema()) {
            result.setFromSchema((FromSchemaSegment) visit(ctx.fromSchema()));
        }
        return result;
    }

    @Override
    public ASTNode visitShowColumns(final ShowColumnsContext ctx) {
        ShowColumnsStatement result = new ShowColumnsStatement();
        if (null != ctx.fromTable()) {
            result.setTable(((FromTableSegment) visit(ctx.fromTable())).getTable());
        }
        if (null != ctx.fromSchema()) {
            result.setFromSchema((FromSchemaSegment) visit(ctx.fromSchema()));
        }
        return result;
    }

    @Override
    public ASTNode visitShowIndex(final ShowIndexContext ctx) {
        ShowIndexStatement result = new ShowIndexStatement();
        if (null != ctx.fromSchema()) {
            SchemaNameContext schemaNameContext = ctx.fromSchema().schemaName();
            // TODO visitSchemaName
            result.setSchema(new SchemaSegment(schemaNameContext.getStart().getStartIndex(), schemaNameContext.getStop().getStopIndex(), (IdentifierValue) visit(schemaNameContext)));
        }
        if (null != ctx.fromTable()) {
            result.setTable(((FromTableSegment) visitFromTable(ctx.fromTable())).getTable());
        }
        return result;
    }

    @Override
    public ASTNode visitShowCreateTable(final ShowCreateTableContext ctx) {
        ShowCreateTableStatement result = new ShowCreateTableStatement();
        result.setTable((SimpleTableSegment) visit(ctx.tableName()));
        return result;
    }

    @Override
    public ASTNode visitFromTable(final FromTableContext ctx) {
        FromTableSegment result = new FromTableSegment();
        result.setTable((SimpleTableSegment) visit(ctx.tableName()));
        return result;
    }

    @Override
    public ASTNode visitShowOther(final ShowOtherContext ctx) {
        return new ShowOtherStatement();
    }

    @Override
    public ASTNode visitSetVariable(final SetVariableContext ctx) {
        SetStatement result = new SetStatement();
        if (null != ctx.variable()) {
            result.setVariable((VariableSegment) visit(ctx.variable()));
        }
        return result;
    }

    @Override
    public ASTNode visitVariable(final VariableContext ctx) {
        return new VariableSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), ctx.getText());
    }

    @Override
    public ASTNode visitFromSchema(final FromSchemaContext ctx) {
        return new FromSchemaSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex());
    }

    @Override
    public ASTNode visitShowLike(final ShowLikeContext ctx) {
        StringLiteralValue literalValue = (StringLiteralValue) visit(ctx.stringLiterals());
        return new ShowLikeSegment(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex(), literalValue.getValue());
    }
}
