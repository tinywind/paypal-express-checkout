package org.tinywind.paypalexpresscheckout.controller;

import com.paypal.PayPal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by tinywind on 2016-07-13.
 */
@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping("cancel")
    public String cancelPage(HttpSession session) {
        session.invalidate();
        return "cancel";
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String indexPage(Model model) {
        PayPal payPal = new PayPal();
        model.addAttribute("gvApiUserName", payPal.getGvAPIUserName());
        model.addAttribute("environment", payPal.getEnvironment());
        return "checkout";
    }

    @RequestMapping(value = "review", method = RequestMethod.GET)
    public String reviewPage(Model model, HttpServletRequest request) {
        Map result = (Map) request.getAttribute("result");
        model.addAttribute("result", result);
        return "review";
    }

    @RequestMapping(value = "return", method = RequestMethod.GET)
    public String returnPage(Model model, HttpServletRequest request) {
        Map result = (Map) request.getAttribute("result");
        model.addAttribute("result", result);

        if ((request.getAttribute("ack").equals("SUCCESS") || request.getAttribute("ack").equals("SUCCESSWITHWARNING")) && request.getAttribute("payment_method").equals("credit_card")) {
            model.addAttribute("showResult", 1);
        } else if ((request.getAttribute("ack").equals("SUCCESS") || request.getAttribute("ack").equals("SUCCESSWITHWARNING"))) {
            model.addAttribute("showResult", 2);
        }

        return "return";
    }

    @RequestMapping(value = "lightboxreturn", method = RequestMethod.GET)
    public String lightboxreturnPage(Model model, HttpServletRequest request) {
        String url = "";
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            url += entry.getKey() + "=" + request.getParameter(entry.getKey()) + "&";
        }
        model.addAttribute("url", url);
        return "lightboxreturn";
    }

    @RequestMapping(value = "checkout", method = RequestMethod.GET)
    public String checkoutPage(Model model) {
        PayPal payPal = new PayPal();
        model.addAttribute("gvApiUserName", payPal.getGvAPIUserName());
        model.addAttribute("environment", payPal.getEnvironment());
        return "checkout";
    }

    @RequestMapping(value = "checkout", method = RequestMethod.POST)
    public String checkoutPostPage() {
        // todo
        return null;
    }
}
