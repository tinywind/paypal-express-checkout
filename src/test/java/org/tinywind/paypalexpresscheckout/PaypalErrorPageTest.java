package org.tinywind.paypalexpresscheckout;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.tinywind.paypalexpresscheckout.controller.MainController;
import org.tinywind.paypalexpresscheckout.model.CheckoutResponse;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaypalErrorPageTest {
    @Test
    void escapesProviderMessagesAndPreservesTheErrorPageMarkup() throws Exception {
        CheckoutResponse response = new CheckoutResponse();
        response.setLongMessage("<script>alert('test')</script>");
        Model model = new ExtendedModelMap();
        Method renderer = MainController.class.getDeclaredMethod("errorPage", Model.class, CheckoutResponse.class);
        renderer.setAccessible(true);
        assertEquals("error", renderer.invoke(new MainController(), model, response));
        String error = (String) model.getAttribute("error");
        assertFalse(error.contains("<script>"));
        assertTrue(error.contains("&lt;script&gt;"));
        assertTrue(error.contains("<br>Detailed Error Message:"));
    }
}
