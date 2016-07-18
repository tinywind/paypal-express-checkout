package org.tinywind.paypalexpresscheckout.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CheckoutResponse extends PaypalCheckout {
    static {
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("ACK", "acknowledgement");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_LONGMESSAGE0", "longMessage");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_SHORTMESSAGE0", "shortMessage");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_ERRORCODE0", "errorCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_SEVERITYCODE0", "severityCode");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("TOKEN", "token");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PHONENUM", "phone");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("BILLINGAGREEMENTACCEPTEDSTATUS", "billingAgreementAcceptedStatus");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("CHECKOUTSTATUS", "checkoutStatus");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("TIMESTAMP", "timestamp");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("CORRELATIONID", "correlationId");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("VERSION", "version");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("BUILD", "build");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("EMAIL", "email");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYERID", "payerId");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYERSTATUS", "payerStatus");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("FIRSTNAME", "firstName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("LASTNAME", "lastName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("COUNTRYCODE", "countryCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPTONAME", "shipToName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPTOSTREET", "shipToStreet");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPTOCITY", "shipToCity");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPTOSTATE", "shipToState");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPTOZIP", "shipToZip");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPTOCOUNTRYCODE", "shipToCountryCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPTOCOUNTRYNAME", "shipToCountryName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("BILLINGNAME", "billingName");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("STREET", "street");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("CITY", "city");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("STATE", "state");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("ZIP", "zip");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("COUNTRY", "country");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("COUNTRYNAME", "countryName");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("ADDRESSID", "addressId");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("ADDRESSSTATUS", "addressStatus");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("CURRENCYCODE", "currencyCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("AMT", "totalAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("ITEMAMT", "itemAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPPINGAMT", "shippingAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("HANDLINGAMT", "handlingAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("TAXAMT", "taxAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("INSURANCEAMT", "insuranceAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPDISCAMT", "shippingDiscount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("INSURANCEOPTIONOFFERED", "insuranceOptionOffered");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_NAME0", "lProductName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_NUMBER0", "lOrderNumber");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_QTY0", "lQuantity");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_TAXAMT0", "lTaxAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_AMT0", "lTotalAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_DESC0", "lProductDescription");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("TRANSACTIONID", "transactionId");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_TRANSACTIONID", "paidTransactionId");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_TRANSACTIONTYPE", "paidTransactionType");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_ACK", "paidAcknowledgement");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_ERRORCODE", "paidErrorCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_PAYMENTSTATUS", "paidStatus");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SUCCESSPAGEREDIRECTREQUESTED", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("INSURANCEOPTIONSELECTED", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("SHIPPINGOPTIONISDEFAULT", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_PAYMENTTYPE", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_ORDERTIME", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_AMT", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_FEEAMT", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_TAXAMT", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_CURRENCYCODE", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_PENDINGREASON", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_REASONCODE", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_PROTECTIONELIGIBILITY", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_PROTECTIONELIGIBILITYTYPE", "?");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTINFO_0_SECUREMERCHANTACCOUNTID", "?");


        PAYPAL_VARIABLE_NAME_CONVERT_MAP.forEach((k, v) -> PAYPAL_VARIABLE_NAME_REVERSE_CONVERT_MAP.put(v, k));
    }

    protected AcknowledgementType acknowledgement;
    protected String longMessage;
    protected String shortMessage;
    protected String errorCode;
    protected String severityCode;

    protected String token;
    protected String phone;
    protected Integer billingAgreementAcceptedStatus;
    protected String checkoutStatus;
    protected String timestamp;
    protected String correlationId;
    protected String version;
    protected String build;
    protected String email;
    protected String payerId;
    protected String payerStatus;
    protected String firstName;
    protected String lastName;
    protected String countryCode;
    protected String shipToName;
    protected String shipToStreet;
    protected String shipToCity;
    protected String shipToState;
    protected String shipToZip;
    protected String shipToCountryCode;
    protected String shipToCountryName;
    protected String billingName;

    protected String street;
    protected String city;
    protected String state;
    protected String zip;
    protected String country;
    protected String countryName;

    protected String addressId;
    protected String addressStatus;
    protected String currencyCode;
    protected Double totalAmount;
    protected Double itemAmount;
    protected Double shippingAmount;
    protected Double handlingAmount;
    protected Double taxAmount;
    protected Double insuranceAmount;
    protected Double shippingDiscount;
    protected Boolean insuranceOptionOffered;

    protected String lProductName;
    protected String lOrderNumber;
    protected Integer lQuantity;
    protected Double lTaxAmount;
    protected Double lTotalAmount;
    protected String lProductDescription;

    protected String transactionId;

    protected String paidTransactionId;
    protected String paidTransactionType;
    protected AcknowledgementType paidAcknowledgement;
    protected String paidErrorCode;
    protected String paidStatus;

    public boolean isAck() {
        return acknowledgement != null
                && (acknowledgement == AcknowledgementType.SUCCESS || acknowledgement == AcknowledgementType.SUCCESS_WITH_WARNING);
    }

    @SuppressWarnings("all")
    public enum AcknowledgementType {
        SUCCESS("Success"), SUCCESS_WITH_WARNING("SuccessWithWarning"), FAILURE("Failure");
        String string;

        AcknowledgementType(String string) {
            this.string = string;
        }

        public static AcknowledgementType stringOf(String s) {
            for (AcknowledgementType type : AcknowledgementType.values()) {
                if (type.string.equalsIgnoreCase(s))
                    return type;
            }
            throw new IllegalArgumentException("No enum constant for " + s);
        }

        public String getString() {
            return string;
        }
    }
}
