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

package org.apache.shardingsphere.shardingscaling.core.metadata;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.sql.parser.binder.metadata.table.TableMetaData;
import org.apache.shardingsphere.sql.parser.binder.metadata.table.TableMetaDataLoader;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Meta data manager.
 */
@RequiredArgsConstructor
public final class MetaDataManager {

    private final DataSource dataSource;

    private final Map<String, TableMetaData> tableMetaDataMap = new HashMap<>();

    /**
     * Get table meta data by table name.
     *
     * @param tableName table name
     * @return table meta data
     */
    public TableMetaData getTableMetaData(final String tableName) {
        if (!tableMetaDataMap.containsKey(tableName)) {
            try {
                tableMetaDataMap.put(tableName, TableMetaDataLoader.load(dataSource, tableName, ""));
            } catch (SQLException e) {
                throw new RuntimeException(String.format("Load metaData for table %s failed", tableName), e);
            }
        }
        return tableMetaDataMap.get(tableName);
    }
}
