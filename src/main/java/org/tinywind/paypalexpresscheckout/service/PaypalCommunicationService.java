package org.tinywind.paypalexpresscheckout.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;
import org.tinywind.paypalexpresscheckout.model.Checkout;
import org.tinywind.paypalexpresscheckout.model.CheckoutRequest;
import org.tinywind.paypalexpresscheckout.model.CheckoutResponse;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import static org.tinywind.paypalexpresscheckout.util.UrlQueryEncoder.encodeQueryParams;

/**
 * Created by tinywind on 2016-07-14.
 */
@Service
public class PaypalCommunicationService {
    @Autowired
    private PaypalConfig paypal;

    public CheckoutResponse callShortcutExpressCheckout(Checkout checkout, String returnURL, String cancelURL) {
        Map<String, Object> params = new HashMap<>();

        params.put("RETURNURL", returnURL);
        params.put("CANCELURL", cancelURL);
        params.put("PAYMENTREQUEST_0_AMT", checkout.getTotalAmount());
        params.put("PAYMENTREQUEST_0_CURRENCYCODE", checkout.getCurrencyCodeType());
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
        params.put("PAYMENTREQUEST_0_SELLERPAYPALACCOUNTID", paypal.getSellerEmail());

        params.put("TOKEN", checkout.getToken());
        params.put("PAYERID", checkout.getPayerId());
        params.put("PAYMENTREQUEST_0_PAYMENTACTION", checkout.getPaymentType());
        params.put("PAYMENTREQUEST_0_AMT", checkout.getTotalAmount());
        params.put("PAYMENTREQUEST_0_CURRENCYCODE", checkout.getCurrencyCodeType());
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
        params.put("VERSION", paypal.getGvVersion());
        params.put("USER", paypal.getGvApiUserName());
        params.put("PWD", paypal.getGvApiPassword());
        params.put("SIGNATURE", paypal.getGvApiSignature());
        params.put("BUTTONSOURCE", paypal.getGvBNCode());

        String agent = "Mozilla/4.0";
        StringBuilder respText = new StringBuilder("");

        try {
            URL postURL = new URL(paypal.getGvApiEndpoint());
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
                try {
                    final String key = URLDecoder.decode(stInternalTokenizer.nextToken(), "UTF-8");
                    final String value = URLDecoder.decode(stInternalTokenizer.nextToken(), "UTF-8");

                    final Field field = Checkout.class.getDeclaredField(Checkout.checkoutVarName(key));
                    final Constructor<? extends Field> constructor = field.getClass().getConstructor(String.class);
                    field.set(nvp, constructor.newInstance(value));
                } catch (UnsupportedEncodingException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return nvp;
    }
}
