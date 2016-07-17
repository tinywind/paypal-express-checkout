package org.tinywind.paypalexpresscheckout.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;
import org.tinywind.paypalexpresscheckout.model.Checkout;
import org.tinywind.paypalexpresscheckout.model.CheckoutResponse;
import org.tinywind.paypalexpresscheckout.util.ReflectionUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static org.tinywind.paypalexpresscheckout.util.UrlQueryEncoder.encodeQueryParams;

@Service
public class PaypalCommunicationService {
    private static final Logger logger = LoggerFactory.getLogger(PaypalCommunicationService.class);

    @Autowired
    private PaypalConfig paypalConfig;

    public CheckoutResponse callShortcutExpressCheckout(Checkout checkout, String returnURL, String cancelURL) {
        Map<String, Object> params = new HashMap<>();

        params.put("RETURNURL", returnURL);
        params.put("CANCELURL", cancelURL);
        params.put("PAYMENTREQUEST_0_AMT", checkout.getTotalAmount());
        params.put("PAYMENTREQUEST_0_CURRENCYCODE", checkout.getCurrencyCode());
        params.put("PAYMENTREQUEST_0_ITEMAMT", checkout.getItemAmount());
        params.put("PAYMENTREQUEST_0_TAXAMT", checkout.getTaxAmount());
        params.put("PAYMENTREQUEST_0_SHIPPINGAMT", checkout.getShippingAmount());
        params.put("PAYMENTREQUEST_0_HANDLINGAMT", checkout.getHandlingAmount());
        params.put("PAYMENTREQUEST_0_SHIPDISCAMT", checkout.getShippingDiscount());
        params.put("PAYMENTREQUEST_0_INSURANCEAMT", checkout.getInsuranceAmount());
        params.put("L_PAYMENTREQUEST_0_NAME0", checkout.getProductName());
        params.put("L_PAYMENTREQUEST_0_NUMBER0", checkout.getOrderNumber());
        params.put("L_PAYMENTREQUEST_0_DESC0", checkout.getProductDescription());
        params.put("L_PAYMENTREQUEST_0_AMT0", checkout.getLTotalAmount());
        params.put("L_PAYMENTREQUEST_0_QTY0", checkout.getQuantity());
        params.put("LOGOIMG", checkout.getLogoImage());

        return httpcall("SetExpressCheckout", params);
    }

    public CheckoutResponse getShippingDetails(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("TOKEN", token);
        return httpcall("GetExpressCheckoutDetails", params);
    }

    public CheckoutResponse confirmPayment(Checkout checkout, String serverName) {
        Map<String, Object> params = new HashMap<>();

        params.put("IPADDRESS", serverName);
        params.put("PAYMENTREQUEST_0_SELLERPAYPALACCOUNTID", paypalConfig.getSellerEmail());

        params.put("TOKEN", checkout.getToken());
        params.put("PAYERID", checkout.getPayerId());
        params.put("PAYMENTREQUEST_0_PAYMENTACTION", checkout.getPaymentType());
        params.put("PAYMENTREQUEST_0_AMT", checkout.getTotalAmount());
        params.put("PAYMENTREQUEST_0_CURRENCYCODE", checkout.getCurrencyCode());
        params.put("PAYMENTREQUEST_0_ITEMAMT", checkout.getItemAmount());
        params.put("PAYMENTREQUEST_0_TAXAMT", checkout.getTaxAmount());
        params.put("PAYMENTREQUEST_0_SHIPPINGAMT", checkout.getShippingAmount());
        params.put("PAYMENTREQUEST_0_HANDLINGAMT", checkout.getHandlingAmount());
        params.put("PAYMENTREQUEST_0_SHIPDISCAMT", checkout.getShippingDiscount());
        params.put("PAYMENTREQUEST_0_INSURANCEAMT", checkout.getInsuranceAmount());

        return httpcall("DoExpressCheckoutPayment", params);
    }

    private CheckoutResponse httpcall(String methodName, Map<String, Object> params) {
        params.put("METHOD", methodName);
        params.put("VERSION", paypalConfig.getGvVersion());
        params.put("USER", paypalConfig.getGvApiUserName());
        params.put("PWD", paypalConfig.getGvApiPassword());
        params.put("SIGNATURE", paypalConfig.getGvApiSignature());
        params.put("BUTTONSOURCE", paypalConfig.getGvBNCode());

        String agent = "Mozilla/4.0";
        StringBuilder respText = new StringBuilder("");

        try {
            URL postURL = new URL(paypalConfig.getGvApiEndpoint());
            HttpURLConnection conn = (HttpURLConnection) postURL.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);

            String paramString = encodeQueryParams(params);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", agent);
            conn.setRequestProperty("Content-Length", String.valueOf(paramString.length()));
            conn.setRequestMethod("POST");

            DataOutputStream output = new DataOutputStream(conn.getOutputStream());
            output.writeBytes(paramString);
            output.flush();
            output.close();

            int rc = conn.getResponseCode();
            if (rc != -1) {
                BufferedReader is = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = is.readLine()) != null) respText.append(line);
                return decodeNVP(respText.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CheckoutResponse decodeNVP(String payload) {
        final CheckoutResponse nvp = new CheckoutResponse();
        final StringTokenizer stTok = new StringTokenizer(payload, "&");
        while (stTok.hasMoreTokens()) {
            final StringTokenizer stInternalTokenizer = new StringTokenizer(stTok.nextToken(), "=");
            if (stInternalTokenizer.countTokens() == 2) {
                String key = null, value = null;
                try {
                    key = URLDecoder.decode(stInternalTokenizer.nextToken(), "UTF-8");
                    value = URLDecoder.decode(stInternalTokenizer.nextToken(), "UTF-8");

                    final Field field = CheckoutResponse.class.getDeclaredField(Checkout.checkoutVarName(key));
                    ReflectionUtil.setValue(nvp, CheckoutResponse.class, field, value);
                } catch (Exception e) {
                    logger.error(e.getClass().getName() + ": " + e.getMessage());
                    if (key != null) logger.debug("    key: " + key + ", value: " + value);
                }
            }
        }
        return nvp;
    }
}
