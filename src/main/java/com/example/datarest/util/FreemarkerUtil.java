package com.example.datarest.util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FreemarkerUtil {

    // Default pattern is matching string within ${...}, e.g. ${name} will match name
    public static final Pattern DEFAULT_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

    /**
     *
     * @param input    the string content of a template file to be parsed
     * @param pattern  the pattern to be used for matching
     * @return         a list of string values matched by the pattern
     */
    public static List<String> findSubstitutionVars(String input, Pattern pattern) {
        if (input == null) {
            return null;
        }

        Matcher matcher = pattern.matcher(input);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            String expr = matcher.group(1);
            boolean found = false;
            for (String str: list) {
                if (str.equalsIgnoreCase(expr)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                list.add(expr);
            }
        }
        return list;
    }


    public static String parseTemplate(String templateName, File templateDirectory, Map<String, String> map) throws Exception {
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading( templateDirectory );
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );

        Template template = cfg.getTemplate( templateName );
        StringWriter sw = new StringWriter();
        template.process(map, sw);
        return sw.toString();
    }

}
