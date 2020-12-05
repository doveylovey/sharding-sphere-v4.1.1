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
import org.apache.shardingsphere.sql.parser.api.visitor.statement.DCLVisitor;
import org.apache.shardingsphere.sql.parser.autogen.MySQLStatementParser.*;
import org.apache.shardingsphere.sql.parser.mysql.visitor.MySQLVisitor;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.dcl.*;

import java.util.Optional;

/**
 * DCL visitor for MySQL.
 */
public final class MySQLDCLVisitor extends MySQLVisitor implements DCLVisitor {

    @Override
    public ASTNode visitGrant(final GrantContext ctx) {
        GrantStatement result = new GrantStatement();
        Optional<SimpleTableSegment> tableSegmentOptional = null == ctx.privilegeClause() ? Optional.empty() : getTableFromPrivilegeClause(ctx.privilegeClause());
        tableSegmentOptional.ifPresent(tableSegment -> result.getTables().add(tableSegment));
        return result;
    }

    @Override
    public ASTNode visitRevoke(final RevokeContext ctx) {
        RevokeStatement result = new RevokeStatement();
        Optional<SimpleTableSegment> tableSegmentOptional = null == ctx.privilegeClause() ? Optional.empty() : getTableFromPrivilegeClause(ctx.privilegeClause());
        tableSegmentOptional.ifPresent(tableSegment -> result.getTables().add(tableSegment));
        return result;
    }

    private Optional<SimpleTableSegment> getTableFromPrivilegeClause(final PrivilegeClauseContext ctx) {
        if (null != ctx.onObjectClause()) {
            TableNameContext tableName = ctx.onObjectClause().privilegeLevel().tableName();
            if (null != tableName) {
                return Optional.of((SimpleTableSegment) visitTableName(tableName));
            }
        }
        return Optional.empty();
    }

    @Override
    public ASTNode visitCreateUser(final CreateUserContext ctx) {
        return new CreateUserStatement();
    }

    @Override
    public ASTNode visitDropUser(final DropUserContext ctx) {
        return new DropUserStatement();
    }

    @Override
    public ASTNode visitAlterUser(final AlterUserContext ctx) {
        return new AlterUserStatement();
    }

    @Override
    public ASTNode visitRenameUser(final RenameUserContext ctx) {
        return new RenameUserStatement();
    }

    @Override
    public ASTNode visitCreateRole(final CreateRoleContext ctx) {
        return new CreateRoleStatement();
    }

    @Override
    public ASTNode visitDropRole(final DropRoleContext ctx) {
        return new DropRoleStatement();
    }

    @Override
    public ASTNode visitSetDefaultRole(final SetDefaultRoleContext ctx) {
        return new SetDefaultRoleStatement();
    }

    @Override
    public ASTNode visitSetRole(final SetRoleContext ctx) {
        return new SetRoleStatement();
    }

    @Override
    public ASTNode visitSetPassword(final SetPasswordContext ctx) {
        return new SetPasswordStatement();
    }
}
