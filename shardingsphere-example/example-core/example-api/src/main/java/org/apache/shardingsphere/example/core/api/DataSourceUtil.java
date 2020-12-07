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

package org.apache.shardingsphere.example.core.api;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public final class DataSourceUtil {
    /**
     * 默认数据库配置
     */
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 3306;
    public static final String DEFAULT_USERNAME = "root";
    public static final String DEFAULT_PASSWORD = "root";

    /**
     * master 数据库配置
     */
    public static final String MASTER_HOST = "localhost";
    public static final int MASTER_PORT = 3306;
    public static final String MASTER_USERNAME = "root";
    public static final String MASTER_PASSWORD = "root";

    /**
     * slave1 数据库配置
     */
    public static final String SLAVE1_HOST = "localhost";
    public static final int SLAVE1_PORT = 3306;
    public static final String SLAVE1_USERNAME = "root";
    public static final String SLAVE1_PASSWORD = "root";

    /**
     * slave2 数据库配置
     */
    public static final String SLAVE2_HOST = "localhost";
    public static final int SLAVE2_PORT = 3306;
    public static final String SLAVE2_USERNAME = "root";
    public static final String SLAVE2_PASSWORD = "root";

    /*public static DataSource createDataSource(final String dbName) {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        result.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8", DEFAULT_HOST, DEFAULT_PORT, dbName));
        result.setUsername(DEFAULT_USERNAME);
        result.setPassword(DEFAULT_PASSWORD);
        return result;
    }*/

    /**
     * 根据指定参数获取数据源
     *
     * @param host     主机地址
     * @param port     端口号
     * @param username 连接数据库的用户名
     * @param password 连接数据库的密码
     * @param dbName   数据库名称
     * @return
     */
    public static DataSource createDataSource(final String host, final int port, final String username, final String password, final String dbName) {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        result.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8", host, port, dbName));
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }
}
