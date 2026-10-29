package org.tinywind.paypalexpresscheckout;

import org.junit.jupiter.api.Test;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaypalConfigTest {
    @Test
    void readsSandboxConfigurationAndScalarValues() throws Exception {
        PaypalConfig config = new PaypalConfig();
        config.init();
        assertEquals("sandbox", config.getEnvironment());
        assertEquals("109.0", config.getGvVersion());
        assertEquals(false, config.getUserActionFlag());
        assertEquals("https://api-3t.sandbox.paypal.com/nvp", config.getGvApiEndpoint());
        assertNotNull(config.getGvApiUserName());
        assertNotNull(config.getGvApiPassword());
        assertNotNull(config.getGvApiSignature());
    }
}
