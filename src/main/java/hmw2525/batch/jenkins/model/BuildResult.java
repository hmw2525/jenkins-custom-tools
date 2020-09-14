/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package hmw2525.batch.jenkins.model;

public enum BuildResult {
    FAILURE,
    UNSTABLE,
    REBUILDING,
    BUILDING,
    ABORTED,
    SUCCESS,
    UNKNOWN
}
