/**
 * Mule Taleo Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 */

package org.mule.modules.taleo.automation.testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;

import javax.xml.datatype.DatatypeFactory;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.taleo.model.RequisitionBean;

public class CreateRequisitionTestCases extends TaleoTestParent {
	
	@After
	public void tearDown() {
		
		MessageProcessor flow = lookupFlowConstruct("delete-requisition");
		
		try {		
			
			if (testObjects.containsKey("requisitionId")) {
				
				MuleEvent response = flow.process(getTestEvent(testObjects));
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				fail();
		}
		
	}

    @Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testCreateRequisition() {
    	
    	testObjects =  new HashMap<String,Object>();
		MessageProcessor flow = lookupFlowConstruct("create-requisition");
    	
		try {
			
			RequisitionBean requisitionBean = (RequisitionBean) context.getBean("createRequisitionRequisitionBean");
	    	requisitionBean.setOpenedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new DateTime().toGregorianCalendar()));
	    	testObjects.put("requisitionRef", requisitionBean);

			MuleEvent response = flow.process(getTestEvent(testObjects));
			Long requisitionId = (Long) response.getMessage().getPayload();
			
			assertNotNull(requisitionId);
			
			testObjects.put("requisitionId", requisitionId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
     
	}
    
}