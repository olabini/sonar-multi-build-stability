/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonar.plugins.multibuildstability;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.resources.Project;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Evgeny Mandrikov
 */
public class BuildStabilityMetrics implements Metrics {
    public static final String DOMAIN_BUILD = "Continuous integration";

    public static Metric BUILDS(int index) {
        return new Metric(
                          "builds_" + index,
                          "Builds",
                          "Number of builds",
                          Metric.ValueType.INT,
                          Metric.DIRECTION_NONE,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric FAILED(int index) {
        return new Metric(
                          "build_failures_" + index,
                          "Failed Builds",
                          "Number of failed builds",
                          Metric.ValueType.INT,
                          Metric.DIRECTION_WORST,
                          false,
                          DOMAIN_BUILD
                          );
    }


    public static Metric SUCCESS_RATE(int index) {
        return new Metric(
                          "build_success_density_" + index,
                          "Success Rate (%)",
                          "Ratio of successful builds",
                          Metric.ValueType.PERCENT,
                          Metric.DIRECTION_BETTER,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric AVG_DURATION(int index) {
        return new Metric(
                          "build_average_duration_" + index,
                          "Average Duration",
                          "Average Duration",
                          Metric.ValueType.MILLISEC,
                          Metric.DIRECTION_WORST,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric LONGEST_DURATION(int index) {
        return new Metric(
                          "build_longest_duration_" + index,
                          "Longest duration",
                          "Duration of longest successful build",
                          Metric.ValueType.MILLISEC,
                          Metric.DIRECTION_WORST,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric SHORTEST_DURATION(int index) {
        return new Metric(
                          "build_shortest_duration_" + index,
                          "Shortest duration",
                          "Duration of shortest successful build",
                          Metric.ValueType.MILLISEC,
                          Metric.DIRECTION_WORST,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric AVG_TIME_TO_FIX(int index) {
        return new Metric(
                          "build_average_time_to_fix_failure_" + index,
                          "Average time to fix a failure",
                          "Average time to fix a failure",
                          Metric.ValueType.MILLISEC,
                          Metric.DIRECTION_WORST,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric LONGEST_TIME_TO_FIX(int index) {
        return new Metric(
                          "build_longest_time_to_fix_failure_" + index,
                          "Longest time to fix a failure",
                          "Longest time to fix a failure",
                          Metric.ValueType.MILLISEC,
                          Metric.DIRECTION_WORST,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric AVG_BUILDS_TO_FIX(int index) {
        return new Metric(
                          "build_average_builds_to_fix_failure_" + index,
                          "Average number of builds between fixes",
                          "Average number of builds between fixes",
                          Metric.ValueType.INT,
                          Metric.DIRECTION_WORST,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric DURATIONS(int index) {
        return new Metric(
                          "build_durations_" + index,
                          "Durations",
                          "Durations",
                          Metric.ValueType.DATA,
                          Metric.DIRECTION_NONE,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric RESULTS(int index) {
        return new Metric(
                          "build_results_" + index,
                          "Results",
                          "Results",
                          Metric.ValueType.DATA,
                          Metric.DIRECTION_NONE,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public static Metric NAME(int index) {
        return new Metric(
                          "build_name_" + index,
                          "Name",
                          "Name",
                          Metric.ValueType.DATA,
                          Metric.DIRECTION_NONE,
                          false,
                          DOMAIN_BUILD
                          );
    }

    public List<Metric> getMetrics() {
        List<Metric> result = new ArrayList<Metric>(10 * 12);

        for(int i = 0; i<10; i++) {
            result.add(BUILDS(i));
            result.add(FAILED(i));
            result.add(SUCCESS_RATE(i));

            result.add(AVG_DURATION(i));
            result.add(LONGEST_DURATION(i));
            result.add(SHORTEST_DURATION(i));

            result.add(AVG_TIME_TO_FIX(i));
            result.add(LONGEST_TIME_TO_FIX(i));
            result.add(AVG_BUILDS_TO_FIX(i));

            result.add(DURATIONS(i));
            result.add(RESULTS(i));

            result.add(NAME(i));
        }

        return result;
    }
}
