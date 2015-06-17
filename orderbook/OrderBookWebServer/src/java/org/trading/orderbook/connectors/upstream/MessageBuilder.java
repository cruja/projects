package org.trading.orderbook.connectors.upstream;

import java.nio.ByteBuffer;
import org.trading.orderbook.connectors.IMessageListener;

public class MessageBuilder {

    private final ByteBuffer buffer;

    private final IMessageListener listener;

    private volatile boolean isNew;

    public MessageBuilder(ByteBuffer buffer, IMessageListener listener) {
        this.buffer = buffer;
        this.listener = listener;
        this.isNew = true;
    }

    public void processMessage(Integer result) {
        byte[] messageBytes = new byte[buffer.position()];
        System.arraycopy(buffer.array(), 0, messageBytes, 0, buffer.position());

        String bufferContent = new String(messageBytes).trim();
        System.out.println("Received: " + result + " bytes, buffer content: [" + bufferContent + "]");

        boolean foundCompleteMessage = bufferContent.contains("}");

        if (foundCompleteMessage) {
            listener.newMessage(bufferContent.substring(1, bufferContent.length() - 1));
            buffer.clear();
        }
    }

}
