package org.trading.orderbook.infra.connectors;

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

}
