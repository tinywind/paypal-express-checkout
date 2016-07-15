package org.tinywind.paypalexpresscheckout.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by tinywind on 2016-07-14.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CheckoutResponse extends PaypalCheckout {
    static {
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("ACK", "acknowledgement");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("TOKEN", "token");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_LONGMESSAGE0", "longMessage");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_SHORTMESSAGE0", "shortMessage");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_ERRORCODE0", "errorCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_SEVERITYCODE0", "severityCode");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("EMAIL", "email");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYERID", "payerId");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYERSTATUS", "payerStatus");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("FIRSTNAME", "firstName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("LASTNAME", "lastName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTONAME", "shipToName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOSTREET", "shipToStreet");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOCITY", "shipToCity");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOSTATE", "shipToState");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE", "shipToCountryCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOZIP", "shipToZip");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("ADDRESSSTATUS", "addressStatus");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_AMT", "totalAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("CURRENCYCODE", "currencyCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("TRANSACTIONID", "transactionId");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("AMT", "paymentTotalAmount");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("credit_card_first_name", "creditCardFirstName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("credit_card_last_name", "creditCardLastName");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_TRANSACTIONID", "transactionId2");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_TRANSACTIONTYPE", "transactionType");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_AMT", "paymentTotalAmount2");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_CURRENCYCODE", "currencyCode2");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_PAYMENTSTATUS", "paymentStatus");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_PAYMENTTYPE", "paymentType");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.forEach((k, v) -> PAYPAL_VARIABLE_NAME_REVERSE_CONVERT_MAP.put(v, k));
    }

    protected AcknowledgementType acknowledgement;
    protected String token;
    protected String longMessage;
    protected String shortMessage;
    protected String errorCode;
    protected String severityCode;

    protected String email;
    protected String payerId;
    protected String payerStatus;
    protected String firstName;
    protected String lastName;
    protected String shipToName;
    protected String shipToStreet;
    protected String shipToCity;
    protected String shipToState;
    protected String shipToCountryCode;
    protected String shipToZip;
    protected String addressStatus;
    protected String totalAmount;
    protected String currencyCode;
    protected String currencyCode2;

    protected String creditCardFirstName;
    protected String creditCardLastName;
    protected String transactionId;
    protected String transactionId2;
    protected String transactionType;
    protected Double paymentTotalAmount;
    protected Double paymentTotalAmount2;
    protected String paymentStatus;
    protected String paymentType;

    public boolean isAck() {
        return acknowledgement != null
//                && (acknowledgement != AcknowledgementType.SUCCESS || acknowledgement != AcknowledgementType.SUCCESS_WITH_WARNING)
                ;
    }

    private enum AcknowledgementType {
        SUCCESS("SUCCESS"), SUCCESS_WITH_WARNING("SUCCESSWITHWARNING");
        String string;

        AcknowledgementType(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }
}
