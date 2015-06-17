package org.trading.orderbook.connectors;

public class CharBuffer {

    private final char[] charBuffer;

    private final StringBuffer stringBuffer;

    private int cmIdx;

    private final IMessageListener listener;

    private int cIdx;

    private int eIdx;

    public CharBuffer(IMessageListener listener) {
        this.charBuffer = new char[1024];
        this.stringBuffer = new StringBuffer();
        this.cIdx = 0;
        this.eIdx = -1;
        this.cmIdx = 0;
        this.listener = listener;
    }

    public void append(String str) {
        this.stringBuffer.append(str);
        eIdx += str.length();

        for (; cIdx <= eIdx; cIdx++) {
            char charAt = stringBuffer.charAt(cIdx);
            if (charAt == '}') {
                newMessage(cmIdx, cIdx);
                cmIdx = cIdx + 1;
            }
        }

        this.stringBuffer.delete(0, cmIdx);
        int d = cmIdx;
        cmIdx -= d;
        cIdx -= d;
        eIdx -= d;
    }

    private void newMessage(int startIdx, int endIdx) {
        String message = this.stringBuffer.substring(startIdx + 1, endIdx);
        listener.newMessage(message);
    }

    public static void main(String[] args) {
        IMessageListener listener = new IMessageListener() {
            @Override
            public void newMessage(String message) {

                System.out.println(message);
            }
        };
        CharBuffer charBuffer = new CharBuffer(listener);

        String m_1 = "{10000;ob_";
        String m_2 = "1;BUY;10;102.5}";

        charBuffer.append(m_1);
        charBuffer.append(m_2);

        String message_1 = "{1;ob_1;BUY;10;102.5}{2;ob_1;BUY;10;105.0}{3;ob_1;BUY;10;107.5}{4;ob_1;BUY;10;110.0}{5;ob_1;BUY;10;112.5}{6;ob_1;BUY;10;115.0}{7;ob_1;BUY;10;117.5}{8;ob_1;BUY;10;120.0}{9;ob_1;BUY;10;122.5}{10;ob_1;BUY;10;125.0}{11;ob_1;BUY;10;127.5}{12;ob_1;BUY;10;130.0}{13;ob_1;BUY;10;132.5}{14;ob_1;BUY;10;135.0}{15;ob_1;BUY;10;137.5}{16;ob_1;BUY;10;140.0}{17;ob_1;BUY;10;142.5}{18;ob_1;BUY;10;145.0}{19;ob_1;BUY;10;147.5}{20;ob_1;BUY;10;150.0}{21;ob_1;BUY;10;152.5}{22;ob_1;BUY;10;155.0}{23;ob_1;BUY;10;157.5}{24;ob_1;BUY;10;160.0}{25;ob_1;BUY;10;162.5}{26;ob_1;BUY;10;165.0}{27;ob_1;BUY;10;167.5}{28;ob_1;BUY;10;170.0}{29;ob_1;B";
        String message_2 = "UY;10;170.0}";
        charBuffer.append(message_1);
        charBuffer.append(message_2);
    }
}
