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

package org.apache.shardingsphere.shardingproxy.backend.text.sctl.explain;

import com.google.common.base.Preconditions;
import org.apache.shardingsphere.shardingproxy.backend.text.sctl.ShardingCTLParser;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sharding CTL explain parser.
 */
public final class ShardingCTLExplainParser implements ShardingCTLParser<ShardingCTLExplainStatement> {

    private final String regex = "sctl:explain\\s+([\\s\\S]*)";

    private final Matcher matcher;

    ShardingCTLExplainParser(final String sql) {
        matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(sql);
    }

    @Override
    public Optional<ShardingCTLExplainStatement> doParse() {
        if (matcher.find()) {
            String value = matcher.group(1);
            Preconditions.checkNotNull(value, "sctl explain sql cannot be null.");
            return Optional.of(new ShardingCTLExplainStatement(value));
        }
        return Optional.empty();
    }
}
