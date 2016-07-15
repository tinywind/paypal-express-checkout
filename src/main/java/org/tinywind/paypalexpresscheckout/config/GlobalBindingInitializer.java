package org.tinywind.paypalexpresscheckout.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.tinywind.paypalexpresscheckout.util.TagExtender;

@ControllerAdvice
public class GlobalBindingInitializer {

    @Autowired
    private TagExtender tagExtender;

    @Autowired
    private PaypalConfig paypal;

    @ModelAttribute
    public TagExtender tagExtender() {
        return tagExtender;
    }

    @ModelAttribute
    public PaypalConfig paypal() {
        return paypal;
    }
}
