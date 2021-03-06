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

package org.apache.shardingsphere.orchestration.core.registrycenter;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class RegistryCenterNodeTest {

    private final RegistryCenterNode stateNode = new RegistryCenterNode("test");

    @Test
    public void assertGetInstancesNodeFullPath() {
        assertThat(stateNode.getInstancesNodeFullPath("testId"), is("/test/state/instances/testId"));
    }

    @Test
    public void assertGetDataSourcesNodeFullRootPath() {
        assertThat(stateNode.getDataSourcesNodeFullRootPath(), is("/test/state/datasources"));
    }

    @Test
    public void assertGetDataSourcesNodeFullPath() {
        assertThat(stateNode.getDataSourcesNodeFullPath("sharding_db"), is("/test/state/datasources/sharding_db"));
    }

    @Test
    public void assertGetOrchestrationShardingSchema() {
        assertThat(stateNode.getOrchestrationShardingSchema("/test/state/datasources/master_slave_db.slave_ds_0").getSchemaName(), is("master_slave_db"));
    }
}
