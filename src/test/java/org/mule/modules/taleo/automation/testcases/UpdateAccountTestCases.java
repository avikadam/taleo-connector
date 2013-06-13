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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.taleo.model.AccountBean;

public class UpdateAccountTestCases extends TaleoTestParent {
	
	@Before
	public void setUp() {
		
		testObjects = new HashMap<String,Object>();
		testObjects.put("account", (AccountBean) context.getBean("updateAccountAccountBean"));
    	
		MessageProcessor flow = lookupFlowConstruct("create-account");
    	
		try {

			MuleEvent response = flow.process(getTestEvent(testObjects));
			testObjects.put("accountId", (Long) response.getMessage().getPayload());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
	}	
	
	
	@After
	public void tearDown() {
		
		MessageProcessor flow = lookupFlowConstruct("delete-account");
		
		try {		
			
			if (testObjects.containsKey("accountId")) {
				
				flow.process(getTestEvent(testObjects));
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				fail();
		}
		
	}

    @Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testUpdateAccount() {
    	
    	String UPDATED_PHONE_NUMBER = "111-111-1111";
    	
		MessageProcessor getAccountByIdFLow = lookupFlowConstruct("get-account-by-id");
		MessageProcessor updateAccountFlow = lookupFlowConstruct("update-account");
    	
		MuleEvent updateAccountResponse, getAccountByResponse;
		AccountBean accountBean, updatedAccountBean;
		
		try {

			getAccountByResponse = getAccountByIdFLow.process(getTestEvent(testObjects));
			accountBean = (AccountBean) getAccountByResponse.getMessage().getPayload();
			
			accountBean.setPhone(UPDATED_PHONE_NUMBER);
			testObjects.put("account", accountBean);
			
			updateAccountResponse = updateAccountFlow.process(getTestEvent(testObjects));
			updateAccountResponse.getMessage().getPayload();
			
			getAccountByResponse = getAccountByIdFLow.process(getTestEvent(testObjects));
			updatedAccountBean = (AccountBean) getAccountByResponse.getMessage().getPayload();
			
			assertEquals(UPDATED_PHONE_NUMBER, updatedAccountBean.getPhone());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
     
	}
    
}