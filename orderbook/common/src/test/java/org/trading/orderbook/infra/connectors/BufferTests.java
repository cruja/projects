package org.trading.orderbook.infra.connectors;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BufferTests {

    @Test
    public void test1() {

        List<String> messages = new ArrayList<>();
        IMessageListener listener = new IMessageListener() {
            @Override
            public void newMessage(String message) {
                messages.add(message);
            }
        };
        CharBuffer charBuffer = new CharBuffer(listener);

        String m_1 = "{10000;ob_";
        String m_2 = "1;BUY;10;102.5}";

        charBuffer.append(m_1);
        assertTrue(messages.equals(Collections.<String>emptyList()));

        charBuffer.append(m_2);
        assertTrue(messages.equals(new ArrayList<String>() {
            {
                add("10000;ob_1;BUY;10;102.5");
            }
        }));
    }

    @Test
    public void test2() {

        List<String> messages = new ArrayList<>();
        IMessageListener listener = new IMessageListener() {
            @Override
            public void newMessage(String message) {
                messages.add(message);
            }
        };
        CharBuffer charBuffer = new CharBuffer(listener);

        String message_1 = "{1;ob_1;BUY;10;102.5}{2;ob_1;BUY;10;105.0}{3;ob_1;BUY;10;107.5}{4;ob_1;BUY;10;110.0}{5;ob_1;BUY;10;112.5}{6;ob_1;BUY;10;115.0}{7;ob_1;BUY;10;117.5}{8;ob_1;BUY;10;120.0}{9;ob_1;BUY;10;122.5}{10;ob_1;BUY;10;125.0}{11;ob_1;BUY;10;127.5}{12;ob_1;BUY;10;130.0}{13;ob_1;BUY;10;132.5}{14;ob_1;BUY;10;135.0}{15;ob_1;BUY;10;137.5}{16;ob_1;BUY;10;140.0}{17;ob_1;BUY;10;142.5}{18;ob_1;BUY;10;145.0}{19;ob_1;BUY;10;147.5}{20;ob_1;BUY;10;150.0}{21;ob_1;BUY;10;152.5}{22;ob_1;BUY;10;155.0}{23;ob_1;BUY;10;157.5}{24;ob_1;BUY;10;160.0}{25;ob_1;BUY;10;162.5}{26;ob_1;BUY;10;165.0}{27;ob_1;BUY;10;167.5}{28;ob_1;BUY;10;170.0}{29;ob_1;B";
        String message_2 = "UY;10;170.0}";

        charBuffer.append(message_1);
        assertTrue(messages.equals(new ArrayList<String>() {
            {
                add("1;ob_1;BUY;10;102.5");
                add("2;ob_1;BUY;10;105.0");
                add("3;ob_1;BUY;10;107.5");
                add("4;ob_1;BUY;10;110.0");
                add("5;ob_1;BUY;10;112.5");
                add("6;ob_1;BUY;10;115.0");
                add("7;ob_1;BUY;10;117.5");
                add("8;ob_1;BUY;10;120.0");
                add("9;ob_1;BUY;10;122.5");
                add("10;ob_1;BUY;10;125.0");
                add("11;ob_1;BUY;10;127.5");
                add("12;ob_1;BUY;10;130.0");
                add("13;ob_1;BUY;10;132.5");
                add("14;ob_1;BUY;10;135.0");
                add("15;ob_1;BUY;10;137.5");
                add("16;ob_1;BUY;10;140.0");
                add("17;ob_1;BUY;10;142.5");
                add("18;ob_1;BUY;10;145.0");
                add("19;ob_1;BUY;10;147.5");
                add("20;ob_1;BUY;10;150.0");
                add("21;ob_1;BUY;10;152.5");
                add("22;ob_1;BUY;10;155.0");
                add("23;ob_1;BUY;10;157.5");
                add("24;ob_1;BUY;10;160.0");
                add("25;ob_1;BUY;10;162.5");
                add("26;ob_1;BUY;10;165.0");
                add("27;ob_1;BUY;10;167.5");
                add("28;ob_1;BUY;10;170.0");
            }
        }));


        charBuffer.append(message_2);
        assertTrue(messages.equals(new ArrayList<String>() {
            {
                add("1;ob_1;BUY;10;102.5");
                add("2;ob_1;BUY;10;105.0");
                add("3;ob_1;BUY;10;107.5");
                add("4;ob_1;BUY;10;110.0");
                add("5;ob_1;BUY;10;112.5");
                add("6;ob_1;BUY;10;115.0");
                add("7;ob_1;BUY;10;117.5");
                add("8;ob_1;BUY;10;120.0");
                add("9;ob_1;BUY;10;122.5");
                add("10;ob_1;BUY;10;125.0");
                add("11;ob_1;BUY;10;127.5");
                add("12;ob_1;BUY;10;130.0");
                add("13;ob_1;BUY;10;132.5");
                add("14;ob_1;BUY;10;135.0");
                add("15;ob_1;BUY;10;137.5");
                add("16;ob_1;BUY;10;140.0");
                add("17;ob_1;BUY;10;142.5");
                add("18;ob_1;BUY;10;145.0");
                add("19;ob_1;BUY;10;147.5");
                add("20;ob_1;BUY;10;150.0");
                add("21;ob_1;BUY;10;152.5");
                add("22;ob_1;BUY;10;155.0");
                add("23;ob_1;BUY;10;157.5");
                add("24;ob_1;BUY;10;160.0");
                add("25;ob_1;BUY;10;162.5");
                add("26;ob_1;BUY;10;165.0");
                add("27;ob_1;BUY;10;167.5");
                add("28;ob_1;BUY;10;170.0");
                add("29;ob_1;BUY;10;170.0");
            }
        }));
    }
}
