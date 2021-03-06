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

package org.apache.shardingsphere.shardingscaling.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.shardingsphere.shardingscaling.core.config.SyncConfiguration;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sharding scaling out job.
 */
@Getter
@Setter
@RequiredArgsConstructor
public final class ShardingScalingJob {

    private static final AtomicInteger ID_AUTO_INCREASE_GENERATOR = new AtomicInteger();

    private final int jobId = ID_AUTO_INCREASE_GENERATOR.incrementAndGet();

    private final transient List<SyncConfiguration> syncConfigurations = new LinkedList<>();

    private final String jobName;

    private String status = "RUNNING";
}
