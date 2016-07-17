package org.tinywind.paypalexpresscheckout.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;
import org.tinywind.paypalexpresscheckout.model.Checkout;
import org.tinywind.paypalexpresscheckout.model.CheckoutRequest;
import org.tinywind.paypalexpresscheckout.model.CheckoutResponse;
import org.tinywind.paypalexpresscheckout.service.PaypalCommunicationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

import static org.tinywind.paypalexpresscheckout.util.UrlQueryEncoder.encodeQueryParams;

@Controller
public class MainController {
    private static final String SESSION_CHECKOUT = "checkoutDetails";

    @Autowired
    private PaypalConfig paypalConfig;

    @Autowired
    private PaypalCommunicationService paypalService;

    @RequestMapping("")
    public String indexPage(@ModelAttribute Checkout checkout) {
        return "index";
    }

    @RequestMapping(value = "checkout-base", method = RequestMethod.POST)
    public String checkoutBasePostPage(HttpSession session, @ModelAttribute CheckoutRequest checkoutRequest) {
        session.setAttribute(SESSION_CHECKOUT, checkoutRequest);
        return checkoutPage(checkoutRequest);
    }

    @RequestMapping(value = "checkout", method = RequestMethod.GET)
    public String checkoutPage(@ModelAttribute CheckoutRequest checkoutRequest) {
        return "checkout";
    }

    @RequestMapping(value = "checkout", method = RequestMethod.POST)
    public String checkoutPostPage(HttpSession session, Model model, @ModelAttribute CheckoutRequest checkoutRequest) {
//        String returnURL = "/return?page=" + (paypalConfig.getUserActionFlag() ? "return" : "review");
        String cancelURL = "/cancel";
        String returnURL = "/lightboxreturn";

        checkoutRequest.set((Checkout) session.getAttribute(SESSION_CHECKOUT));
        if (checkoutRequest.getShippingAmount() != null)
            checkoutRequest.setTotalAmount(checkoutRequest.getTotalAmount() + (checkoutRequest.getShippingAmount() - checkoutRequest.getShippingDiscount()));

        session.setAttribute(SESSION_CHECKOUT, checkoutRequest);
        final CheckoutResponse response = paypalService.callShortcutExpressCheckout(checkoutRequest, returnURL, cancelURL);
        System.out.println(response.toString());

        if (!response.isAck()) return errorPage(model, response);

        checkoutRequest.setToken(response.getToken());
        // FIXME ??
        return "redirect:/" + paypalConfig.getPaypalUrl() + response.getToken() + (paypalConfig.getUserActionFlag() ? "&useraction=commit" : "");
    }

    @RequestMapping("return")
    public String returnPage(HttpSession session, Model model, HttpServletRequest request, CheckoutRequest checkoutRequest, String page) {
        if (!StringUtils.isEmpty(checkoutRequest.getToken())) {
            final CheckoutResponse response = paypalService.getShippingDetails(checkoutRequest.getToken());
            System.out.println(response.toString());
            model.addAttribute("response", response);

            if (!response.isAck()) return errorPage(model, response);
            checkoutRequest.setPayerId(response.getPayerId());
        }

        CheckoutRequest checkoutBackup = (CheckoutRequest) session.getAttribute(SESSION_CHECKOUT);
        checkoutBackup.set(checkoutRequest);
        checkoutRequest = checkoutBackup;
        model.addAttribute("request", checkoutRequest);

        if (checkoutRequest.getShippingAmount() != null)
            checkoutRequest.setTotalAmount(checkoutRequest.getTotalAmount() + (checkoutRequest.getShippingAmount() - checkoutRequest.getShippingDiscount()));
        System.out.println(checkoutRequest.toString());

        if (Objects.equals("return", page)) {
            final CheckoutResponse response = paypalService.confirmPayment(checkoutRequest, request.getServerName());
            System.out.println(response.toString());

            if (!response.isAck()) return errorPage(model, response);

            model.addAttribute("response", response);

            model.addAttribute("byCreditCard", checkoutRequest.getPaymentMethod() == CheckoutRequest.PaymentMethod.CREDIT_CARD);
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

    private String errorPage(Model model, String error) {
        model.addAttribute("error", error);
        return "error";
    }

    @RequestMapping("cancel")
    public String cancelPage(HttpSession session) {
        session.invalidate();
        return "cancel";
    }
}
