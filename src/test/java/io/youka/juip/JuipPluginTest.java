package io.youka.juip;

import org.junit.jupiter.api.Test;

/*
Expected output to console:
---
Enter io.youka.juip.JuipPluginTest.<init>
Test init
Leave io.youka.juip.JuipPluginTest.<init>
Enter io.youka.juip.JuipPluginTest.testPrintEnterLeave
Test method 1
Leave io.youka.juip.JuipPluginTest.testPrintEnterLeave
Enter io.youka.juip.JuipPluginTest.<init>
Test init
Leave io.youka.juip.JuipPluginTest.<init>
Test method 2
---
 */
public class JuipPluginTest {
    @PrintEnterLeave
    public JuipPluginTest() {
        System.out.println("Test init");
    }

    @Test
    @PrintEnterLeave
    public void testPrintEnterLeave() {
        System.out.println("Test method 1");
    }

    @Test
    public void testNone() {
        System.out.println("Test method 2");
    }
}
