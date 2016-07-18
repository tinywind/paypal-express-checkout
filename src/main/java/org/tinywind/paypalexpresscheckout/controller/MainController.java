package org.tinywind.paypalexpresscheckout.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private static final String SESSION_CHECKOUT = "checkoutDetails";

    @Autowired
    private PaypalConfig paypalConfig;

    @Autowired
    private PaypalCommunicationService paypalService;

    @RequestMapping("")
    public String indexPage(@ModelAttribute Checkout checkout) {
        return "index";
    }

    @RequestMapping(value = "checkout", method = RequestMethod.GET)
    public String checkoutPage(@ModelAttribute CheckoutRequest checkoutRequest) {
        return "checkout";
    }

    @RequestMapping(value = "checkout", method = RequestMethod.POST)
    public String checkoutPostPage(HttpServletRequest request, HttpSession session, Model model, @ModelAttribute CheckoutRequest checkoutRequest, @RequestParam Boolean shortcut) {
        final Checkout backup = (Checkout) session.getAttribute(SESSION_CHECKOUT);
        if (!shortcut && backup == null) {
            session.setAttribute(SESSION_CHECKOUT, checkoutRequest);
            return checkoutPage(checkoutRequest);
        }

//        String returnURL = "/return?page=" + (paypalConfig.getUserActionFlag() ? "return" : "review");
        final String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        final String cancelURL = basePath + "/cancel";
        final String returnURL = basePath + "/lightboxreturn";
        final String logoImage = basePath + "/img/logo.jpg";

        checkoutRequest.set(backup);
        session.setAttribute(SESSION_CHECKOUT, checkoutRequest);
        checkoutRequest.calculateTotalAmount();
        checkoutRequest.setLogoImage(logoImage);
        logger.trace(checkoutRequest.toString());
        final CheckoutResponse response = (shortcut
                ? paypalService.callShortcutExpressCheckout(checkoutRequest, returnURL, cancelURL)
                : paypalService.callMarkExpressCheckout(checkoutRequest, returnURL, cancelURL));
        logger.trace(response.toString());

        if (!response.isAck()) return errorPage(model, response);

        checkoutRequest.setToken(response.getToken());
        return "redirect:" + paypalConfig.getPaypalUrl() + response.getToken() + (paypalConfig.getUserActionFlag() ? "&useraction=commit" : "");
    }

    @RequestMapping("return")
    public String returnPage(HttpSession session, Model model, HttpServletRequest request, CheckoutRequest checkoutRequest, String page) {
        if (!StringUtils.isEmpty(checkoutRequest.getToken())) {
            logger.trace(checkoutRequest.toString());
            final CheckoutResponse response = paypalService.getShippingDetails(checkoutRequest.getToken());
            logger.trace(response.toString());
            model.addAttribute("response", response);

            if (!response.isAck()) return errorPage(model, response);
            checkoutRequest.setPayerId(response.getPayerId());
        }

        CheckoutRequest checkoutBackup = (CheckoutRequest) session.getAttribute(SESSION_CHECKOUT);
        checkoutBackup.set(checkoutRequest);
        checkoutRequest = checkoutBackup;
        model.addAttribute("checkoutRequest", checkoutRequest);

        if (Objects.equals("return", page)) {
            logger.trace(checkoutRequest.toString());
            final CheckoutResponse response = paypalService.confirmPayment(checkoutRequest, request.getServerName());
            logger.trace(response.toString());

            if (!response.isAck()) return errorPage(model, response);

            model.addAttribute("checkoutResponse", response);
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
