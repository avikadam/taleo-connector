/**
 * Mule Taleo Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 */

package org.mule.modules.taleo.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.UUID;

import javax.xml.datatype.DatatypeFactory;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.taleo.model.AccountBean;
import org.mule.modules.taleo.model.AttachmentBean;
import org.mule.modules.taleo.model.BackgroundCheckBean;
import org.mule.modules.taleo.model.CandidateBean;
import org.mule.modules.taleo.model.InterviewBean;


public class GetInterviewByIdTestCases extends TaleoTestParent {
	
	@Before
	public void setUp() {
    	
    	testObjects =  new HashMap<String,Object>();
    	CandidateBean candidateBean = (CandidateBean) context.getBean("getInterviewByIdCandidateBean");
    	candidateBean.setEmail(String.format("%s@email.com", UUID.randomUUID().toString().substring(0, 8)));
    	
    	testObjects.put("candidateRef", candidateBean);

		MessageProcessor flow = lookupFlowConstruct("create-candidate");
    	
		try {

			MuleEvent response = flow.process(getTestEvent(testObjects));
			Long candidateId = (Long) response.getMessage().getPayload();
			testObjects.put("candidateId", candidateId);
			
			InterviewBean interviewBean = (InterviewBean) context.getBean("getInterviewByIdInterviewBean");
			interviewBean.setCandidateId(candidateId);
			interviewBean.setStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new DateTime().toGregorianCalendar()));
			testObjects.put("interviewRef", interviewBean);
			
			MessageProcessor createInterviewFlow = lookupFlowConstruct("create-interview");
			MuleEvent createInterviewResponse = createInterviewFlow.process(getTestEvent(testObjects));
			Long interviewId = (Long) createInterviewResponse.getMessage().getPayload();
			testObjects.put("interviewId", interviewId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
     
	}
	
	@After
	public void tearDown() {
		
		MessageProcessor deleteCandidateFlow = lookupFlowConstruct("delete-candidate");
		MessageProcessor deleteInterviewFlow = lookupFlowConstruct("delete-interview");
		
		try {		

			if (testObjects.containsKey("interviewId")) {
				deleteInterviewFlow.process(getTestEvent(testObjects));	
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
	
    @Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testGetInterviewByCandidate() {
    	
		MessageProcessor flow = lookupFlowConstruct("get-interview-by-id");
    	
		try {

			MuleEvent response = flow.process(getTestEvent(testObjects));
			InterviewBean interviewBean = (InterviewBean) response.getMessage().getPayload();
			
			assertEquals((Long) interviewBean.getCandidateId(), (Long) testObjects.get("candidateId"));
			assertEquals((Long) interviewBean.getId(), (Long) testObjects.get("interviewId"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
     
	}
    
}