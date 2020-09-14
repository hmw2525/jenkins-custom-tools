/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package hmw2525.batch.jenkins.model;

import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Collections2.filter;

public class BuildWithDetails extends Build {
    @SuppressWarnings("rawtypes")
	List actions;
    boolean building;
    String description;
    int duration;
    String fullDisplayName;
    String id;
    long timestamp;
    BuildResult result;

    public boolean isBuilding() {
        return building;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public String getFullDisplayName() {
        return fullDisplayName;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BuildResult getResult() {
        return result;
    }

    @SuppressWarnings("rawtypes")
	public List getActions() {
		return actions;
	}

	@SuppressWarnings("rawtypes")
	public void setActions(List actions) {
        this.actions = actions;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> getParameters() {
        Collection parameters = filter(actions, new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> action) {
                return action.containsKey("parameters");
            }
        });

        Map<String, String> params = new HashMap<String, String>();

        if (parameters != null && !parameters.isEmpty()) {
            for(Map<String, String> param : ((Map<String, List<Map<String, String>>>) parameters.toArray()[0]).get("parameters")) {
                String key = param.get("name");
                String value = param.get("value");
                params.put(key, value);
            }
        }

        return params;
    }
}
