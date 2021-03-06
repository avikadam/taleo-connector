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
import org.mule.modules.taleo.model.ArrayOfDepartmentBean;
import org.mule.modules.taleo.model.DepartmentArr;
import org.mule.modules.taleo.model.DepartmentBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GetDepartmentsTestCases extends TaleoTestParent {

    private String departmentName = UUID.randomUUID().toString();
    private Long departmentId;

    @Before
    public void setUp() {

        testObjects = new HashMap<String, Object>();

        DepartmentBean departmentBean = (DepartmentBean) context.getBean("getDepartmentsDepartmentBean");
        departmentBean.setDepartmentName(departmentName);
        testObjects.put("departmentName", departmentName);

        testObjects.put("departmentRef", departmentBean);

        MessageProcessor flow = lookupFlowConstruct("create-department");

        try {

            MuleEvent response = flow.process(getTestEvent(testObjects));
            departmentId = (Long) response.getMessage().getPayload();
            testObjects.put("departmentId", departmentId);


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
    public void testGetDepartments() {

        MessageProcessor getDepartmentsFlow = lookupFlowConstruct("get-departments");

        MuleEvent response;

        boolean found = false;

        try {

            response = getDepartmentsFlow.process(getTestEvent(null));
            DepartmentArr departmentArr = (DepartmentArr) response.getMessage().getPayload();

            List<DepartmentBean> deparments = ((ArrayOfDepartmentBean) departmentArr.getArray()).getItem();
            Iterator<DepartmentBean> iterator = deparments.iterator();

            while (iterator.hasNext()) {

                DepartmentBean departmentBean = iterator.next();

                if ((departmentBean.getDepartmentId() == departmentId) &&
                        (departmentBean.getDepartmentName().equals(departmentName))) {
                    found = true;
                }

            }

            assertTrue(found);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }

}