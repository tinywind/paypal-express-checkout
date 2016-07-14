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
import org.tinywind.paypalexpresscheckout.service.PaypalCommunicationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * Created by tinywind on 2016-07-13.
 */
@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private PaypalConfig paypal;

    @Autowired
    private PaypalCommunicationService paypalService;

    @RequestMapping("index")
    public String indexPage(Model model, @ModelAttribute Checkout checkout) {
        model.addAttribute("gvApiUserName", paypal.getGvAPIUserName());
        model.addAttribute("environment", paypal.getEnvironment());
        return "checkout";
    }

    @RequestMapping(value = "checkout", method = RequestMethod.GET)
    public String checkoutPage(Model model) {
        model.addAttribute("gvApiUserName", paypal.getGvAPIUserName());
        model.addAttribute("environment", paypal.getEnvironment());
        return "checkout";
    }

    @RequestMapping(value = "checkout", method = RequestMethod.POST)
    public String checkoutPostPage(HttpSession session, Model model, @ModelAttribute Checkout checkout) {
        if (StringUtils.isEmpty(checkout.getConfirm()) && !StringUtils.isEmpty(checkout.getCheckout())) {
            session.setAttribute("checkoutDetails", checkout);
            session.setAttribute("EXPRESS_MARK", "ECMark");
            return checkoutPage(model);
        }

        String returnURL = "/return?page=" + (paypal.getUserActionFlag().equals("true") ? "return" : "review");
        String cancelURL = "/cancel";

        if (Objects.equals("ECMark", session.getAttribute("EXPRESS_MARK"))) {
            returnURL = "/lightboxreturn";
            checkout.set((Checkout) session.getAttribute("checkoutDetails"));
            if (checkout.getShippingAmount() != null) {
                if (checkout.getShippingAmount().compareTo(new BigDecimal(0)) > 0)
                    checkout.setPAYMENTREQUEST_0_AMT(checkout.getPAYMENTREQUEST_0_AMT().add(checkout.getShippingAmount()).subtract(checkout.getPAYMENTREQUEST_0_SHIPPINGAMT()));
                checkout.setPAYMENTREQUEST_0_SHIPPINGAMT(checkout.getShippingAmount());
            }
        } else {
            session.invalidate();
        }
        session.setAttribute("checkoutDetails", checkout);

        Checkout nvp = paypalService.callShortcutExpressCheckout(checkout, returnURL, cancelURL);
        String strAck = nvp.get("ACK");
        if (strAck != null && (strAck.equalsIgnoreCase("SUCCESS") || strAck.equalsIgnoreCase("SUCCESSWITHWARNING"))) {
            session.setAttribute("TOKEN", nvp.get("token"));
            String payPalURL = paypal.getPaypalUrl() + nvp.get("token");
            if (!StringUtils.isEmpty((String) session.getAttribute("EXPRESS_MARK")) && session.getAttribute("EXPRESS_MARK").equals("ECMark")
                    || (paypal.getUserActionFlag().equalsIgnoreCase("true")))
                payPalURL += "&useraction=commit";
            return "redirect:/" + payPalURL;
        }

        return errorPage(model, "SetExpressCheckout API call failed. " +
                "<br>Detailed Error Message: " + nvp.get("L_LONGMESSAGE0") +
                "<br>Short Error Message: " + nvp.get("L_SHORTMESSAGE0") +
                "<br>Error Code: " + nvp.get("L_ERRORCODE0") +
                "<br>Error Severity Code: " + nvp.get("L_SEVERITYCODE0"));
    }

    @RequestMapping("return")
    public String returnPage(HttpSession session, Model model, HttpServletRequest request, Checkout checkout, String page) {
        if (!StringUtils.isEmpty(checkout.getPayer_id())) session.setAttribute("player_id", checkout.getPayer_id());
        if (!StringUtils.isEmpty(checkout.getToken())) session.setAttribute("TOKEN", checkout.getToken());
        else checkout.setToken((String) session.getAttribute("token"));

        Checkout result = new Checkout();
        if (!StringUtils.isEmpty(checkout.getToken())) {
            Checkout results = paypalService.getShippingDetails(checkout.getToken());
            String strAck = results.get("ACK");
            if (strAck != null && (strAck.equalsIgnoreCase("SUCCESS") || strAck.equalsIgnoreCase("SUCCESSWITHWARNING"))) {
                session.setAttribute("payer_id", results.get("PAYERID"));
                result.putAll(results);
            } else {
                return errorPage(model, "SetExpressCheckout API call failed. " +
                        "<br>Detailed Error Message: " + results.get("L_LONGMESSAGE0") +
                        "<br>Short Error Message: " + results.get("L_SHORTMESSAGE0") +
                        "<br>Error Code: " + results.get("L_ERRORCODE0") +
                        "<br>Error Severity Code: " + results.get("L_SEVERITYCODE0"));
            }
        }

        Checkout checkoutDetails = (Checkout) session.getAttribute("checkoutDetails");
        checkoutDetails.set(checkout);
        checkout = checkoutDetails;
        checkout.setToken(checkout.getToken());
        checkout.setPayer_id((String) session.getAttribute("payer_id"));

        result.setPAYMENTREQUEST_0_SHIPPINGAMT(checkout.getPAYMENTREQUEST_0_SHIPPINGAMT());
        if (checkout.getShippingAmount() != null) {
            if (checkout.getPAYMENTREQUEST_0_SHIPPINGAMT().compareTo(new BigDecimal(0)) > 0)
                checkout.setPAYMENTREQUEST_0_AMT(checkout.getPAYMENTREQUEST_0_AMT().add(checkout.getShippingAmount()).subtract(checkout.getPAYMENTREQUEST_0_SHIPPINGAMT()));
            checkout.setPAYMENTREQUEST_0_SHIPPINGAMT(checkout.getShippingAmount());
        }

        model.addAttribute("result", result);

        if ((request.getAttribute("ack").equals("SUCCESS") || request.getAttribute("ack").equals("SUCCESSWITHWARNING")) && request.getAttribute("payment_method").equals("credit_card")) {
            model.addAttribute("showResult", 1);
        } else if ((request.getAttribute("ack").equals("SUCCESS") || request.getAttribute("ack").equals("SUCCESSWITHWARNING"))) {
            model.addAttribute("showResult", 2);
        }

        if (Objects.equals("return", page)) {
            Checkout results = paypalService.confirmPayment(checkoutDetails, request.getServerName());
            request.setAttribute("payment_method", "");
            String strAck = results.get("ACK").toString().toUpperCase();
            if (strAck == null || (!strAck.equalsIgnoreCase("SUCCESS") && !strAck.equalsIgnoreCase("SUCCESSWITHWARNING"))) {
                return errorPage(model, "SetExpressCheckout API call failed. " +
                        "<br>Detailed Error Message: " + results.get("L_LONGMESSAGE0") +
                        "<br>Short Error Message: " + results.get("L_SHORTMESSAGE0") +
                        "<br>Error Code: " + results.get("L_ERRORCODE0") +
                        "<br>Error Severity Code: " + results.get("L_SEVERITYCODE0"));
            }

            result.putAll(results);
            result.putAll(checkoutDetails);
            request.setAttribute("ack", strAck);
            session.invalidate();

            return "return";
        }

        return "review";
    }

    @RequestMapping(value = "lightboxreturn", method = RequestMethod.GET)
    public String lightBoxReturnPage(Model model, HttpServletRequest request) {
        String url = "";
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            url += entry.getKey() + "=" + request.getParameter(entry.getKey()) + "&";
        }
        model.addAttribute("url", url);
        return "lightboxreturn";
    }

    @RequestMapping("error")
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
