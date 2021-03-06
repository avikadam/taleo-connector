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
import org.mule.modules.taleo.model.BackgroundCheckBean;
import org.mule.modules.taleo.model.CandidateBean;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class GetBackgroundCheckByIdTestCases extends TaleoTestParent {

    @Before
    public void setUp() {

        MessageProcessor flow;
        MuleEvent response;

        testObjects = new HashMap<String, Object>();
        CandidateBean candidateBean = (CandidateBean) context.getBean("getBackgroundCheckByIdCandidateBean");
        candidateBean.setEmail(String.format("%s@email.com", UUID.randomUUID().toString().substring(0, 8)));

        testObjects.put("candidateRef", candidateBean);

        try {

            flow = lookupFlowConstruct("create-candidate");
            response = flow.process(getTestEvent(testObjects));
            Long candidateId = (Long) response.getMessage().getPayload();
            testObjects.put("candidateId", candidateId);

            BackgroundCheckBean backgroundCheckBean = (BackgroundCheckBean) context.getBean("getBackgroundCheckByIdBackgroundCheckBean");
            backgroundCheckBean.setCandidateId(candidateId);
            testObjects.put("backgroundCheckRef", backgroundCheckBean);

            flow = lookupFlowConstruct("create-background-check");
            response = flow.process(getTestEvent(testObjects));
            Long backgroundCheckId = (Long) response.getMessage().getPayload();
            testObjects.put("backgroundCheckId", backgroundCheckId);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }

    @After
    public void tearDown() {

        MessageProcessor deleteCandidateFlow = lookupFlowConstruct("delete-candidate");
        MessageProcessor deleteBackgroundCheckFlow = lookupFlowConstruct("delete-background-check");

        try {

            if (testObjects.containsKey("backgroundCheckId")) {
                deleteBackgroundCheckFlow.process(getTestEvent(testObjects));
            }

            if (testObjects.containsKey("candidateId")) {
                deleteCandidateFlow.process(getTestEvent(testObjects));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }

    @Category({RegressionTests.class})
    @Test
    public void testGetBackgroundCheckById() {

        MessageProcessor flow = lookupFlowConstruct("get-background-check-by-id");

        try {

            MuleEvent response = flow.process(getTestEvent(testObjects));
            BackgroundCheckBean backgroundCheckBean = (BackgroundCheckBean) response.getMessage().getPayload();

            assertEquals((Long) backgroundCheckBean.getCandidateId(), (Long) testObjects.get("candidateId"));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }

}