/* 
 * Copyright (c) 2013 SK planet. 
 * All right reserved. 
 * 
 * This software is the confidential and proprietary information of SK planet. 
 * You shall not disclose such Confidential Information and 
 * shall use it only in accordance with the terms of the license agreement 
 * you entered into with SK planet. 
 */
package com.sptek.batch.jenkins.model;

import java.net.URI;
import java.net.URISyntaxException;

import com.sptek.batch.jenkins.server.JenkinsServer;

/** 
 * <p></p>
 * 
 * <ul>
 * <li>Created on : 2014. 5. 12.</li>
 * <li>Created by : incross_mwhong</li>
 * <li>Last Updated on : 2014. 5. 12.</li>
 * <li>Last Updated by : incross_mwhong</li>
 * </ul>
 */
public class JenkinsConnector {
	private String URL = null;
	private String account = null;
	private String password = null;

	public JenkinsConnector(String URL, String account, String password) {
		this.URL = URL;
		this.account = account;
		this.password = password;
	}
	
	public JenkinsServer getJenkinsServer() throws URISyntaxException {
		return new JenkinsServer(new URI(this.URL), this.account, this.password);
	}
	
	public JenkinsServer getJenkinsServer(String name) throws URISyntaxException {
		JenkinsServer server = new JenkinsServer(new URI(this.URL), this.account, this.password);
		if (server != null) {
			server.setName(name);
		}
		return server;
	}
}
