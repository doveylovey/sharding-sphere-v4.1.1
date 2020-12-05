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

package org.apache.shardingsphere.sharding.route.engine.condition;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Column.
 */
@RequiredArgsConstructor
@Getter
@ToString
public final class Column {

    private final String name;

    private final String tableName;

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Column) {
            Column column = (Column) obj;
            return Objects.equal(name.toUpperCase(), column.getName().toUpperCase()) && Objects.equal(tableName.toUpperCase(), column.getTableName().toUpperCase());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name.toUpperCase(), tableName.toUpperCase());
    }
}
