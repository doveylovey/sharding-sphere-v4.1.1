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

package org.apache.shardingsphere.shardingproxy.frontend.postgresql.command;

import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;
import org.apache.shardingsphere.database.protocol.postgresql.packet.generic.PostgreSQLReadyForQueryPacket;
import org.apache.shardingsphere.shardingproxy.frontend.api.QueryCommandExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostgreSQLCommandExecuteEngineTest {

    @Mock
    private ChannelHandlerContext channelHandlerContext;

    @Mock
    private QueryCommandExecutor queryCommandExecutor;

    @Test
    @SneakyThrows
    public void assertWriteQueryDataWithError() {
        PostgreSQLCommandExecuteEngine postgreSQLCommandExecuteEngine = new PostgreSQLCommandExecuteEngine();
        when(queryCommandExecutor.isQuery()).thenReturn(false);
        when(queryCommandExecutor.isErrorResponse()).thenReturn(true);
        postgreSQLCommandExecuteEngine.writeQueryData(channelHandlerContext, null, queryCommandExecutor, 0);
        verify(channelHandlerContext, times(1)).write(isA(PostgreSQLReadyForQueryPacket.class));
    }
}
