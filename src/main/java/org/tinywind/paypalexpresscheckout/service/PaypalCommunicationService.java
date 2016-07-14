package org.tinywind.paypalexpresscheckout.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;
import org.tinywind.paypalexpresscheckout.model.Checkout;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by tinywind on 2016-07-14.
 */
@Service
public class PaypalCommunicationService {
    @Autowired
    private PaypalConfig paypal;

    private static String encodeQueryParams(Map<String, Object> params) {
        return encodeQueryParams(convertMapToNameValuePair(params));
    }

    private static List<NameValuePair> convertMapToNameValuePair(Map<String, Object> params) {
        List<NameValuePair> li = new ArrayList<>();
        for (Map.Entry<String, Object> e : params.entrySet()) {
            Object value = e.getValue();
            if (value != null) {
                if (value instanceof List) {
                    for (Object o : (List) value) li.add(new BasicNameValuePair(e.getKey(), o.toString()));
                } else {
                    li.add(new BasicNameValuePair(e.getKey(), value.toString()));
                }
            }
        }
        return li;
    }

    private static String encodeQueryParams(List<NameValuePair> nameValuePairs) {
        return URLEncodedUtils.format(nameValuePairs, "UTF-8");
    }

    public Checkout callShortcutExpressCheckout(Checkout checkout, String returnURL, String cancelURL) {
        Map<String, Object> params = new HashMap<>();

        params.put("RETURNURL", returnURL);
        params.put("CANCELURL", cancelURL);
        params.put("PAYMENTREQUEST_0_AMT", checkout.getPAYMENTREQUEST_0_AMT());
        params.put("PAYMENTREQUEST_0_CURRENCYCODE", checkout.getCurrencyCodeType());
        params.put("PAYMENTREQUEST_0_ITEMAMT", checkout.getPAYMENTREQUEST_0_ITEMAMT());
        params.put("PAYMENTREQUEST_0_TAXAMT", checkout.getPAYMENTREQUEST_0_TAXAMT());
        params.put("PAYMENTREQUEST_0_SHIPPINGAMT", checkout.getShippingAmount());
        params.put("PAYMENTREQUEST_0_HANDLINGAMT", checkout.getPAYMENTREQUEST_0_HANDLINGAMT());
        params.put("PAYMENTREQUEST_0_SHIPDISCAMT", checkout.getPAYMENTREQUEST_0_SHIPDISCAMT());
        params.put("PAYMENTREQUEST_0_INSURANCEAMT", checkout.getPAYMENTREQUEST_0_INSURANCEAMT());
        params.put("L_PAYMENTREQUEST_0_NAME0", checkout.getL_PAYMENTREQUEST_0_NAME0());
        params.put("L_PAYMENTREQUEST_0_NUMBER0", checkout.getL_PAYMENTREQUEST_0_NUMBER0());
        params.put("L_PAYMENTREQUEST_0_DESC0", checkout.getL_PAYMENTREQUEST_0_DESC0());
        params.put("L_PAYMENTREQUEST_0_AMT0", checkout.getL_PAYMENTREQUEST_0_AMT0());
        params.put("L_PAYMENTREQUEST_0_QTY0", checkout.getL_PAYMENTREQUEST_0_QTY0());
        params.put("LOGOIMG", checkout.getLOGOIMG());

        return httpcall("SetExpressCheckout", params);
    }

    public Checkout getShippingDetails(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("TOKEN", token);
        return httpcall("GetExpressCheckoutDetails", params);
    }

    public Checkout confirmPayment(Checkout checkout, String serverName) {
        Map<String, Object> params = new HashMap<>();

        params.put("IPADDRESS", serverName);
        params.put("PAYMENTREQUEST_0_SELLERPAYPALACCOUNTID", paypal.getSellerEmail());

        params.put("TOKEN", checkout.getToken());
        params.put("PAYERID", checkout.getPayer_id());
        params.put("PAYMENTREQUEST_0_PAYMENTACTION", checkout.getPaymentType());
        params.put("PAYMENTREQUEST_0_AMT", checkout.getPAYMENTREQUEST_0_AMT());
        params.put("PAYMENTREQUEST_0_CURRENCYCODE", checkout.getCurrencyCodeType());
        params.put("PAYMENTREQUEST_0_ITEMAMT", checkout.getPAYMENTREQUEST_0_ITEMAMT());
        params.put("PAYMENTREQUEST_0_TAXAMT", checkout.getPAYMENTREQUEST_0_TAXAMT());
        params.put("PAYMENTREQUEST_0_SHIPPINGAMT", checkout.getShippingAmount());
        params.put("PAYMENTREQUEST_0_HANDLINGAMT", checkout.getPAYMENTREQUEST_0_HANDLINGAMT());
        params.put("PAYMENTREQUEST_0_SHIPDISCAMT", checkout.getPAYMENTREQUEST_0_SHIPDISCAMT());
        params.put("PAYMENTREQUEST_0_INSURANCEAMT", checkout.getPAYMENTREQUEST_0_INSURANCEAMT());

        return httpcall("DoExpressCheckoutPayment", params);
    }

    private Checkout httpcall(String methodName, Map<String, Object> params) {
        params.put("METHOD", methodName);
        params.put("VERSION", paypal.getGvVersion());
        params.put("USER", paypal.getGvAPIUserName());
        params.put("PWD", paypal.getGvAPIPassword());
        params.put("SIGNATURE", paypal.getGvAPISignature());
        params.put("BUTTONSOURCE", paypal.getGvBNCode());

        String agent = "Mozilla/4.0";
        StringBuilder respText = new StringBuilder("");

        try {
            URL postURL = new URL(paypal.getGvAPIEndpoint());
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
                return deformatNVP(respText.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Checkout deformatNVP(String pPayload) {
        Checkout nvp = new Checkout();
        StringTokenizer stTok = new StringTokenizer(pPayload, "&");
        while (stTok.hasMoreTokens()) {
            StringTokenizer stInternalTokenizer = new StringTokenizer(stTok.nextToken(), "=");
            if (stInternalTokenizer.countTokens() == 2) {
                try {
                    String key = URLDecoder.decode(stInternalTokenizer.nextToken(), "UTF-8");
                    String value = URLDecoder.decode(stInternalTokenizer.nextToken(), "UTF-8");

                    Field field = Checkout.class.getDeclaredField(key);
                    Constructor<? extends Field> constructor = field.getClass().getConstructor(String.class);
                    field.set(nvp, constructor.newInstance(value));
                } catch (UnsupportedEncodingException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return nvp;
    }
}
