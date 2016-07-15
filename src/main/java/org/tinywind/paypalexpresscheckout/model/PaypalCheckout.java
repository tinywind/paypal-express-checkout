package org.tinywind.paypalexpresscheckout.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tinywind on 2016-07-15.
 */
public abstract class PaypalCheckout {
    protected static final Map<String, String> PAYPAL_VARIABLE_NAME_CONVERT_MAP = new HashMap<>();
    protected static final Map<String, String> PAYPAL_VARIABLE_NAME_REVERSE_CONVERT_MAP = new HashMap<>();

    public static String checkoutVarName(String paypalVarName) {
        return PAYPAL_VARIABLE_NAME_CONVERT_MAP.get(paypalVarName);
    }

    public static String paypalVarName(String checkoutVarName) {
        return PAYPAL_VARIABLE_NAME_REVERSE_CONVERT_MAP.get(checkoutVarName);
    }
}
