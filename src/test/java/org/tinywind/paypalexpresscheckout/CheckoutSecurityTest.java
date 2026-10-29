package org.tinywind.paypalexpresscheckout;

import org.junit.jupiter.api.Test;
import org.tinywind.paypalexpresscheckout.model.Checkout;
import org.tinywind.paypalexpresscheckout.service.PaypalCommunicationService;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CheckoutSecurityTest {
    @Test
    void preservesTheCameraOfferAndNegativeShippingDiscount() {
        Checkout checkout = offer();
        checkout.calculateTotalAmount();
        assertEquals(10d, checkout.getLTotalAmount());
        assertEquals(17d, checkout.getTotalAmount());
    }

    @Test
    void calculatesDecimalAmountsWithoutBinaryRoundingDrift() {
        Checkout checkout = offer();
        checkout.setItemAmount(0.1d);
        checkout.setQuantity(3);
        checkout.setTaxAmount(0d);
        checkout.setShippingAmount(0d);
        checkout.setHandlingAmount(0d);
        checkout.setShippingDiscount(0d);
        checkout.setInsuranceAmount(0d);
        checkout.calculateTotalAmount();
        assertEquals(0.3d, checkout.getLTotalAmount());
        assertEquals(0.3d, checkout.getTotalAmount());
    }

    @Test
    void rejectsNonFiniteAndOversizedMonetaryInputs() {
        List<BiConsumer<Checkout, Double>> setters = List.of(Checkout::setItemAmount, Checkout::setTaxAmount,
                Checkout::setShippingAmount, Checkout::setHandlingAmount, Checkout::setShippingDiscount,
                Checkout::setInsuranceAmount);
        for (BiConsumer<Checkout, Double> setter : setters) {
            for (double value : new double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
                    Double.MAX_VALUE, -Double.MAX_VALUE}) {
                Checkout checkout = offer();
                setter.accept(checkout, value);
                assertThrows(IllegalArgumentException.class, checkout::calculateTotalAmount);
            }
        }
    }

    @Test
    void rejectsInvalidQuantitiesSignsAndPrecision() {
        for (int quantity : new int[]{0, -1, Integer.MAX_VALUE}) {
            Checkout checkout = offer();
            checkout.setQuantity(quantity);
            assertThrows(IllegalArgumentException.class, checkout::calculateTotalAmount);
        }
        Checkout negative = offer();
        negative.setItemAmount(-1d);
        assertThrows(IllegalArgumentException.class, negative::calculateTotalAmount);
        Checkout discount = offer();
        discount.setShippingDiscount(1d);
        assertThrows(IllegalArgumentException.class, discount::calculateTotalAmount);
        Checkout precision = offer();
        precision.setItemAmount(0.001d);
        assertThrows(IllegalArgumentException.class, precision::calculateTotalAmount);
    }

    @Test
    void keepsUnitPricesSeparateFromItemTotalsInPaypalParameters() throws Exception {
        Checkout checkout = offer();
        checkout.setQuantity(3);
        checkout.setItemAmount(0.1d);
        checkout.calculateTotalAmount();
        Method method = PaypalCommunicationService.class.getDeclaredMethod("getSetExpressCheckoutParams",
                Checkout.class, String.class, String.class);
        method.setAccessible(true);
        Map<?, ?> params = (Map<?, ?>) method.invoke(new PaypalCommunicationService(), checkout,
                "http://127.0.0.1/return", "http://127.0.0.1/cancel");
        assertEquals("0.30", params.get("PAYMENTREQUEST_0_ITEMAMT"));
        assertEquals("0.10", params.get("L_PAYMENTREQUEST_0_AMT0"));
        assertEquals("-3.00", params.get("PAYMENTREQUEST_0_SHIPDISCAMT"));
        assertEquals("7.30", params.get("PAYMENTREQUEST_0_AMT"));
    }

    @Test
    void honorsTheExistingZeroDecimalCurrencyOptions() {
        for (String currency : new String[]{"HUF", "JPY", "TWD"}) {
            Checkout checkout = offer();
            DataBinder binder = new DataBinder(checkout);
            binder.bind(new MutablePropertyValues(Map.of("currencyCode", currency)));
            assertFalse(binder.getBindingResult().hasErrors());
            checkout.calculateTotalAmount();
            assertEquals("17", checkout.formatAmount(checkout.getTotalAmount()));
            checkout.setItemAmount(0.1d);
            assertThrows(IllegalArgumentException.class, checkout::calculateTotalAmount);
        }
    }

    private Checkout offer() {
        Checkout checkout = new Checkout();
        checkout.setQuantity(1);
        checkout.setItemAmount(10d);
        checkout.setTaxAmount(2d);
        checkout.setShippingAmount(5d);
        checkout.setHandlingAmount(1d);
        checkout.setShippingDiscount(-3d);
        checkout.setInsuranceAmount(2d);
        return checkout;
    }
}
