package org.trading.orderbook.connectors;

import java.nio.ByteBuffer;

public class MessageBuilder {

    private final ByteBuffer buffer;

    byte[] messageBytes = new byte[1024];

    private final CharBuffer charBuffer;

    public MessageBuilder(ByteBuffer buffer, IMessageListener listener) {
        this.buffer = buffer;
        this.charBuffer = new CharBuffer(listener);
    }

    public void processMessage(Integer result) {
        System.arraycopy(buffer.array(), 0, messageBytes, 0, buffer.position());
        processBuffer(result);
        buffer.clear();
    }

    private void processBuffer(Integer result) {
        String bufferContent = new String(messageBytes, 0, buffer.position()).trim();
        System.out.println("Received: " + result + " bytes, buffer content: [" + bufferContent + "]");
        this.charBuffer.append(bufferContent);
    }
}
