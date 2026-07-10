package com.smartlogistics.notificationservice;

import com.smartlogistics.notificationservice.util.TemplateEngine;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemplateEngineTest {

    @Test
    public void testPlaceholderReplacement() {
        String template = "Hello ${firstName}, your shipment ${shipmentId} has been matched.";
        Map<String, Object> variables = new HashMap<>();
        variables.put("firstName", "Suyog");
        variables.put("shipmentId", "12345");

        String result = TemplateEngine.process(template, variables);
        assertEquals("Hello Suyog, your shipment 12345 has been matched.", result);
    }

    @Test
    public void testMissingPlaceholders() {
        String template = "Hello ${firstName}, your code is ${code}.";
        Map<String, Object> variables = new HashMap<>();
        variables.put("firstName", "Suyog");

        String result = TemplateEngine.process(template, variables);
        assertEquals("Hello Suyog, your code is ${code}.", result);
    }

    @Test
    public void testNullTemplate() {
        String result = TemplateEngine.process(null, new HashMap<>());
        assertEquals("", result);
    }
}
