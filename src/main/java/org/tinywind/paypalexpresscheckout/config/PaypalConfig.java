package org.tinywind.paypalexpresscheckout.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import lombok.Data;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Properties;

@Data
@Component
public class PaypalConfig {
    private String gvApiUserName;
    private String gvApiPassword;
    private String gvApiSignature;
    private String gvApiEndpoint;
    private String gvBNCode;
    private String gvVersion;
    private String paypalUrl;
    private Boolean userActionFlag;
    private String sellerEmail;
    private String environment;

    @PostConstruct
    public void init() {
        YamlPropertiesFactoryBean loader = new YamlPropertiesFactoryBean();
        loader.setResources(new ClassPathResource("paypal.yml"));
        Properties properties = loader.getObject();
        boolean sandbox = Boolean.parseBoolean(properties.getProperty("setting.flag"));
        environment = sandbox ? "sandbox" : "production";
        String prefix = "account." + (sandbox ? "sandbox" : "paypal") + ".";
        gvApiUserName = properties.getProperty(prefix + "user");
        gvApiPassword = properties.getProperty(prefix + "password");
        gvApiSignature = properties.getProperty(prefix + "signature");
        gvApiEndpoint = properties.getProperty(prefix + "nvp_endpoint");
        paypalUrl = properties.getProperty(prefix + "checkout_url");
        gvBNCode = properties.getProperty("setting.sbn_code");
        gvVersion = properties.getProperty("setting.api_version");
        userActionFlag = Boolean.parseBoolean(properties.getProperty("setting.useraction"));
        sellerEmail = properties.getProperty("account.seller_email");
        String ssl = properties.getProperty("setting.ssl");
        if (ssl != null) System.setProperty("https.protocols", ssl);
    }
}

