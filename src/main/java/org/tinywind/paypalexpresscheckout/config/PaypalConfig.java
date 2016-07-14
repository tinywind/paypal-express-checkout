package org.tinywind.paypalexpresscheckout.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Data
@Component
public class PaypalConfig {
    @Autowired
    private PaypalProperty property;

    private String gvAPIUserName;
    private String gvAPIPassword;
    private String gvAPISignature;
    private String gvAPIEndpoint;
    private String gvBNCode;
    private String gvVersion;
    private String paypalUrl;
    private String userActionFlag;
    private String sellerEmail;
    private String environment;

    @PostConstruct
    public void init() {
        Map<String, String> prop = property.getProperties();
        String strSandbox = "";
        environment = "production";
        if (prop.get("SANDBOX_FLAG").equals("true")) {
            strSandbox = "_SANDBOX";
            environment = "sandbox";
        }
        gvAPIUserName = prop.get("PP_USER" + strSandbox);
        gvAPIPassword = prop.get("PP_PASSWORD" + strSandbox);
        gvAPISignature = prop.get("PP_SIGNATURE" + strSandbox);
        gvAPIEndpoint = prop.get("PP_NVP_ENDPOINT" + strSandbox);
        gvBNCode = prop.get("SBN_CODE");
        gvVersion = prop.get("API_VERSION");
        paypalUrl = prop.get("PP_CHECKOUT_URL" + strSandbox);
        userActionFlag = prop.get("USERACTION_FLAG");
        sellerEmail = prop.get("PP_SELLER_EMAIL");
        java.lang.System.setProperty("https.protocols", prop.get("SSL_VERSION_TO_USE"));
    }
}

