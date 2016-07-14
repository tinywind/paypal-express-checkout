package org.tinywind.paypalexpresscheckout.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(locations = {"paypal.properties"})
public class PaypalProperty {
    @NestedConfigurationProperty
    private Map<String, String> properties = new HashMap<>();

    public Map<String, String> getProperties() {
        return properties;
    }
}
