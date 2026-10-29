package org.tinywind.paypalexpresscheckout.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;
import org.tinywind.paypalexpresscheckout.model.Checkout;
import org.tinywind.paypalexpresscheckout.model.CheckoutRequest;
import org.tinywind.paypalexpresscheckout.model.CheckoutResponse;
import org.tinywind.paypalexpresscheckout.service.PaypalCommunicationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

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

    @RequestMapping(value = "checkout", method = RequestMethod.GET)
    public String checkoutPage(@ModelAttribute CheckoutRequest checkoutRequest) {
        return "checkout";
    }

    @RequestMapping(value = "checkout", method = RequestMethod.POST)
    public String checkoutPostPage(HttpServletRequest request, HttpSession session, Model model, @ModelAttribute CheckoutRequest checkoutRequest, @RequestParam Boolean shortcut) {
        final Checkout backup = (Checkout) session.getAttribute(SESSION_CHECKOUT);
        requireCameraOffer(checkoutRequest);
        if (backup != null) checkoutRequest.set(backup);
        checkoutRequest.setToken(null);
        checkoutRequest.setPayerId(null);
        checkoutRequest.calculateTotalAmount();
        requireCameraOffer(checkoutRequest);
        if (!shortcut && backup == null) {
            session.setAttribute(SESSION_CHECKOUT, checkoutRequest);
            return checkoutPage(checkoutRequest);
        }

        final String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        final String cancelURL = basePath + "/cancel";
        final String returnURL = basePath + "/lightboxreturn";
        final String logoImage = basePath + "/img/logo.jpg";

        session.setAttribute(SESSION_CHECKOUT, checkoutRequest);
        checkoutRequest.setLogoImage(logoImage);
        final CheckoutResponse response = (shortcut
                ? paypalService.callShortcutExpressCheckout(checkoutRequest, returnURL, cancelURL)
                : paypalService.callMarkExpressCheckout(checkoutRequest, returnURL, cancelURL));

        if (!response.isAck()) return errorPage(model, response);

        checkoutRequest.setToken(response.getToken());
        return "redirect:" + paypalConfig.getPaypalUrl() + response.getToken() + (paypalConfig.getUserActionFlag() ? "&useraction=commit" : "");
    }

    @RequestMapping("return")
    public String returnPage(HttpSession session, Model model, HttpServletRequest request, @RequestParam String token) {
        CheckoutRequest checkoutRequest = (CheckoutRequest) session.getAttribute(SESSION_CHECKOUT);
        if (checkoutRequest == null || StringUtils.isEmpty(token) || !token.equals(checkoutRequest.getToken()))
            throw new IllegalArgumentException("Invalid checkout token");
        checkoutRequest.calculateTotalAmount();
        requireCameraOffer(checkoutRequest);
        final CheckoutResponse details = paypalService.getShippingDetails(checkoutRequest.getToken());
        model.addAttribute("response", details);
        if (!details.isAck()) return errorPage(model, details);
        checkoutRequest.setPayerId(details.getPayerId());
        model.addAttribute("checkoutRequest", checkoutRequest);
        final CheckoutResponse response = paypalService.confirmPayment(checkoutRequest, request.getServerName());

        if (!response.isAck()) return errorPage(model, response);

        model.addAttribute("checkoutResponse", response);
        model.addAttribute("byCreditCard", checkoutRequest.getPaymentMethod() == CheckoutRequest.PaymentMethod.CREDIT_CARD);
        session.invalidate();
        return "return";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("lightboxreturn")
    public String lightBoxReturnPage(Model model, HttpServletRequest request) {
        model.addAttribute("url", encodeQueryParams((Map) request.getParameterMap()));
        return "lightboxreturn";
    }

    private String errorPage(Model model, CheckoutResponse error) {
        return errorPage(model, "SetExpressCheckout API call failed. " +
                "<br>Detailed Error Message: " + HtmlUtils.htmlEscape(String.valueOf(error.getLongMessage())) +
                "<br>Short Error Message: " + HtmlUtils.htmlEscape(String.valueOf(error.getShortMessage())) +
                "<br>Error Code: " + HtmlUtils.htmlEscape(String.valueOf(error.getErrorCode())) +
                "<br>Error Severity Code: " + HtmlUtils.htmlEscape(String.valueOf(error.getSeverityCode())));
    }

    private void requireCameraOffer(Checkout checkout) {
        if ((checkout.getQuantity() != null && checkout.getQuantity() != 1)
                || (checkout.getItemAmount() != null && checkout.getItemAmount() != 10d)
                || (checkout.getTaxAmount() != null && checkout.getTaxAmount() != 2d)
                || (checkout.getShippingAmount() != null && checkout.getShippingAmount() != 5d)
                || (checkout.getHandlingAmount() != null && checkout.getHandlingAmount() != 1d)
                || (checkout.getShippingDiscount() != null && checkout.getShippingDiscount() != -3d)
                || (checkout.getInsuranceAmount() != null && checkout.getInsuranceAmount() != 2d))
            throw new IllegalArgumentException("Checkout does not match the camera offer");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidCheckout(Model model) {
        return errorPage(model, "Invalid checkout parameters.");
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
