/*
 * Copyright 2004-2005 The Apache Software Foundation or its licensors,
 *                     as applicable.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.test.api.nodetype;

import org.apache.jackrabbit.test.AbstractJCRTest;
import org.apache.jackrabbit.test.NotExecutableException;

import javax.jcr.nodetype.PropertyDef;
import javax.jcr.nodetype.NodeType;
import javax.jcr.Session;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.StringValue;
import javax.jcr.BinaryValue;
import java.text.ParseException;

/**
 * Test of <code>NodeType.canSetProperty(String propertyName, Value
 * value)</code> and <code>NodeType.canSetProperty(String propertyName, Value[]
 * values)</code> where property is of type Date.
 *
 * @test
 * @sources CanSetPropertyDateTest.java
 * @executeClass org.apache.jackrabbit.test.api.nodetype.CanSetPropertyDateTest
 * @keywords level1
 */
public class CanSetPropertyDateTest extends AbstractJCRTest {
    /**
     * The session we use for the tests
     */
    private Session session;

    /**
     * Sets up the fixture for the test cases.
     */
    protected void setUp() throws Exception {
        isReadOnly = true;
        super.setUp();

        session = helper.getReadOnlySession();
    }

    /**
     * Releases the session aquired in {@link #setUp()}.
     */
    protected void tearDown() throws Exception {
        if (session != null) {
            session.logout();
        }
        super.tearDown();
    }


    /**
     * Tests if NodeType.canSetProperty(String propertyName, Value value)
     * returns true if value and its type are convertible to DateValue.
     */
    public void testConversions()
            throws NotExecutableException, RepositoryException {

        PropertyDef propDef =
                NodeTypeUtil.locatePropertyDef(session, PropertyType.DATE, false, false, false, false);

        if (propDef == null) {
            throw new NotExecutableException("No date property def that meets the " +
                    "requirements of the test has been found");
        }

        NodeType nodeType = propDef.getDeclaringNodeType();


        Value anyStringValue = NodeTypeUtil.getValueOfType(PropertyType.STRING);
        assertFalse("canSetProperty(String propertyName, Value value) must return " +
                "false if the property is of type Date and value is a StringValue " +
                "not in date format",
                nodeType.canSetProperty(propDef.getName(), anyStringValue));

        StringValue dateStringValue =
                new StringValue(NodeTypeUtil.getValueOfType(PropertyType.DATE).getString());
        assertTrue("canSetProperty(String propertyName, Value value) must return " +
                "true if the property is of type Date and value is a StringValue " +
                "in date format",
                nodeType.canSetProperty(propDef.getName(), dateStringValue));

        Value anyBinaryValue = NodeTypeUtil.getValueOfType(PropertyType.BINARY);
        assertFalse("canSetProperty(String propertyName, Value value) must return " +
                "false if the property is of type Date and value is a UTF-8 " +
                "BinaryValue not in date format",
                nodeType.canSetProperty(propDef.getName(), anyBinaryValue));

        BinaryValue dateBinaryValue =
                new BinaryValue(NodeTypeUtil.getValueOfType(PropertyType.DATE).getString());
        assertTrue("canSetProperty(String propertyName, Value value) must return " +
                "true if the property is of type Date and value is a UTF-8 " +
                "BinaryValue in date format",
                nodeType.canSetProperty(propDef.getName(), dateBinaryValue));

        Value dateValue = NodeTypeUtil.getValueOfType(PropertyType.DATE);
        assertTrue("canSetProperty(String propertyName, Value value) must return " +
                "true if the property is of type Date and value is a DateValue",
                nodeType.canSetProperty(propDef.getName(), dateValue));

        Value doubleValue = NodeTypeUtil.getValueOfType(PropertyType.DOUBLE);
        assertTrue("canSetProperty(String propertyName, Value value) must return " +
                "true if the property is of type Date and value is a DoubleValue",
                nodeType.canSetProperty(propDef.getName(), doubleValue));

        Value longValue = NodeTypeUtil.getValueOfType(PropertyType.LONG);
        assertTrue("canSetProperty(String propertyName, Value value) must return " +
                "true if the property is of type Date and value is a LongValue",
                nodeType.canSetProperty(propDef.getName(), longValue));

        Value booleanValue = NodeTypeUtil.getValueOfType(PropertyType.BOOLEAN);
        assertFalse("canSetProperty(String propertyName, Value value) must return " +
                "false if the property is of type Date and value is a BooleanValue",
                nodeType.canSetProperty(propDef.getName(), booleanValue));

        Value nameValue = NodeTypeUtil.getValueOfType(PropertyType.NAME);
        assertFalse("canSetProperty(String propertyName, Value value) must return " +
                "false if the property is of type Date and value is a NameValue",
                nodeType.canSetProperty(propDef.getName(), nameValue));

        Value pathValue = NodeTypeUtil.getValueOfType(PropertyType.PATH);
        assertFalse("canSetProperty(String propertyName, Value value) must return " +
                "false if the property is of type Date and value is a PathValue",
                nodeType.canSetProperty(propDef.getName(), pathValue));
    }

    /**
     * Tests if NodeType.canSetProperty(String propertyName, Value[] values)
     * returns true if all values and its types are convertible to DateValue.
     */
    public void testConversionsMultiple()
            throws NotExecutableException, RepositoryException {

        PropertyDef propDef =
                NodeTypeUtil.locatePropertyDef(session, PropertyType.DATE, true, false, false, false);

        if (propDef == null) {
            throw new NotExecutableException("No multiple string property def that meets the " +
                    "requirements of the test has been found");
        }

        NodeType nodeType = propDef.getDeclaringNodeType();


        Value dateValue = NodeTypeUtil.getValueOfType(PropertyType.DATE);

        Value anyStringValue = NodeTypeUtil.getValueOfType(PropertyType.STRING);
        // note: for assertFalse, use first value of requested type to check
        // if not only first value is checked
        Value anyStringValues[] = {dateValue, anyStringValue};
        assertFalse("canSetProperty(String propertyName, Value[] values) must return " +
                "false if the property is of type Date and values are of type StringValue " +
                "not in date format",
                nodeType.canSetProperty(propDef.getName(), anyStringValues));

        StringValue dateStringValue =
                new StringValue(NodeTypeUtil.getValueOfType(PropertyType.DATE).getString());
        Value dateStringValues[] = {dateStringValue};
        assertTrue("canSetProperty(String propertyName, Value[] values) must return " +
                "true if the property is of type Date and values are of type StringValue " +
                "in date format",
                nodeType.canSetProperty(propDef.getName(), dateStringValues));

        Value dateValues[] = {dateValue};
        assertTrue("canSetProperty(String propertyName, Value[] values) must return " +
                "true if the property is of type Date and values are of type DateValue",
                nodeType.canSetProperty(propDef.getName(), dateValues));

        Value anyBinaryValue = NodeTypeUtil.getValueOfType(PropertyType.BINARY);
        Value anyBinaryValues[] = {dateValue, anyBinaryValue};
        assertFalse("canSetProperty(String propertyName, Value[] values) must return " +
                "false if the property is of type Date and values are of type BinaryValue" +
                "in UTF-8 but not in date format",
                nodeType.canSetProperty(propDef.getName(), anyBinaryValues));

        BinaryValue dateBinaryValue =
                new BinaryValue(NodeTypeUtil.getValueOfType(PropertyType.DATE).getString());
        Value dateBinaryValues[] = {dateBinaryValue};
        assertTrue("canSetProperty(String propertyName, Value[] values) must return " +
                "true if the property is of type Date and values are of type BinaryValue" +
                "in UTF-8 and in date format",
                nodeType.canSetProperty(propDef.getName(), dateBinaryValues));

        Value doubleValue = NodeTypeUtil.getValueOfType(PropertyType.DOUBLE);
        Value doubleValues[] = {doubleValue};
        assertTrue("canSetProperty(String propertyName, Value[] values) must return " +
                "true if the property is of type Date and values are of type DoubleValue",
                nodeType.canSetProperty(propDef.getName(), doubleValues));

        Value longValue = NodeTypeUtil.getValueOfType(PropertyType.LONG);
        Value longValues[] = {longValue};
        assertTrue("canSetProperty(String propertyName, Value[] values) must return " +
                "true if the property is of type Date and values are of type LongValue",
                nodeType.canSetProperty(propDef.getName(), longValues));

        Value booleanValue = NodeTypeUtil.getValueOfType(PropertyType.BOOLEAN);
        Value booleanValues[] = {dateValue, booleanValue};
        assertFalse("canSetProperty(String propertyName, Value[] values) must return " +
                "false if the property is of type Date and values are of type BooleanValue",
                nodeType.canSetProperty(propDef.getName(), booleanValues));

        Value nameValue = NodeTypeUtil.getValueOfType(PropertyType.NAME);
        Value nameValues[] = {dateValue, nameValue};
        assertFalse("canSetProperty(String propertyName, Value[] values) must return " +
                "false if the property is of type Date and values are of type NameValue",
                nodeType.canSetProperty(propDef.getName(), nameValues));

        Value pathValue = NodeTypeUtil.getValueOfType(PropertyType.PATH);
        Value pathValues[] = {dateValue, pathValue};
        assertFalse("canSetProperty(String propertyName, Value[] values) must return " +
                "false if the property is of type Date and values are of type PathValue",
                nodeType.canSetProperty(propDef.getName(), pathValues));
    }

    /**
     * Tests if canSetProperty(String propertyName, Value value) returns false
     * if value does not match the value constraints of the property def
     */
    public void testOutOfValueConstraint()
            throws NotExecutableException, ParseException, RepositoryException {

        PropertyDef propDef =
                NodeTypeUtil.locatePropertyDef(session, PropertyType.DATE, false, false, true, false);

        if (propDef == null) {
            throw new NotExecutableException("No date property def with " +
                    "testable value constraints has been found");
        }

        Value value = NodeTypeUtil.getValueOutOfContstraint(propDef);
        if (value == null) {
            // value should never be null since this is catched already in locatePropertyDef
            throw new NotExecutableException("No date property def with " +
                    "testable value constraints has been found");
        }

        NodeType nodeType = propDef.getDeclaringNodeType();

        assertFalse("canSetProperty(String propertyName, Value value) must " +
                "return false if value does not match the value constraints.",
                nodeType.canSetProperty(propDef.getName(), value));
    }

    /**
     * Tests if canSetProperty(String propertyName, Value[] values) returns
     * false if values do not match the value constraints of the property def
     */
    public void testOutOfValueConstraintMultiple()
            throws NotExecutableException, ParseException, RepositoryException {

        PropertyDef propDef =
                NodeTypeUtil.locatePropertyDef(session, PropertyType.DATE, true, false, true, false);

        if (propDef == null) {
            throw new NotExecutableException("No multiple date property def with " +
                    "testable value constraints has been found");
        }

        Value value = NodeTypeUtil.getValueOutOfContstraint(propDef);
        if (value == null) {
            // value should never be null since this is catched already in locatePropertyDef
            throw new NotExecutableException("No multiple date property def with " +
                    "testable value constraints has been found");
        }

        NodeType nodeType = propDef.getDeclaringNodeType();
        Value values[] = {value};

        assertFalse("canSetProperty(String propertyName, Value[] values) must " +
                "return false if values do not match the value constraints.",
                nodeType.canSetProperty(propDef.getName(), values));
    }
}