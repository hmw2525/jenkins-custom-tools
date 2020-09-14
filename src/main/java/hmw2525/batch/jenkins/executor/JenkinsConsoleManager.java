/* 
 * Copyright (c) 2013 SK planet. 
 * All right reserved. 
 * 
 * This software is the confidential and proprietary information of SK planet. 
 * You shall not disclose such Confidential Information and 
 * shall use it only in accordance with the terms of the license agreement 
 * you entered into with SK planet. 
 */
package hmw2525.batch.jenkins.executor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.log4j.Logger;

import hmw2525.batch.jenkins.model.JenkinsConnector;
import hmw2525.batch.jenkins.model.Job;
import hmw2525.batch.jenkins.server.JenkinsServer;

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
public class JenkinsConsoleManager {
	
	private static final Logger logger = Logger.getLogger(JenkinsConsoleManager.class);
	
	private JenkinsConnector devJenkins = null;
	private JenkinsConnector qaJenkins = null;
	private JenkinsConnector prodJenkins = null;
	private JenkinsConnector deployJenkins = null;
	
	private JenkinsServer dev = null;
	private JenkinsServer qa = null;
	private JenkinsServer prod = null;
	private JenkinsServer deploy = null;
	
	public JenkinsConsoleManager() throws URISyntaxException {
		String account = "incross_mwhong";
		String password = "!2525Sptek";

		devJenkins  = new JenkinsConnector("http://172.21.192.28/sp_batch/jenkins", account, password);
		qaJenkins   = new JenkinsConnector("http://172.21.236.58/sp_batch/jenkins", account, password);
		prodJenkins = new JenkinsConnector("http://172.18.102.124:8610/sp_batch/jenkins/", account, password);
		deployJenkins = new JenkinsConnector("http://172.19.103.48:8080", account, password);

		dev = devJenkins.getJenkinsServer("DEV-BATCH");
		qa = qaJenkins.getJenkinsServer("QA-BATCH");
		prod = prodJenkins.getJenkinsServer("PROD-BATCH");
		deploy = deployJenkins.getJenkinsServer("CI");
	}
	
	public void copyJobBasedOnPROD(String jobName) throws Exception {
		
		Map<String, Job> devJobMap = dev.getJobs();
		Map<String, Job> qaJobMap = qa.getJobs();

		String jobConfig = prod.getJobXml(jobName);
		String key = jobName.toLowerCase();
		
		if (jobConfig != null) {
			jobConfig = jobConfig.replace("<disabled>true</disabled>", "<disabled>false</disabled>");
			jobConfig = jobConfig.replace("&lt;li style=&quot;color:red;&quot;&gt;[상용 Batch]&lt;/li&gt;", 
					"&lt;li style=&quot;color:blue;&quot;&gt;[QA Batch]&lt;/li&gt;");
			jobConfig = jobConfig.replace("prod", "qa");
			if (qaJobMap.containsKey(key)) {
   				String qaJobConfig = qa.getJobXml(jobName);
   				if (qaJobConfig != null && !qaJobConfig.contains("#Diff#")) {
    				qa.updateJob(jobName, new String(jobConfig.getBytes()));
    				logger.info("[QA] Update the job - " + jobName);
   				} else {
    				logger.info("[QA] Skip to update the job  (Find the flag [#Diff#]) - " + jobName);
   				}
			} else {
				qa.createJob(jobName, new String(jobConfig.getBytes()));
				logger.info("[QA] Create the job  - " + jobName);
			}

			jobConfig = jobConfig.replace("&lt;li style=&quot;color:red;&quot;&gt;[상용 Batch]&lt;/li&gt;", 
					"&lt;li style=&quot;color:gray;&quot;&gt;[DEV Batch]&lt;/li&gt;");
			jobConfig = jobConfig.replace("qa", "dev");
			if (devJobMap.containsKey(key)) {
   				String devJobConfig = dev.getJobXml(jobName);
   				if (devJobConfig != null && !devJobConfig.contains("#Diff#")) {
    				dev.updateJob(jobName, new String(jobConfig.getBytes()));
    				logger.info("[DEV] Update the job - " + jobName);
   				} else {
    				logger.info("[DEV] Skip to update the job  (Find the flag [#Diff#]) - " + jobName);
   				}
			} else {
				dev.createJob(jobName, new String(jobConfig.getBytes()));
				logger.info("[DEV] Create the job - " + jobName);
			}
		}
	}
	
	public void syncJobFromQAToDev() throws Exception {
		
		Map<String, Job> qaJobMap = qa.getJobs();
		Map<String, Job> devJobMap = dev.getJobs();

		for (String key : qaJobMap.keySet()) {
			Job job = qaJobMap.get(key);
			String jobName = job.getName();

			String jobConfig = qa.getJobXml(jobName);
			
			if (devJobMap.containsKey(key)) {
				dev.updateJob(jobName, new String(jobConfig.getBytes()));
				logger.info("[DEV] Update Job Configuration - " + jobName);
			} else {
				dev.createJob(jobName, new String(jobConfig.getBytes()));
				logger.info("[DEV] Update Job Configuration - " + jobName);
			}
		}
	}	
	
	public void syncJobFromPROD(String containJobName) throws Exception {
		
		Map<String, Job> qaJobMap = qa.getJobs();
		Map<String, Job> devJobMap = dev.getJobs();
		Map<String, Job> prodJobMap = prod.getJobs();

		for (String key : prodJobMap.keySet()) {
			if (containJobName == null || key.contains(containJobName.toLowerCase())) {

				Job job = prodJobMap.get(key);
				
    			String jobName = job.getName();
    			String jobConfig = prod.getJobXml(jobName);
    			
    			jobConfig = jobConfig.replace("&lt;li style=&quot;color:red;&quot;&gt;[상용 Batch]&lt;/li&gt;", 
    					"&lt;li style=&quot;color:blue;&quot;&gt;[QA Batch]&lt;/li&gt;");
    			jobConfig = jobConfig.replace("prod", "qa");
    			if (qaJobMap.containsKey(key)) {
    				String qaJobConfig = qa.getJobXml(jobName);
    				if (qaJobConfig != null && !qaJobConfig.contains("#Diff#")) {
	    				qa.updateJob(jobName, new String(jobConfig.getBytes()));
	    				logger.info("[QA] Update the job - " + jobName);
    				} else {
	    				logger.info("[QA] Skip to update the job  (Find the flag [#Diff#]) - " + jobName);
    				}
    			} else {
        			jobConfig = jobConfig.replace("prod", "qa");
    				qa.createJob(jobName, new String(jobConfig.getBytes()));
    				logger.info("[QA] Create the job - " + jobName);
    			}

    			jobConfig = jobConfig.replace("&lt;li style=&quot;color:blue;&quot;&gt;[QA Batch]&lt;/li&gt;",
    					"&lt;li style=&quot;color:gray;&quot;&gt;[DEV Batch]&lt;/li&gt;");
    			String toJobConfig = replaceTextofConfig(jobConfig, "triggers", "");
    			if (toJobConfig != null) {
    				jobConfig = toJobConfig;
    			}
    			jobConfig = jobConfig.replace("prod", "dev").replace("qa", "dev");
    			if (devJobMap.containsKey(key)) {
    				String devJobConfig = dev.getJobXml(jobName);
    				if (devJobConfig != null && !devJobConfig.contains("#Diff#")) {
    					dev.updateJob(jobName, new String(jobConfig.getBytes()));
        				logger.info("[DEV] Update the job - " + jobName);
    				} else {
	    				logger.info("[DEV] Skip to update the job (Find the flag [#Diff#]) - " + jobName);
    				}
    			} else {
        			jobConfig = jobConfig.replace("prod", "dev").replace("qa", "dev");;
    				dev.createJob(jobName, new String(jobConfig.getBytes()));
    				logger.info("[DEV] Create the job - " + jobName);
    			}
			}
		}
	}		
	
	public void updateJenkinsConfigPROD() throws Exception {
		
		Map<String, Job> prodJobMap = prod.getJobs();

		for (String key : prodJobMap.keySet()) {
			if (key.contains("StorePlatform-".toLowerCase())) {
				Job job = prodJobMap.get(key);
				
    			String jobName = job.getName();
    			String jobConfig = prod.getJobXml(jobName);
    			
    			jobConfig = jobConfig.replace("/tenant-tstore-batch", "/sac-batch");
    			
   				prod.updateJob(jobName, new String(jobConfig.getBytes()));
   				logger.info("[PROD] Update Job Configuration - " + jobName);
			}
		}
	}		

	public void updateJenkinsConfigQA() throws Exception {
		
		Map<String, Job> qaJobMap = qa.getJobs();

		for (String key : qaJobMap.keySet()) {
			Job job = qaJobMap.get(key);
			
			String jobName = job.getName();
			String jobConfig = qa.getJobXml(jobName);

			if (key.contains("StorePlatform-".toLowerCase()) || key.contains("DB-Lightweighting-SAC".toLowerCase())) {
    			jobConfig = jobConfig.replace("/tenant-tstore-batch", "/sac-batch");
    			
   				qa.updateJob(jobName, new String(jobConfig.getBytes()));
   				logger.info("[QA] Update Job Configuration - " + jobName);
			} 
		}
	}
	
	public void updateJenkinsConfigDEV() throws Exception {
		
		Map<String, Job> devJobMap = dev.getJobs();

		for (String key : devJobMap.keySet()) {
			Job job = devJobMap.get(key);
			
			String jobName = job.getName();
			String jobConfig = dev.getJobXml(jobName);

			if (key.contains("StorePlatform-".toLowerCase()) || key.contains("DB-Lightweighting-SAC".toLowerCase())) {
    			jobConfig = jobConfig.replace("/tenant-tstore-batch", "/sac-batch");
    			
   				dev.updateJob(jobName, new String(jobConfig.getBytes()));
   				logger.info("[DEV] Update Job Configuration - " + jobName);
			} 
		}
	}	

	public void updateAllJobConfigXml(JenkinsServer server, String tagName, String convertTxt, String jobNamePattern) throws Exception {
		
		Map<String, Job> jobMap = server.getJobs();
		
		for (String key : jobMap.keySet()) {
			Job job = jobMap.get(key);
			
			String jobName = job.getName();
			
			if (jobNamePattern == null || jobName.contains(jobNamePattern)) {
				String rpConfigXml = replaceTextofConfig(server, jobName, tagName, convertTxt);
	
				if (rpConfigXml != null) {
					server.updateJob(jobName, new String(rpConfigXml.getBytes()));
					logger.info("[" + server.getName() + "] " + jobName + " is updated ");
				} else {
					logger.info("[" + server.getName() + "] " + jobName + " is skipped");
				}
			}
		}
	}
	
	public void downloadDeployJenkinsJobs(JenkinsServer server, String jobName) throws Exception {
		String jobConfigXml = server.getJobXml(jobName);
		java.io.File configFile = new java.io.File("D:\\MyWork\\JenkinsConfig\\" + jobName + ".xml");
		if (!configFile.exists()) {
			configFile.createNewFile();
		}
		System.out.println(jobConfigXml);
		
		FileOutputStream out = new FileOutputStream(configFile);
		try {
			out.write(jobConfigXml.getBytes("UTF-8"));
		} finally {
			if (out != null) {out.close();}
		}
	}

	public void updateDeployJenkinsJobs(JenkinsServer server, String jobName) throws Exception {
		java.io.File configFile = new java.io.File("D:\\MyWork\\JenkinsConfig\\" + jobName + ".xml");
		FileInputStream in = new FileInputStream(configFile);
		try {
            int size = in.available();
            byte[] buf = new byte[size];
            
            // "FileStream.txt" 에서 buf 크기만큼을 읽어온다.
            // "copyFile.txt" 에 buf 배열 0 부터 readCount 만큼을 출력한다.
            int readCount = in.read(buf);
            if (readCount > 0) {
                String jobXml = new String(buf);
                System.out.println(jobXml);
                server.updateJob(jobName, jobXml);
            }
		} finally {
			if (in != null) {in.close();}
		}
	}
	
	
	public void execute() throws Exception {
		
//		copyJobBasedOnPROD("Tenant-Tstore-DailyEbookAnnounceMain");
//		copyJobBasedOnPROD("Tenant-Tstore-DailyEbookAnnouncePushMain");
//		syncJobFromPROD("DB-Lightweighting-WISEU-");
	}
	
	public void execute(String [] args) throws Exception {
//		String groovyStr = "// groovy script file name"
//				 + "\ndef groovyName = \"analyzeSummaryLog.groovy\"" 
//				 + "\n"
//				 + "\n//groovy script file full path"
//				 + "\ndef execGroovyPath = manager.build.getEnvironment().get(\"JENKINS_HOME\") + \"/scriptler/scripts/\" + groovyName"
//				 + "\n"
//				 + "\n//execute groovy script"
//				 + "\nevaluate(new File(execGroovyPath));";

//		updateAllJobConfigXml(dev, "groovyScript", groovyStr, "Storeplatform-Display-");
//				
//		downloadDeployJenkinsJobs(dev, "Storeplatform-Purchase-PrchsCntDeleteMain");
		syncJobFromPROD("DB-Lightweighting-");
		syncJobFromPROD("Tenant-");
		syncJobFromPROD("Storeplatform-");
	}
	
	private String replaceTextofConfig(JenkinsServer server, String jobName, String tagName, String convertTxt) throws Exception {
		String configXml = server.getJobXml(jobName);
		String rpConfigXml = null;
		if (configXml != null) {
			String startTag = "<" + tagName + ">";
			String endTag = "</" + tagName + ">";
			int start = configXml.indexOf(startTag) + startTag.length();
			int end = configXml.indexOf(endTag);
			if (start >= startTag.length() && end >= 0 ) {
				rpConfigXml = configXml.substring(0, start) 
							+ convertTxt
							+ configXml.substring(end);
			} 
		}
		return rpConfigXml;
	}
	
	private String replaceTextofConfig(String jobConfigXml, String tagName, String convertTxt) throws Exception {
		String configXml = jobConfigXml;
		String rpConfigXml = null;
		if (configXml != null) {
			String startTag = "<" + tagName + ">";
			String endTag = "</" + tagName + ">";
			int start = configXml.indexOf(startTag) + startTag.length();
			int end = configXml.indexOf(endTag);
			if (start >= startTag.length() && end >= 0 ) {
				rpConfigXml = configXml.substring(0, start) 
							+ convertTxt
							+ configXml.substring(end);
			} 
		}
		return rpConfigXml;
	}
	
	public static void main(String []args) {
		/* 
		 * <p>COMMAND EXAMPLE</p>
		 * <ul><ol>Copy One Job : BatchJobSyncronizer -c DB-Lightweighting-WISEU-Daily-NVECARERECEIPT -b prod</ol>
		 * <ol>Copy Jobs : BatchJobSyncronizer -c DB-Lightweighting-WISEU-Daily -b prod -multi </ol>
		 * <ol>Update One Job Config : BatchJobSyncronizer -u DB-Lightweighting-WISEU-Daily-NVECARERECEIPT -t prod -uc D://x.config -ut groovyScript</ol>
		 * <ol>Update Jobs Config : BatchJobSyncronizer -u DB-Lightweighting-WISEU-Daily-NVECARERECEIPT -t prod -uc D://x.config -ut groovyScript -multi</ol>
		 * <ol>Delete One Job : BatchJobSyncronizer -r DB-Lightweighting-WISEU-Daily-NVECARERECEIPT -b prod</ol>
		 * <ol>Delete One Job : BatchJobSyncronizer -multi -r DB-Lightweighting-WISEU-Daily -b prod</ol>
		 * <ol>Syncronize ALL : BatchJobSyncronizer -s -b prod </ol>
		 * </ul>
		 */
		
		JenkinsConsoleManager syncronizer = null;
		try {
			syncronizer = new JenkinsConsoleManager();
			syncronizer.execute(args);
//			logger.info((new Date(1406648232041L)));

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
