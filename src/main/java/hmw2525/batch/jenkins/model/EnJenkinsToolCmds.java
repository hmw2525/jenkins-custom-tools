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

import static hmw2525.batch.jenkins.model.EnJenkinsToolOptions.*;

import java.util.Arrays;
import java.util.List;
/** 
 * <p></p>
 * <ul>
 * <li>Created on : 2014. 9. 2.</li>
 * <li>Created by : incross_mwhong</li>
 * <li>Last Updated on : 2014. 9. 2.</li>
 * <li>Last Updated by : incross_mwhong</li>
 * </ul>
 */
public enum EnJenkinsToolCmds {
	JOB_COPY	(EnJenkinsToolCmds.CMD_JOB_COPY, true,
					new EnJenkinsToolOptions[] {JKSERVER_BASE},  
					new EnJenkinsToolOptions[] {MULTI_PROCESS}),
	JOB_UPDATE	(EnJenkinsToolCmds.CMD_JOB_UPDATE, true,
					new EnJenkinsToolOptions[] {JKSERVER_TARGET, UPDATE_CONTENT, UPDATE_TAG},  
					new EnJenkinsToolOptions[] {MULTI_PROCESS}),
	JOB_DELETE	(EnJenkinsToolCmds.CMD_JOB_DELETE, true,
					new EnJenkinsToolOptions[] {JKSERVER_BASE},  
					new EnJenkinsToolOptions[] {MULTI_PROCESS}),
	JOB_SYNC	(EnJenkinsToolCmds.CMD_JOB_SYNC, false,
					new EnJenkinsToolOptions[] {JKSERVER_BASE}, null);

	public final static String CMD_JOB_COPY = "-c";
	public final static String CMD_JOB_UPDATE = "-u";
	public final static String CMD_JOB_SYNC = "-s";
	public final static String CMD_JOB_DELETE = "-r";

	private String command;
	private String value;
	private boolean isRequiredValue=false;
	private EnJenkinsToolOptions [] requiredOptions = null;
	private EnJenkinsToolOptions [] extraOptions = null;

	private EnJenkinsToolCmds(String command, boolean isRequiredValue, EnJenkinsToolOptions [] requiredOptions, 
			EnJenkinsToolOptions [] extraOptions) {
		this.command = command;
		this.isRequiredValue = isRequiredValue;
		this.requiredOptions = requiredOptions;
		this.extraOptions = extraOptions;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
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

	/**
	 * @return the isRequiredValue
	 */
	public boolean isRequiredValue() {
		return isRequiredValue;
	}

	/**
	 * @param isRequiredValue the isRequiredValue to set
	 */
	public void setRequiredValue(boolean isRequiredValue) {
		this.isRequiredValue = isRequiredValue;
	}

	/**
	 * @return the requiredOptions
	 */
	public EnJenkinsToolOptions[] getRequiredOptions() {
		return requiredOptions;
	}

	/**
	 * @param requiredOptions the requiredOptions to set
	 */
	public void setRequiredOptions(EnJenkinsToolOptions[] requiredOptions) {
		this.requiredOptions = requiredOptions;
	}

	/**
	 * @return the extraOptions
	 */
	public EnJenkinsToolOptions[] getExtraOptions() {
		return extraOptions;
	}

	/**
	 * @param extraOptions the extraOptions to set
	 */
	public void setExtraOptions(EnJenkinsToolOptions[] extraOptions) {
		this.extraOptions = extraOptions;
	}
	
	public static EnJenkinsToolCmds parseCommandLine(String [] args) throws Exception {
		EnJenkinsToolCmds command = null;
		
		List<String> argsList = Arrays.asList(args);
		
		if (argsList.contains(CMD_JOB_COPY)) {
			
		} else if (argsList.contains(CMD_JOB_UPDATE)) {
			command = JOB_COPY;
		} else if (argsList.contains(CMD_JOB_DELETE)) {
			command = JOB_COPY;
		} else if (argsList.contains(CMD_JOB_SYNC)) {
			command = JOB_COPY;
		}
		
		if (command == null) {
			throw new Exception ("There isn't a command [ " + CMD_JOB_COPY 
					+ "," + CMD_JOB_UPDATE + "," + CMD_JOB_SYNC 
					+ "," + CMD_JOB_DELETE + " ]"
					);
		}
		
		return command;
	}
}
