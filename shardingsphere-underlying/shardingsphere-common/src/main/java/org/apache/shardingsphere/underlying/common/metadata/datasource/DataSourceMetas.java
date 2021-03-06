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

package org.apache.shardingsphere.underlying.common.metadata.datasource;

import org.apache.shardingsphere.spi.database.metadata.DataSourceMetaData;
import org.apache.shardingsphere.spi.database.metadata.MemorizedDataSourceMetaData;
import org.apache.shardingsphere.spi.database.type.DatabaseType;
import org.apache.shardingsphere.underlying.common.config.DatabaseAccessConfiguration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Data source metas.
 */
public final class DataSourceMetas {

    private final Map<String, DataSourceMetaData> dataSourceMetaDataMap;

    public DataSourceMetas(final DatabaseType databaseType, final Map<String, DatabaseAccessConfiguration> databaseAccessConfigurationMap) {
        dataSourceMetaDataMap = getDataSourceMetaDataMap(databaseType, databaseAccessConfigurationMap);
    }

    private Map<String, DataSourceMetaData> getDataSourceMetaDataMap(final DatabaseType databaseType, final Map<String, DatabaseAccessConfiguration> databaseAccessConfigurationMap) {
        return databaseAccessConfigurationMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> databaseType.getDataSourceMetaData(entry.getValue().getUrl(), entry.getValue().getUsername())));
    }

    /**
     * Get all instance data source names.
     *
     * @return instance data source names
     */
    public Collection<String> getAllInstanceDataSourceNames() {
        Collection<String> result = new LinkedList<>();
        for (Entry<String, DataSourceMetaData> entry : dataSourceMetaDataMap.entrySet()) {
            if (!isExisted(entry.getKey(), result)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    private boolean isExisted(final String dataSourceName, final Collection<String> existedDataSourceNames) {
        return existedDataSourceNames.stream().anyMatch(each -> isInSameDatabaseInstance(dataSourceMetaDataMap.get(dataSourceName), dataSourceMetaDataMap.get(each)));
    }

    private boolean isInSameDatabaseInstance(final DataSourceMetaData sample, final DataSourceMetaData target) {
        return sample instanceof MemorizedDataSourceMetaData
                ? Objects.equals(target.getSchema(), sample.getSchema()) : target.getHostName().equals(sample.getHostName()) && target.getPort() == sample.getPort();
    }

    /**
     * Get data source meta data.
     *
     * @param dataSourceName data source name
     * @return data source meta data
     */
    public DataSourceMetaData getDataSourceMetaData(final String dataSourceName) {
        return dataSourceMetaDataMap.get(dataSourceName);
    }
}
