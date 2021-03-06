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
import org.mule.modules.taleo.model.CandidateBean;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UpdateCandidateTestCases extends TaleoTestParent {

    @Before
    public void setUp() {

        testObjects = new HashMap<String, Object>();
        testObjects.put("candidateRef", (CandidateBean) context.getBean("updateCandidateCandidateBean"));

        MessageProcessor flow = lookupFlowConstruct("create-candidate");

        try {

            MuleEvent response = flow.process(getTestEvent(testObjects));
            testObjects.put("candidateId", (Long) response.getMessage().getPayload());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }


    @After
    public void tearDown() {

        MessageProcessor flow = lookupFlowConstruct("delete-candidate");

        try {

            if (testObjects.containsKey("candidateId")) {

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
    public void testUpdateCandidate() {

        String UPDATED_CELLPHONE_NUMBER = "111-111-1111";

        MessageProcessor getCandidateByIdFLow = lookupFlowConstruct("get-candidate-by-id");
        MessageProcessor updateCandidateFlow = lookupFlowConstruct("update-candidate");

        MuleEvent getCandidateByResponse;
        CandidateBean candidateBean, updatedCandidateBean;

        try {

            getCandidateByResponse = getCandidateByIdFLow.process(getTestEvent(testObjects));
            candidateBean = (CandidateBean) getCandidateByResponse.getMessage().getPayload();

            candidateBean.setPhone(UPDATED_CELLPHONE_NUMBER);
            testObjects.put("candidateRef", candidateBean);

            updateCandidateFlow.process(getTestEvent(testObjects));

            getCandidateByResponse = getCandidateByIdFLow.process(getTestEvent(testObjects));
            updatedCandidateBean = (CandidateBean) getCandidateByResponse.getMessage().getPayload();

            assertEquals(UPDATED_CELLPHONE_NUMBER, updatedCandidateBean.getPhone());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

    }

}