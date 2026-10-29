package org.tinywind.paypalexpresscheckout;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;

import java.net.CookieManager;
import java.net.URI;
import java.net.ServerSocket;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LauncherTest {
    @Test
    void rendersTheCheckoutLandingPage() throws Exception {
        try (ConfigurableApplicationContext context = SpringApplication.run(Launcher.class, "--server.port=0")) {
            String port = context.getEnvironment().getProperty("local.server.port");
            HttpResponse<String> response = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), response.body());
            assertTrue(response.body().contains("DIGITAL SLR CAMERA"), response.body());
        }
    }

    @Test
    void rejectsTamperedOffersAndCallbacksBeforeAnyPaymentRequest() throws Exception {
        try (ConfigurableApplicationContext context = SpringApplication.run(Launcher.class, "--server.port=0")) {
            int unusedPort;
            try (ServerSocket socket = new ServerSocket(0)) { unusedPort = socket.getLocalPort(); }
            context.getBean(PaypalConfig.class).setGvApiEndpoint("http://127.0.0.1:" + unusedPort);
            String base = "http://localhost:" + context.getEnvironment().getProperty("local.server.port");
            HttpClient client = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
            String offer = "quantity=1&itemAmount=10&taxAmount=2&shippingAmount=5&handlingAmount=1"
                    + "&shippingDiscount=-3&insuranceAmount=2&currencyCode=USD&paymentType=Sale";
            for (String changed : new String[]{offer.replace("itemAmount=10", "itemAmount=NaN"),
                    offer.replace("itemAmount=10", "itemAmount=Infinity"),
                    offer.replace("itemAmount=10", "itemAmount=0.01"),
                    offer.replace("quantity=1", "quantity=2"),
                    offer.replace("quantity=1", "quantity=-1"),
                    offer.replace("shippingDiscount=-3", "shippingDiscount=3"),
                    offer.replace("taxAmount=2", "taxAmount=0.001")}) {
                HttpResponse<String> response = client.send(HttpRequest.newBuilder(URI.create(base + "/checkout?shortcut=true"))
                        .timeout(java.time.Duration.ofSeconds(5))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(changed)).build(), HttpResponse.BodyHandlers.ofString());
                assertEquals(400, response.statusCode(), response.body());
            }
            HttpResponse<String> staged = client.send(HttpRequest.newBuilder(URI.create(base + "/checkout?shortcut=false"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(offer + "&token=client-token&payerId=client-payer&totalAmount=0.01"))
                    .build(), HttpResponse.BodyHandlers.ofString());
            assertEquals(200, staged.statusCode(), staged.body());
            HttpResponse<String> secondStage = client.send(HttpRequest.newBuilder(URI.create(base + "/checkout?shortcut=false"))
                    .timeout(java.time.Duration.ofSeconds(5))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("itemAmount=0.01")).build(), HttpResponse.BodyHandlers.ofString());
            assertEquals(400, secondStage.statusCode(), secondStage.body());
            HttpResponse<String> callback = client.send(HttpRequest.newBuilder(
                            URI.create(base + "/return?token=client-token&totalAmount=0.01"))
                    .timeout(java.time.Duration.ofSeconds(5)).GET().build(), HttpResponse.BodyHandlers.ofString());
            assertEquals(400, callback.statusCode(), callback.body());
            assertEquals(400, client.send(HttpRequest.newBuilder(URI.create(base + "/return"))
                    .GET().build(), HttpResponse.BodyHandlers.ofString()).statusCode());
        }
    }
}
