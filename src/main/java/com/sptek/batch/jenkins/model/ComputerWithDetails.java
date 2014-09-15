/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.sptek.batch.jenkins.model;

import com.google.common.base.Function;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ComputerWithDetails extends Job {
    String displayName;

	List<Map> actions;
    List<Map> executors;
    Boolean idle;
    Boolean jnlp;
    Boolean launchSupported;
    Map loadStatistics;
    Boolean manualLaunchAllowed;
    Map<String, Map> monitorData;
    Integer numExecutors;
    Boolean offline;
    Object  offlineCause;
    String  offlineReason;
    List<Map> oneOffExecutors;
    Boolean temporarilyOffline;

    public String getDisplayName() {
        return displayName;
    }

    public List<Map> getActions() {
        return actions;
    }

    public List<Map> getExecutors() {
        return executors;
    }

    public Boolean getIdle() {
        return idle;
    }

    public Boolean getJnlp() {
        return jnlp;
    }

    public Boolean getLaunchSupported() {
        return launchSupported;
    }

    public Map getLoadStatistics() {
        return loadStatistics;
    }

    public Boolean getManualLaunchAllowed() {
        return manualLaunchAllowed;
    }

    public Map<String, Map> getMonitorData() {
        return monitorData;
    }

    public Integer getNumExecutors() {
        return numExecutors;
    }

    public Boolean getOffline() {
        return offline;
    }

    public Object getOfflineCause() {
        return offlineCause;
    }

    public String getOfflineReason() {
        return offlineReason;
    }

    public List<Map> getOneOffExecutors() {
        return oneOffExecutors;
    }

    public Boolean getTemporarilyOffline() {
        return temporarilyOffline;
    }

    @SuppressWarnings("unused")
	private class ComputerWithClient implements Function<Computer, Computer> {
        @Override
        public Computer apply(Computer computer) {
            computer.setClient(client);
            return computer;
        }
    }
}