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

package org.apache.shardingsphere.sql.parser.sqlserver.visitor.impl;

import org.apache.shardingsphere.sql.parser.api.ASTNode;
import org.apache.shardingsphere.sql.parser.api.visitor.statement.DCLVisitor;
import org.apache.shardingsphere.sql.parser.autogen.SQLServerStatementParser.*;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.table.SimpleTableSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.dcl.*;
import org.apache.shardingsphere.sql.parser.sqlserver.visitor.SQLServerVisitor;

import java.util.Collection;
import java.util.Collections;

/**
 * DCL visitor for SQLServer.
 */
public final class SQLServerDCLVisitor extends SQLServerVisitor implements DCLVisitor {

    @Override
    public ASTNode visitGrant(final GrantContext ctx) {
        GrantStatement result = new GrantStatement();
        if (null != ctx.classPrivilegesClause()) {
            for (SimpleTableSegment each : getTableFromPrivilegeClause(ctx.classPrivilegesClause())) {
                result.getTables().add(each);
            }
        }
        if (null != ctx.classTypePrivilegesClause()) {
            for (SimpleTableSegment each : getTableFromPrivilegeClause(ctx.classTypePrivilegesClause())) {
                result.getTables().add(each);
            }
        }
        return result;
    }

    @Override
    public ASTNode visitRevoke(final RevokeContext ctx) {
        RevokeStatement result = new RevokeStatement();
        if (null != ctx.classPrivilegesClause()) {
            for (SimpleTableSegment each : getTableFromPrivilegeClause(ctx.classPrivilegesClause())) {
                result.getTables().add(each);
            }
        }
        if (null != ctx.classTypePrivilegesClause()) {
            for (SimpleTableSegment each : getTableFromPrivilegeClause(ctx.classTypePrivilegesClause())) {
                result.getTables().add(each);
            }
        }
        return result;
    }

    private Collection<SimpleTableSegment> getTableFromPrivilegeClause(final ClassPrivilegesClauseContext ctx) {
        return null == ctx.onClassClause().tableName() ? Collections.emptyList() : Collections.singletonList((SimpleTableSegment) visit(ctx.onClassClause().tableName()));
    }

    private Collection<SimpleTableSegment> getTableFromPrivilegeClause(final ClassTypePrivilegesClauseContext ctx) {
        return null == ctx.onClassTypeClause().tableName() ? Collections.emptyList() : Collections.singletonList((SimpleTableSegment) visit(ctx.onClassTypeClause().tableName()));
    }

    @Override
    public ASTNode visitCreateUser(final CreateUserContext ctx) {
        return new CreateUserStatement();
    }

    @Override
    public ASTNode visitAlterUser(final AlterUserContext ctx) {
        return new AlterUserStatement();
    }

    @Override
    public ASTNode visitDeny(final DenyContext ctx) {
        DenyUserStatement result = new DenyUserStatement();
        if (null != ctx.classPrivilegesClause()) {
            for (SimpleTableSegment each : getTableFromPrivilegeClause(ctx.classPrivilegesClause())) {
                result.setTable(each);
            }
        }
        if (null != ctx.classTypePrivilegesClause()) {
            for (SimpleTableSegment each : getTableFromPrivilegeClause(ctx.classTypePrivilegesClause())) {
                result.setTable(each);
            }
        }
        return result;
    }

    @Override
    public ASTNode visitDropUser(final DropUserContext ctx) {
        return new DropUserStatement();
    }

    @Override
    public ASTNode visitCreateRole(final CreateRoleContext ctx) {
        return new CreateRoleStatement();
    }

    @Override
    public ASTNode visitAlterRole(final AlterRoleContext ctx) {
        return new AlterRoleStatement();
    }

    @Override
    public ASTNode visitDropRole(final DropRoleContext ctx) {
        return new DropRoleStatement();
    }

    @Override
    public ASTNode visitCreateLogin(final CreateLoginContext ctx) {
        return new CreateLoginStatement();
    }

    @Override
    public ASTNode visitAlterLogin(final AlterLoginContext ctx) {
        return new AlterLoginStatement();
    }

    @Override
    public ASTNode visitDropLogin(final DropLoginContext ctx) {
        return new DropLoginStatement();
    }
}
