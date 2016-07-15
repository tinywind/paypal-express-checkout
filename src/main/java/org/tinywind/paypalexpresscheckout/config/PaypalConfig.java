package org.tinywind.paypalexpresscheckout.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

@Data
@Component
public class PaypalConfig {
    @Autowired
    private PaypalProperty property;

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

//    @PostConstruct
    public void init() {
        Map<String, String> prop = property.getProperties();
        String strSandbox = "";
        environment = "production";
        if (Objects.equals("true", prop.get("sandbox.flag"))) {
            strSandbox = "sandbox.";
            environment = "sandbox";
        }
        gvApiUserName = prop.get(strSandbox + "user");
        gvApiPassword = prop.get(strSandbox + "password");
        gvApiSignature = prop.get(strSandbox + "signature");
        gvApiEndpoint = prop.get(strSandbox + "nvp_endpoint");
        gvBNCode = prop.get("sbn.code");
        gvVersion = prop.get("api.version");
        paypalUrl = prop.get(strSandbox + "checkout_url");
        userActionFlag = Boolean.getBoolean(prop.get("useraction"));
        sellerEmail = prop.get("seller.email");
        java.lang.System.setProperty("https.protocols", prop.get("ssl"));
    }
}

