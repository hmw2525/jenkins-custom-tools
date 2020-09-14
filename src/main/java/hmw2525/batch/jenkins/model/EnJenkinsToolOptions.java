/* 
 * Copyright (c) 2013 SK planet. 
 * All right reserved. 
 * 
 * This software is the confidential and proprietary information of SK planet. 
 * You shall not disclose such Confidential Information and 
 * shall use it only in accordance with the terms of the license agreement 
 * you entered into with SK planet. 
 */
package hmw2525.batch.jenkins.model;

/** 
 * <p></p>
 * 
 * <ul>
 * <li>Created on : 2014. 9. 2.</li>
 * <li>Created by : incross_mwhong</li>
 * <li>Last Updated on : 2014. 9. 2.</li>
 * <li>Last Updated by : incross_mwhong</li>
 * </ul>
 */
public enum EnJenkinsToolOptions {
	JKSERVER_BASE	("-b",		
			new String [] {	EnJenkinsToolOptions.ENV_BATCH_PROD, EnJenkinsToolOptions.ENV_BATCH_QA, 
							EnJenkinsToolOptions.ENV_BATCH_DEV}),
	JKSERVER_TARGET	("-t",		
			new String [] {	EnJenkinsToolOptions.ENV_BATCH_PROD, EnJenkinsToolOptions.ENV_BATCH_QA, 
							EnJenkinsToolOptions.ENV_BATCH_DEV, EnJenkinsToolOptions.ENV_CI}),
	MULTI_PROCESS	("-multi",	null),
	UPDATE_CONTENT	("-uc",		null),
	UPDATE_TAG		("-ut",		null)
	;

	public static final String ENV_BATCH_PROD 	= "batch-prod";
	public static final String ENV_BATCH_QA 	= "batch-qa";
	public static final String ENV_BATCH_DEV 	= "batch-dev";
	public static final String ENV_CI	 		= "ci";
	
	//Option Name
	private String name = null;
	private String value = null;
	private String [] preservedValue =  null;
	
	private EnJenkinsToolOptions(String name, String [] preservedValue) {
		this.name = name;
		this.preservedValue = preservedValue;
	}

	/**
	 * @return the preservedValue
	 */
	public String[] getPreservedValue() {
		return preservedValue;
	}

	/**
	 * @param preservedValue the preservedValue to set
	 */
	public void setPreservedValue(String[] preservedValue) {
		this.preservedValue = preservedValue;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
