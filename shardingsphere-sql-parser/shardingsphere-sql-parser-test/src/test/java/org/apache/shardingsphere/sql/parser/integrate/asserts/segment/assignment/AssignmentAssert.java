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

package org.apache.shardingsphere.sql.parser.integrate.asserts.segment.assignment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.sql.parser.integrate.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.column.ColumnAssert;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.domain.segment.impl.assignment.ExpectedAssignment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.assignment.AssignmentSegment;

/**
 * Assignment assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssignmentAssert {

    /**
     * Assert actual assignment segment is correct with expected assignment.
     *
     * @param assertContext assert context
     * @param actual        actual assignment segment
     * @param expected      expected assignment
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final AssignmentSegment actual, final ExpectedAssignment expected) {
        ColumnAssert.assertIs(assertContext, actual.getColumn(), expected.getColumn());
        // TODO assert assign operator
        AssignmentValueAssert.assertIs(assertContext, actual.getValue(), expected.getAssignmentValue());
    }
}
