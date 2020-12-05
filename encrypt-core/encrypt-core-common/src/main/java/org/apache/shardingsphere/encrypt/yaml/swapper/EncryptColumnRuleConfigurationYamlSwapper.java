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

package org.apache.shardingsphere.encrypt.yaml.swapper;

import org.apache.shardingsphere.encrypt.api.EncryptColumnRuleConfiguration;
import org.apache.shardingsphere.encrypt.yaml.config.YamlEncryptColumnRuleConfiguration;
import org.apache.shardingsphere.underlying.common.yaml.swapper.YamlSwapper;

/**
 * Encrypt column configuration YAML swapper.
 */
public final class EncryptColumnRuleConfigurationYamlSwapper implements YamlSwapper<YamlEncryptColumnRuleConfiguration, EncryptColumnRuleConfiguration> {

    @Override
    public YamlEncryptColumnRuleConfiguration swap(final EncryptColumnRuleConfiguration data) {
        YamlEncryptColumnRuleConfiguration result = new YamlEncryptColumnRuleConfiguration();
        result.setPlainColumn(data.getPlainColumn());
        result.setCipherColumn(data.getCipherColumn());
        result.setAssistedQueryColumn(data.getAssistedQueryColumn());
        result.setEncryptor(data.getEncryptor());
        return result;
    }

    @Override
    public EncryptColumnRuleConfiguration swap(final YamlEncryptColumnRuleConfiguration yamlConfiguration) {
        return new EncryptColumnRuleConfiguration(
                yamlConfiguration.getPlainColumn(), yamlConfiguration.getCipherColumn(), yamlConfiguration.getAssistedQueryColumn(), yamlConfiguration.getEncryptor());
    }
}
