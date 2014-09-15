/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.sptek.batch.jenkins.model;

import com.google.common.base.Function;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.net.URLEncoder.encode;

public class Computer extends BaseModel {
    private String displayName;

    public List<Computer> getComputers() {
        return computer;
    }

    public void setComputer(List<Computer> computer) {
        this.computer = computer;
    }

    List<Computer> computer;

    public Computer() {
    }

    public Computer(String displayName) {
        this();
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public ComputerWithDetails details() throws IOException {
        return client.get("/computer/" + displayName.replaceAll("master", "(master)"), ComputerWithDetails.class);
    }

    @SuppressWarnings("unused")
	private static class MapEntryToQueryStringPair implements Function<Map.Entry<String, String>, String> {
        @SuppressWarnings("deprecation")
		@Override
        public String apply(Map.Entry<String, String> entry) {
            return encode(entry.getKey()) + "=" + encode(entry.getValue());
        }
    }
}
