package org.tinywind.paypalexpresscheckout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tinywind.paypalexpresscheckout.config.PaypalConfig;
import org.tinywind.paypalexpresscheckout.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PaypalConfig.class)
public class PaypalConfigTest {
    @Autowired
    private PaypalConfig config;

    @Test
    public void testConfig() {
        final Class<PaypalConfig> klass = PaypalConfig.class;
        for (Field field : klass.getDeclaredFields()) {
            try {
                assert ReflectionUtil.getValue(config, klass, field) != null;
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}