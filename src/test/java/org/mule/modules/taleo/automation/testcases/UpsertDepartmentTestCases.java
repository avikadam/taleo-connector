/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under
 * the terms of the CPAL v1.0 license, a copy of which has been included with this
 * distribution in the LICENSE.md file.
 */

package org.mule.modules.taleo.automation.testcases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.taleo.model.DepartmentBean;
import org.mule.modules.taleo.model.Map;
import org.mule.modules.taleo.model.MapItem;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UpsertDepartmentTestCases extends TaleoTestParent {

    @Before
    public void setUp() {

        testObjects = new HashMap<String, Object>();

        DepartmentBean departmentBean = (DepartmentBean) context.getBean("upsertDepartmentDepartmentBean");
        departmentBean.setDepartmentName(UUID.randomUUID().toString());

        testObjects.put("departmentRef", departmentBean);

        MessageProcessor flow = lookupFlowConstruct("create-department");

        try {

            MuleEvent response = flow.process(getTestEvent(testObjects));
            testObjects.put("departmentId", (Long) response.getMessage().getPayload());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }


    @After
    public void tearDown() {

        MessageProcessor flow = lookupFlowConstruct("delete-department");

        try {

            if (testObjects.containsKey("departmentId")) {

                flow.process(getTestEvent(testObjects));

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }

    @Category({RegressionTests.class})
    @Test
    public void testUpsertDepartment() {

        Map additionalProperties = (Map) context.getBean("upsertDepartmentAdditionalProperties");

        MessageProcessor getDepartmentByIdFLow = lookupFlowConstruct("get-department-by-id");
        MessageProcessor upsertDepartmentFlow = lookupFlowConstruct("upsert-department");

        MuleEvent getDepartmentByResponse;
        DepartmentBean departmentBean, updatedDepartmentBean;

        try {

            getDepartmentByResponse = getDepartmentByIdFLow.process(getTestEvent(testObjects));
            departmentBean = (DepartmentBean) getDepartmentByResponse.getMessage().getPayload();

            departmentBean.setAdditionalProperties(additionalProperties);

            testObjects.put("departmentRef", departmentBean);

            upsertDepartmentFlow.process(getTestEvent(testObjects));

            getDepartmentByResponse = getDepartmentByIdFLow.process(getTestEvent(testObjects));
            updatedDepartmentBean = (DepartmentBean) getDepartmentByResponse.getMessage().getPayload();

            assertEquals((Long) updatedDepartmentBean.getDepartmentId(), (Long) testObjects.get("departmentId"));

            MapItem expectedMapItem = ((List<MapItem>) additionalProperties.getItem()).get(0);
            MapItem actualMapItem = ((List<MapItem>) updatedDepartmentBean.getAdditionalProperties().getItem()).get(0);

            assertEquals(expectedMapItem.getKey(), actualMapItem.getKey());
            assertEquals(expectedMapItem.getPropertyValue(), actualMapItem.getPropertyValue());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }

}