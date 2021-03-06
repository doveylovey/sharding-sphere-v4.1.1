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

package org.apache.shardingsphere.route.time;

import org.apache.shardingsphere.route.time.impl.TimeServiceFactory;
import org.apache.shardingsphere.sharding.route.spi.TimeService;

import java.util.Date;

/**
 * Delegate database time service.
 */
public final class DatabaseTimeServiceDelegate implements TimeService {

    private static final TimeService TIME_SERVICE = TimeServiceFactory.createTimeService();

    @Override
    public Date getTime() {
        return TIME_SERVICE.getTime();
    }
}
