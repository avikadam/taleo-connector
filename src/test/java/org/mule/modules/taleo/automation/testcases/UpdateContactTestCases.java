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
import org.mule.modules.taleo.model.ContactBean;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UpdateContactTestCases extends TaleoTestParent {

    @Before
    public void setUp() {

        testObjects = new HashMap<String, Object>();
        testObjects.put("contactRef", (ContactBean) context.getBean("updateContactContactBean"));

        MessageProcessor flow = lookupFlowConstruct("create-contact");

        try {

            MuleEvent response = flow.process(getTestEvent(testObjects));
            testObjects.put("contactId", (Long) response.getMessage().getPayload());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }


    @After
    public void tearDown() {

        MessageProcessor flow = lookupFlowConstruct("delete-contact");

        try {

            if (testObjects.containsKey("contactId")) {

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
    public void testUpdateContact() {

        String UPDATED_CELLPHONE_NUMBER = "111-111-1111";

        MessageProcessor getContactByIdFLow = lookupFlowConstruct("get-contact-by-id");
        MessageProcessor updateContactFlow = lookupFlowConstruct("update-contact");

        MuleEvent getContactByResponse;
        ContactBean contactBean, updatedContactBean;

        try {

            getContactByResponse = getContactByIdFLow.process(getTestEvent(testObjects));
            contactBean = (ContactBean) getContactByResponse.getMessage().getPayload();

            contactBean.setPhone(UPDATED_CELLPHONE_NUMBER);
            testObjects.put("contactRef", contactBean);

            updateContactFlow.process(getTestEvent(testObjects));

            getContactByResponse = getContactByIdFLow.process(getTestEvent(testObjects));
            updatedContactBean = (ContactBean) getContactByResponse.getMessage().getPayload();

            assertEquals(UPDATED_CELLPHONE_NUMBER, updatedContactBean.getPhone());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }

}