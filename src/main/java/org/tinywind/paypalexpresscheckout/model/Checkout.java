package org.tinywind.paypalexpresscheckout.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tinywind on 2016-07-14.
 */
@Data
public class Checkout {
    private static final Map<String, String> PAYPAL_VARIABLE_NAME_CONVERT_MAP = new HashMap<>();
    private static final Map<String, String> PAYPAL_VARIABLE_NAME_REVERSE_CONVERT_MAP = new HashMap<>();
    private static final Map<String, CurrencyCodeType> CURRENCY_CODE_TYPE_OPTIONS = new HashMap<>();
    private static final Map<String, PaymentType> PAYMENT_TYPE_OPTIONS = new HashMap<>();
    private static final Map<String, CountryType> COUNTRY_TYPE_OPTIONS = new HashMap<>();

    static {
        for (CurrencyCodeType type : CurrencyCodeType.values()) CURRENCY_CODE_TYPE_OPTIONS.put(type.getString(), type);
        for (PaymentType type : PaymentType.values()) PAYMENT_TYPE_OPTIONS.put(type.getString(), type);
        for (CountryType type : CountryType.values()) COUNTRY_TYPE_OPTIONS.put(type.getString(), type);


        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("token", "token");

        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_NAME0", "productName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_NUMBER0", "orderNumber");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_DESC0", "productDescription");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_0_QTY0", "quantity");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_ITEMAMT", "itemAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_TAXAMT", "taxAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPPINGAMT", "shippingAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_HANDLINGAMT", "handlingAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPDISCAMT", "shippingDiscount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_INSURANCEAMT", "insuranceAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_AMT", "totalAmount");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("LOGOIMG", "logoImage");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_CURRENCYCODE", "currencyCodeType");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_PAYMENTACTION", "paymentType");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_FIRSTNAME", "firstName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("L_PAYMENTREQUEST_LASTNAME", "lastName");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOSTREET", "address1");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOSTREET2", "address2");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOCITY", "city");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOSTATE", "state");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOZIP", "zipCode");
        PAYPAL_VARIABLE_NAME_CONVERT_MAP.put("PAYMENTREQUEST_0_SHIPTOCOUNTRY", "country");


        PAYPAL_VARIABLE_NAME_CONVERT_MAP.forEach((k, v) -> PAYPAL_VARIABLE_NAME_REVERSE_CONVERT_MAP.put(v, k));
    }

    private String productName;
    private String orderNumber;
    private String productDescription;
    private Integer quantity;
    private Double itemAmount;
    private Double taxAmount;
    private Double shippingAmount;
    private Double handlingAmount;
    private Double shippingDiscount;
    private Double insuranceAmount;
    private Double totalAmount;
    private String logoImage;
    private CurrencyCodeType currencyCodeType;
    private PaymentType paymentType;
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private CountryType country;



    private String confirm;
    private String checkout;
    private String token;
    private String payer_id;
    private String shippingAmt;
    private String handlingAmt;
    private String shippingDiscAmt;
    private String insuranceAmt;

    public String getPaypalVarName(String paypalVarName) {
        return PAYPAL_VARIABLE_NAME_CONVERT_MAP.get(paypalVarName);
    }

    public String getCheckoutVarName(String checkoutVarName) {
        return PAYPAL_VARIABLE_NAME_REVERSE_CONVERT_MAP.get(checkoutVarName);
    }

    public Map<String, CurrencyCodeType> getCurrencyCodeTypeOptions() {
        return CURRENCY_CODE_TYPE_OPTIONS;
    }

    public Map<String, PaymentType> getPaymentTypeOptions() {
        return PAYMENT_TYPE_OPTIONS;
    }

    public Map<String, CountryType> getCountryTypeOptions() {
        return COUNTRY_TYPE_OPTIONS;
    }

    public void set(Checkout checkout) {
        final Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Object value = field.get(checkout);
                if (field.getType().equals(java.lang.String.class)) {
                    if (!StringUtils.isEmpty((String) value))
                        field.set(this, value);
                } else {
                    if (value != null)
                        field.set(this, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private enum CurrencyCodeType {
        USD("USD"), AUD("AUD"), BRL("BRL"), CAD("CAD"), CZK("CZK"), DKK("DKK"), EUR("EUR"), HKD("HKD"), HUF("HUF"), ILS("ILS"), JPY("JPY"), NOK("NOK"), MXN("MXN"), NZD("NZD"), PHP("PHP"), PLN("PLN"), GBP("GBP"), SGD("SGD"), SEK("SEK"), CHF("CHF"), TWD("TWD"), THB("THB"), TRY("TRY");
        String string;

        CurrencyCodeType(String string) {
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

    private enum CountryType {
        AF("Afghanistan"),
        AX("Åland Islands"),
        AL("Albania"),
        DZ("Algeria"),
        AS("American Samoa"),
        AD("Andorra"),
        AO("Angola"),
        AI("Anguilla"),
        AQ("Antarctica"),
        AG("Antigua and Barbuda"),
        AR("Argentina"),
        AM("Armenia"),
        AW("Aruba"),
        AU("Australia"),
        AT("Austria"),
        AZ("Azerbaijan"),
        BS("Bahamas"),
        BH("Bahrain"),
        BD("Bangladesh"),
        BB("Barbados"),
        BY("Belarus"),
        BE("Belgium"),
        BZ("Belize"),
        BJ("Benin"),
        BM("Bermuda"),
        BT("Bhutan"),
        BO("Bolivia"),
        BA("Bosnia and Herzegovina"),
        BW("Botswana"),
        BV("Bouvet Island"),
        BR("Brazil"),
        IO("British Indian Ocean Territory"),
        BN("Brunei Darussalam"),
        BG("Bulgaria"),
        BF("Burkina Faso"),
        BI("Burundi"),
        KH("Cambodia"),
        CM("Cameroon"),
        CA("Canada"),
        CV("Cape Verde"),
        KY("Cayman Islands"),
        CF("Central African Republic"),
        TD("Chad"),
        CL("Chile"),
        CN("China"),
        CX("Christmas Island"),
        CC("Cocos (Keeling) Islands"),
        CO("Colombia"),
        KM("Comoros"),
        CG("Congo"),
        CD("Congo, The Democratic Republic of The"),
        CK("Cook Islands"),
        CR("Costa Rica"),
        CI("Cote D'ivoire"),
        HR("Croatia"),
        CU("Cuba"),
        CY("Cyprus"),
        CZ("Czech Republic"),
        DK("Denmark"),
        DJ("Djibouti"),
        DM("Dominica"),
        DO("Dominican Republic"),
        EC("Ecuador"),
        EG("Egypt"),
        SV("El Salvador"),
        GQ("Equatorial Guinea"),
        ER("Eritrea"),
        EE("Estonia"),
        ET("Ethiopia"),
        FK("Falkland Islands (Malvinas)"),
        FO("Faroe Islands"),
        FJ("Fiji"),
        FI("Finland"),
        FR("France"),
        GF("French Guiana"),
        PF("French Polynesia"),
        TF("French Southern Territories"),
        GA("Gabon"),
        GM("Gambia"),
        GE("Georgia"),
        DE("Germany"),
        GH("Ghana"),
        GI("Gibraltar"),
        GR("Greece"),
        GL("Greenland"),
        GD("Grenada"),
        GP("Guadeloupe"),
        GU("Guam"),
        GT("Guatemala"),
        GG("Guernsey"),
        GN("Guinea"),
        GW("Guinea-bissau"),
        GY("Guyana"),
        HT("Haiti"),
        HM("Heard Island and Mcdonald Islands"),
        VA("Holy See (Vatican City State)"),
        HN("Honduras"),
        HK("Hong Kong"),
        HU("Hungary"),
        IS("Iceland"),
        IN("India"),
        ID("Indonesia"),
        IR("Iran, Islamic Republic of"),
        IQ("Iraq"),
        IE("Ireland"),
        IM("Isle of Man"),
        IL("Israel"),
        IT("Italy"),
        JM("Jamaica"),
        JP("Japan"),
        JE("Jersey"),
        JO("Jordan"),
        KZ("Kazakhstan"),
        KE("Kenya"),
        KI("Kiribati"),
        KP("Korea, Democratic People's Republic of"),
        KR("Korea, Republic of"),
        KW("Kuwait"),
        KG("Kyrgyzstan"),
        LA("Lao People's Democratic Republic"),
        LV("Latvia"),
        LB("Lebanon"),
        LS("Lesotho"),
        LR("Liberia"),
        LY("Libyan Arab Jamahiriya"),
        LI("Liechtenstein"),
        LT("Lithuania"),
        LU("Luxembourg"),
        MO("Macao"),
        MK("Macedonia, The Former Yugoslav Republic of"),
        MG("Madagascar"),
        MW("Malawi"),
        MY("Malaysia"),
        MV("Maldives"),
        ML("Mali"),
        MT("Malta"),
        MH("Marshall Islands"),
        MQ("Martinique"),
        MR("Mauritania"),
        MU("Mauritius"),
        YT("Mayotte"),
        MX("Mexico"),
        FM("Micronesia, Federated States of"),
        MD("Moldova, Republic of"),
        MC("Monaco"),
        MN("Mongolia"),
        ME("Montenegro"),
        MS("Montserrat"),
        MA("Morocco"),
        MZ("Mozambique"),
        MM("Myanmar"),
        NA("Namibia"),
        NR("Nauru"),
        NP("Nepal"),
        NL("Netherlands"),
        AN("Netherlands Antilles"),
        NC("New Caledonia"),
        NZ("New Zealand"),
        NI("Nicaragua"),
        NE("Niger"),
        NG("Nigeria"),
        NU("Niue"),
        NF("Norfolk Island"),
        MP("Northern Mariana Islands"),
        NO("Norway"),
        OM("Oman"),
        PK("Pakistan"),
        PW("Palau"),
        PS("Palestinian Territory, Occupied"),
        PA("Panama"),
        PG("Papua New Guinea"),
        PY("Paraguay"),
        PE("Peru"),
        PH("Philippines"),
        PN("Pitcairn"),
        PL("Poland"),
        PT("Portugal"),
        PR("Puerto Rico"),
        QA("Qatar"),
        RE("Reunion"),
        RO("Romania"),
        RU("Russian Federation"),
        RW("Rwanda"),
        SH("Saint Helena"),
        KN("Saint Kitts and Nevis"),
        LC("Saint Lucia"),
        PM("Saint Pierre and Miquelon"),
        VC("Saint Vincent and The Grenadines"),
        WS("Samoa"),
        SM("San Marino"),
        ST("Sao Tome and Principe"),
        SA("Saudi Arabia"),
        SN("Senegal"),
        RS("Serbia"),
        SC("Seychelles"),
        SL("Sierra Leone"),
        SG("Singapore"),
        SK("Slovakia"),
        SI("Slovenia"),
        SB("Solomon Islands"),
        SO("Somalia"),
        ZA("South Africa"),
        GS("South Georgia and The South Sandwich Islands"),
        ES("Spain"),
        LK("Sri Lanka"),
        SD("Sudan"),
        SR("Suriname"),
        SJ("Svalbard and Jan Mayen"),
        SZ("Swaziland"),
        SE("Sweden"),
        CH("Switzerland"),
        SY("Syrian Arab Republic"),
        TW("Taiwan, Province of China"),
        TJ("Tajikistan"),
        TZ("Tanzania, United Republic of"),
        TH("Thailand"),
        TL("Timor-leste"),
        TG("Togo"),
        TK("Tokelau"),
        TO("Tonga"),
        TT("Trinidad and Tobago"),
        TN("Tunisia"),
        TR("Turkey"),
        TM("Turkmenistan"),
        TC("Turks and Caicos Islands"),
        TV("Tuvalu"),
        UG("Uganda"),
        UA("Ukraine"),
        AE("United Arab Emirates"),
        GB("United Kingdom"),
        US("United States"),
        UM("United States Minor Outlying Islands"),
        UY("Uruguay"),
        UZ("Uzbekistan"),
        VU("Vanuatu"),
        VE("Venezuela"),
        VN("Viet Nam"),
        VG("Virgin Islands, British"),
        VI("Virgin Islands, U.S."),
        WF("Wallis and Futuna"),
        EH("Western Sahara"),
        YE("Yemen"),
        ZM("Zambia"),
        ZW("Zimbabwe");
        String string;

        CountryType(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }
}
