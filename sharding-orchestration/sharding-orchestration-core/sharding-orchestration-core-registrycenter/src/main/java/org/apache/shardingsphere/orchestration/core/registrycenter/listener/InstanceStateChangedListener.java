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

package org.apache.shardingsphere.orchestration.core.registrycenter.listener;

import org.apache.shardingsphere.orchestration.center.RegistryCenterRepository;
import org.apache.shardingsphere.orchestration.center.listener.DataChangedEvent;
import org.apache.shardingsphere.orchestration.core.common.listener.PostShardingRegistryCenterEventListener;
import org.apache.shardingsphere.orchestration.core.registrycenter.RegistryCenterNode;
import org.apache.shardingsphere.orchestration.core.registrycenter.RegistryCenterNodeStatus;
import org.apache.shardingsphere.orchestration.core.registrycenter.event.CircuitStateChangedEvent;
import org.apache.shardingsphere.orchestration.core.registrycenter.instance.OrchestrationInstance;

/**
 * Instance state changed listener.
 */
public final class InstanceStateChangedListener extends PostShardingRegistryCenterEventListener {

    public InstanceStateChangedListener(final String name, final RegistryCenterRepository registryCenterRepository) {
        super(registryCenterRepository, new RegistryCenterNode(name).getInstancesNodeFullPath(OrchestrationInstance.getInstance().getInstanceId()));
    }

    @Override
    protected CircuitStateChangedEvent createShardingOrchestrationEvent(final DataChangedEvent event) {
        return new CircuitStateChangedEvent(RegistryCenterNodeStatus.DISABLED.toString().equalsIgnoreCase(event.getValue()));
    }
}