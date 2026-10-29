package org.tinywind.paypalexpresscheckout.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinywind.paypalexpresscheckout.service.PaypalCommunicationService;
import org.tinywind.paypalexpresscheckout.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class Checkout extends PaypalCheckout {
    private static final Logger logger = LoggerFactory.getLogger(Checkout.class);
    protected static final Map<String, CurrencyCode> CURRENCY_CODE_OPTIONS = new HashMap<>();
    protected static final Map<String, PaymentType> PAYMENT_TYPE_OPTIONS = new HashMap<>();

    static {
        for (CurrencyCode type : CurrencyCode.values()) CURRENCY_CODE_OPTIONS.put(type.getString(), type);
        for (PaymentType type : PaymentType.values()) PAYMENT_TYPE_OPTIONS.put(type.getString(), type);

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_ITEMAMT", "itemAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_TAXAMT", "taxAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPPINGAMT", "shippingAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_HANDLINGAMT", "handlingAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPDISCAMT", "shippingDiscount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_INSURANCEAMT", "insuranceAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_AMT", "totalAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_CURRENCYCODE", "currencyCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_PAYMENTACTION", "paymentType");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_NAME0", "productName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_NUMBER0", "orderNumber");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_DESC0", "productDescription");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_QTY0", "quantity");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_AMT0", "lTotalAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("LOGOIMG", "logoImage");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYERID", "payerId");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("TOKEN", "token");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.forEach((k, v) -> PAYPAL_VARIABLE_NAME_REVERSE_CONVERT_MAP.put(v, k));
    }

    protected String logoImage;

    protected String buyerEmail;
    protected String buyerPassword;
    protected String productName;
    protected String orderNumber;
    protected String productDescription;
    protected Integer quantity;

    protected Double itemAmount;
    protected Double taxAmount;
    protected Double shippingAmount;
    protected Double handlingAmount;
    protected Double shippingDiscount;
    protected Double insuranceAmount;
    protected Double totalAmount;
    protected Double lTotalAmount;
    protected CurrencyCode currencyCode;
    protected PaymentType paymentType;

    protected String payerId; // from Paypal server
    protected String token; // from Paypal server

    public Map<String, CurrencyCode> getCurrencyCodeOptions() {
        return CURRENCY_CODE_OPTIONS;
    }

    public Map<String, PaymentType> getPaymentTypeOptions() {
        return PAYMENT_TYPE_OPTIONS;
    }

    public void calculateTotalAmount() {
        if (quantity == null || quantity <= 0) throw new IllegalArgumentException("Invalid quantity");
        BigDecimal lineTotal = amount(itemAmount, false).multiply(BigDecimal.valueOf(quantity));
        BigDecimal total = lineTotal.add(amount(taxAmount, false)).add(amount(shippingAmount, false))
                .add(amount(handlingAmount, false)).add(amount(shippingDiscount, true))
                .add(amount(insuranceAmount, false));
        if (lineTotal.precision() > 9 || total.precision() > 9 || total.signum() < 0)
            throw new IllegalArgumentException("Invalid total amount");
        lTotalAmount = lineTotal.doubleValue();
        totalAmount = total.doubleValue();
    }

    public String formatAmount(Double value) {
        return amount(value).toPlainString();
    }

    private BigDecimal amount(Double value, boolean discount) {
        BigDecimal amount = amount(value);
        if (discount ? amount.signum() > 0 : amount.signum() < 0)
            throw new IllegalArgumentException("Invalid amount sign");
        return amount;
    }

    private BigDecimal amount(Double value) {
        if (value == null || !Double.isFinite(value)) throw new IllegalArgumentException("Invalid amount");
        int decimals = currencyCode == CurrencyCode.HUF || currencyCode == CurrencyCode.JPY
                || currencyCode == CurrencyCode.TWD ? 0 : 2;
        BigDecimal amount = BigDecimal.valueOf(value);
        if (amount.abs().compareTo(new BigDecimal("999999999")) > 0
                || amount.stripTrailingZeros().scale() > decimals)
            throw new IllegalArgumentException("Invalid amount precision or range");
        amount = amount.setScale(decimals, RoundingMode.UNNECESSARY);
        if (amount.precision() > 9) throw new IllegalArgumentException("Invalid amount range");
        return amount;
    }

    public void set(Checkout checkout) {
//        for (Class<?> klass = this.getClass(); klass != null; klass = klass.getSuperclass())
        Class<?> klass = Checkout.class;
        {
            final Field[] fields = klass.getDeclaredFields();
            for (Field field : fields) {
                if ((field.getModifiers() & java.lang.reflect.Modifier.FINAL) != 0) continue;

                Object value;
                try {
                    value = ReflectionUtil.getValue(checkout, klass, field);
                } catch (Exception e) {
                    logger.debug(e.getClass().getName() + ": " + e.getMessage());
                    continue;
                }

                if ((field.getType().equals(java.lang.String.class) && !StringUtils.isEmpty((String) value))
                        || (!field.getType().equals(java.lang.String.class) && value != null)) {
                    try {
                        ReflectionUtil.setValue(this, klass, field, value);
                    } catch (Exception e) {
                        logger.debug(e.getClass().getName() + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    private enum CurrencyCode {
        USD("USD"), AUD("AUD"), BRL("BRL"), CAD("CAD"), CZK("CZK"), DKK("DKK"), EUR("EUR"), HKD("HKD"), HUF("HUF"), ILS("ILS"), JPY("JPY"), NOK("NOK"), MXN("MXN"), NZD("NZD"), PHP("PHP"), PLN("PLN"), GBP("GBP"), SGD("SGD"), SEK("SEK"), CHF("CHF"), TWD("TWD"), THB("THB"), TRY("TRY");
        String string;

        CurrencyCode(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    private enum PaymentType {
        Sale("Sale"), Authorization("Authorization"), Order("Order");
        String string;

        PaymentType(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }
}
