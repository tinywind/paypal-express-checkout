package org.tinywind.paypalexpresscheckout.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;
import org.tinywind.paypalexpresscheckout.config.PaypalProperty;
import org.tinywind.paypalexpresscheckout.model.Checkout;
import org.tinywind.paypalexpresscheckout.model.CheckoutRequest;
import org.tinywind.paypalexpresscheckout.model.CheckoutResponse;
import org.tinywind.paypalexpresscheckout.service.PaypalCommunicationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

import static org.tinywind.paypalexpresscheckout.util.UrlQueryEncoder.encodeQueryParams;

/**
 * Created by tinywind on 2016-07-13.
 */
@Controller
@RequestMapping("/")
public class MainController {
    private static final String SESSION_CHECKOUT = "checkoutDetails";

    @Autowired
    private PaypalProperty paypalProperty;
    @Autowired
    private PaypalConfig paypal;
    @Autowired
    private PaypalCommunicationService paypalService;

    @RequestMapping("")
    public String indexPage(@ModelAttribute Checkout checkout) {
        return "checkout";
    }

    @RequestMapping(value = "checkout-base", method = RequestMethod.POST)
    public String checkoutBasePostPage(HttpSession session, @ModelAttribute CheckoutRequest checkout) {
        session.setAttribute(SESSION_CHECKOUT, checkout);
        return checkoutPage(checkout);
    }

    @RequestMapping(value = "checkout", method = RequestMethod.GET)
    public String checkoutPage(@ModelAttribute CheckoutRequest checkout) {
        return "checkout";
    }

    /**
     * TODO: payerId가 checkout.jsp 페이지에서 날아올수 있다....
     */
    @RequestMapping(value = "checkout", method = RequestMethod.POST)
    public String checkoutPostPage(HttpSession session, Model model, @ModelAttribute CheckoutRequest checkout) {
//        String returnURL = "/return?page=" + (paypal.getUserActionFlag() ? "return" : "review");
        String cancelURL = "/cancel";
        String returnURL = "/lightboxreturn";

        checkout.set((Checkout) session.getAttribute(SESSION_CHECKOUT));
        if (checkout.getShippingAmount() != null)
            checkout.setTotalAmount(checkout.getTotalAmount() + (checkout.getShippingAmount() - checkout.getShippingDiscount()));

        session.setAttribute(SESSION_CHECKOUT, checkout);
        final CheckoutResponse response = paypalService.callShortcutExpressCheckout(checkout, returnURL, cancelURL);
        System.out.println(response.toString());

        if (!response.isAck()) return errorPage(model, response);

        checkout.setToken(response.getToken());
        // FIXME ??
        return "redirect:/" + paypal.getPaypalUrl() + response.getToken() + (paypal.getUserActionFlag() ? "&useraction=commit" : "");
    }

    @RequestMapping("return")
    public String returnPage(HttpSession session, Model model, HttpServletRequest request, CheckoutRequest checkout, String page) {
        if (!StringUtils.isEmpty(checkout.getToken())) {
            final CheckoutResponse response = paypalService.getShippingDetails(checkout.getToken());
            System.out.println(response.toString());
            model.addAttribute("response", response);

            if (!response.isAck()) return errorPage(model, response);
            checkout.setPayerId(response.getPayerId());
        }

        CheckoutRequest checkoutBackup = (CheckoutRequest) session.getAttribute(SESSION_CHECKOUT);
        checkoutBackup.set(checkout);
        checkout = checkoutBackup;
        model.addAttribute("request", checkout);

        if (checkout.getShippingAmount() != null)
            checkout.setTotalAmount(checkout.getTotalAmount() + (checkout.getShippingAmount() - checkout.getShippingDiscount()));
        System.out.println(checkout.toString());

        if (Objects.equals("return", page)) {
            final CheckoutResponse response = paypalService.confirmPayment(checkout, request.getServerName());
            System.out.println(response.toString());

            if (!response.isAck()) return errorPage(model, response);

            model.addAttribute("response", response);

            model.addAttribute("byCreditCard", checkout.getPaymentMethod() == CheckoutRequest.PaymentMethod.CREDIT_CARD);
            session.invalidate();
            return "return";
        }

        return "review";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("lightboxreturn")
    public String lightBoxReturnPage(Model model, HttpServletRequest request) {
        model.addAttribute("url", encodeQueryParams((Map) request.getParameterMap()));
        return "lightboxreturn";
    }

    private String errorPage(Model model, CheckoutResponse error) {
        return errorPage(model, "SetExpressCheckout API call failed. " +
                "<br>Detailed Error Message: " + error.getLongMessage() +
                "<br>Short Error Message: " + error.getShortMessage() +
                "<br>Error Code: " + error.getErrorCode() +
                "<br>Error Severity Code: " + error.getSeverityCode());
    }

    //    @RequestMapping("error")
    public String errorPage(Model model, String error) {
        model.addAttribute("error", error);
        return "error";
    }

    @RequestMapping("cancel")
    public String cancelPage(HttpSession session) {
        session.invalidate();
        return "cancel";
    }
}
