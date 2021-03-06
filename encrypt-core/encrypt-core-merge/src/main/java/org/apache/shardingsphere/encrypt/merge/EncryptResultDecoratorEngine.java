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

package org.apache.shardingsphere.encrypt.merge;

import org.apache.shardingsphere.encrypt.merge.dal.EncryptDALResultDecorator;
import org.apache.shardingsphere.encrypt.merge.dql.EncryptDQLResultDecorator;
import org.apache.shardingsphere.encrypt.merge.dql.EncryptorMetaData;
import org.apache.shardingsphere.encrypt.rule.EncryptRule;
import org.apache.shardingsphere.spi.database.type.DatabaseType;
import org.apache.shardingsphere.sql.parser.binder.metadata.schema.SchemaMetaData;
import org.apache.shardingsphere.sql.parser.binder.statement.SQLStatementContext;
import org.apache.shardingsphere.sql.parser.binder.statement.dml.SelectStatementContext;
import org.apache.shardingsphere.sql.parser.sql.statement.dal.DALStatement;
import org.apache.shardingsphere.underlying.common.config.properties.ConfigurationProperties;
import org.apache.shardingsphere.underlying.common.config.properties.ConfigurationPropertyKey;
import org.apache.shardingsphere.underlying.merge.engine.decorator.ResultDecorator;
import org.apache.shardingsphere.underlying.merge.engine.decorator.ResultDecoratorEngine;
import org.apache.shardingsphere.underlying.merge.engine.decorator.impl.TransparentResultDecorator;

/**
 * Result decorator engine for encrypt.
 */
public final class EncryptResultDecoratorEngine implements ResultDecoratorEngine<EncryptRule> {

    @Override
    public ResultDecorator newInstance(final DatabaseType databaseType, final SchemaMetaData schemaMetaData,
                                       final EncryptRule encryptRule, final ConfigurationProperties properties, final SQLStatementContext sqlStatementContext) {
        if (sqlStatementContext instanceof SelectStatementContext) {
            return new EncryptDQLResultDecorator(
                    new EncryptorMetaData(schemaMetaData, encryptRule, (SelectStatementContext) sqlStatementContext), properties.<Boolean>getValue(ConfigurationPropertyKey.QUERY_WITH_CIPHER_COLUMN));
        }
        if (sqlStatementContext.getSqlStatement() instanceof DALStatement) {
            return new EncryptDALResultDecorator();
        }
        return new TransparentResultDecorator();
    }

    @Override
    public int getOrder() {
        return 20;
    }

    @Override
    public Class<EncryptRule> getType() {
        return EncryptRule.class;
    }
}
