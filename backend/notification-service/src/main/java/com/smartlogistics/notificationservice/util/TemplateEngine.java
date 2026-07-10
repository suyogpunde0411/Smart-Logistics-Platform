package com.smartlogistics.notificationservice.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    public static String process(String template, Map<String, Object> variables) {
        if (template == null) {
            return "";
        }
        if (variables == null || variables.isEmpty()) {
            return template;
        }

        Matcher matcher = PATTERN.matcher(template);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            Object value = variables.get(key);
            String replacement = value != null ? value.toString() : matcher.group(0);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
