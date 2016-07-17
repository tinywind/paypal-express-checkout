package org.tinywind.paypalexpresscheckout.config;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.Map;

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

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() throws YamlException {
        final YamlReader reader = new YamlReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("paypal.yml")));
        final Map<String, Object> prop = (Map<String, Object>) reader.read();
        final Map<String, Object> setting = (Map<String, Object>) prop.get("setting");
        final Map<String, Object> account = (Map<String, Object>) prop.get("account");
        Map<String, Object> accountInfoSender;
        if ("true".equalsIgnoreCase((String) setting.get("flag"))) {
            environment = "sandbox";
            accountInfoSender = (Map<String, Object>) account.get("sandbox");
        } else {
            environment = "production";
            accountInfoSender = (Map<String, Object>) account.get("paypal");
        }
        final Map<String, Object> accountInfo = accountInfoSender;
        gvApiUserName = (String) accountInfo.get("user");
        gvApiPassword = (String) accountInfo.get("password");
        gvApiSignature = (String) accountInfo.get("signature");
        gvApiEndpoint = (String) accountInfo.get("nvp_endpoint");
        paypalUrl = (String) accountInfo.get("checkout_url");
        gvBNCode = (String) setting.get("sbn_code");
        gvVersion = (String) setting.get("api_version");
        userActionFlag = Boolean.getBoolean((String) setting.get("useraction"));
        sellerEmail = (String) account.get("seller_email");
        final String ssl = (String) setting.get("ssl");
        if (ssl != null) java.lang.System.setProperty("https.protocols", ssl);
    }
}

