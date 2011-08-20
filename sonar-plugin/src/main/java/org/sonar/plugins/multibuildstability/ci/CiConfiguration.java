package org.sonar.plugins.multibuildstability.ci;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class CiConfiguration {
    private final String title;
    private final String system;
    private final String url;

    private CiConfiguration(String title, String system, String url) {
        this.title = title;
        this.system = system;
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSystem() {
        return this.system;
    }

    public String getUrl() {
        return this.url;
    }

    private final static Set<String> recognizedSystems = new HashSet<String>(Arrays.asList(
                                                                                           "bamboo",
                                                                                           "cruise",
                                                                                           "go",
                                                                                           "jenkins",
                                                                                           "hudson"
                                                                                           ));

    private static boolean isRecognizedSystem(String input) {
        return recognizedSystems.contains(input.toLowerCase().trim());
    }

    private static List<String> separateDifferentConfigurations(String input) {
        List<String> pieces = new LinkedList<String>();
        char[] chars = input.toCharArray();
        StringBuilder sep = new StringBuilder();
        char last = 0;
        for(int i = 0; i < chars.length; i++) {
            char current = chars[i];
            if(current == '|' || current == '\n') {
                if(last != '\\') {
                    String result = sep.toString().trim();
                    if(!result.isEmpty()) {
                        pieces.add(result);
                    }
                    sep = new StringBuilder();
                } else {
                    sep.deleteCharAt(sep.length()-1);
                    sep.append(current);
                }
                last = current;
            } else {
                sep.append(current);
                last = current;
            }
        }
        String result = sep.toString().trim();
        if(!result.isEmpty()) {
            pieces.add(result);
        }
        
        return pieces;
    }

    private static String map(String system) {
        return system.equalsIgnoreCase("jenkins") ? "hudson" : system;
    }

    private static CiConfiguration parseFrom(String input) {
        String title = "";
        String system = "hudson";
        String url = null;

        String rest = input;
        if(rest.startsWith("[")) {
            char[] ic = rest.toCharArray();
            StringBuilder titleBuilder = new StringBuilder();
            int i = 1;
            char last = 0;
            for(;i < ic.length; i++) {
                if(ic[i] == '[') {
                    if(last == '\\') {
                        titleBuilder.deleteCharAt(titleBuilder.length() - 1);
                    }
                } else if(ic[i] == ']') {
                    if(last == '\\') {
                        titleBuilder.deleteCharAt(titleBuilder.length() - 1);
                    } else {
                        break;
                    }
                } else if(ic[i] == '|') {
                    if(last == '\\') {
                        titleBuilder.deleteCharAt(titleBuilder.length() - 1);
                    }
                }

                last = ic[i];
                titleBuilder.append(last);
            }
            title = titleBuilder.toString();
            rest = rest.substring(i+1);
        }

        String[] split = rest.split(":");
        int start = 0;
        if(isRecognizedSystem(split[0])) {
            system = map(split[0].toLowerCase().trim());
            start++;
        }
        StringBuilder urlBuilder = new StringBuilder();
        String sep = "";
        for(;start < split.length; start++) {
            urlBuilder.append(sep);
            urlBuilder.append(split[start]);
            sep = ":";
        }
        url = urlBuilder.toString().trim();
        
        return new CiConfiguration(title, system, url);
    }

    public static List<CiConfiguration> parseAllFrom(String input) {
        List<String> pieces = separateDifferentConfigurations(input);
        List<CiConfiguration> result = new LinkedList<CiConfiguration>();
        
        for(String spec : pieces) {
            result.add(parseFrom(spec));
        }

        return result;
    }
}
