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

package org.apache.shardingsphere.sql.parser.postgresql;

import org.apache.shardingsphere.sql.parser.api.lexer.SQLLexer;
import org.apache.shardingsphere.sql.parser.api.parser.SQLParser;
import org.apache.shardingsphere.sql.parser.api.visitor.SQLVisitorFacade;
import org.apache.shardingsphere.sql.parser.postgresql.lexer.PostgreSQLLexer;
import org.apache.shardingsphere.sql.parser.postgresql.parser.PostgreSQLParser;
import org.apache.shardingsphere.sql.parser.postgresql.visitor.PostgreSQLVisitorFacade;
import org.apache.shardingsphere.sql.parser.spi.SQLParserConfiguration;

/**
 * SQL parser configuration for PostgreSQL.
 */
public final class PostgreSQLParserConfiguration implements SQLParserConfiguration {

    @Override
    public String getDatabaseTypeName() {
        return "PostgreSQL";
    }

    @Override
    public Class<? extends SQLLexer> getLexerClass() {
        return PostgreSQLLexer.class;
    }

    @Override
    public Class<? extends SQLParser> getParserClass() {
        return PostgreSQLParser.class;
    }

    @Override
    public Class<? extends SQLVisitorFacade> getVisitorFacadeClass() {
        return PostgreSQLVisitorFacade.class;
    }
}
