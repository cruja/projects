package org.trading.orderbook.infra.connectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trading.orderbook.infra.connectors.processor.AbstractMessageProcessor;
import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;
import org.trading.orderbook.infra.connectors.processor.IMessageProcessor;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CommunicationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationEndpoint.class);

    private SocketAddress address;
    private AsynchronousSocketChannel channel;
    private final Object attachment;
    private final CompletionHandler<Void, Object> completionHandler;
    private final CompletionHandler<Integer, Object> readCompletionHandler;
    private final CompletionHandler<Integer, Object> writeCompletionHandler;
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;
    private Runnable reader;
    private Runnable writer;

    private final AsynchronousChannelGroup group;

    private final IMessageListener messageListener;

    private MessageBuilder messageBuilder;

    private final List<IMessageListener> listenerList;

    private final IMessageProcessor writerProcessor;

    public CommunicationEndpoint(
            SocketAddress address
    ) {

        this(null, null);
        this.address = address;
        initIO();
        try {
            channel = AsynchronousSocketChannel.open();
            channel.connect(address, attachment, completionHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommunicationEndpoint(
            AsynchronousChannelGroup group,
            AsynchronousSocketChannel channel,
            Object attachment) {

        this(group, attachment);
        this.channel = channel;
        initIO();
    }

    private CommunicationEndpoint(
            AsynchronousChannelGroup group,
            Object attachment) {

        this.listenerList = new ArrayList<>();
        this.group = group;
        this.attachment = attachment;
        this.readBuffer = ByteBuffer.allocate(1024);
        this.writeBuffer = ByteBuffer.allocate(1024);
        this.messageListener = new IMessageListener() {
            @Override
            public void newMessage(String message) {
                dispatch(message);
            }
        };
        this.messageBuilder = new MessageBuilder(readBuffer, messageListener);
        this.completionHandler = new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                LOGGER.info("Connected to " + address);
                read();
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                LOGGER.error("Failed connecting to " + address, exc);
                exc.printStackTrace();
            }
        };
        this.readCompletionHandler = new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                messageBuilder.processMessage(result);
                read();
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                LOGGER.error("Failed reading ", exc);
                exc.printStackTrace();
            }
        };
        writeCompletionHandler = new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                writeBuffer.flip();
                LOGGER.info("Sent " + result + " bytes: " + new String(writeBuffer.array()));
                writerProcessor.enableAdvancing();
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                LOGGER.error("Failed sending ", exc);
                exc.printStackTrace();
            }
        };
        writerProcessor = new AbstractMessageProcessor() {

            private final Semaphore semaphore = new Semaphore(1);

            public void enableAdvancing() {
                semaphore.release();
            }

            @Override
            public void processMessage(String message) {
                try {
                    IMessageProcessingCommand command = new IMessageProcessingCommand() {

                        @Override
                        public void process() {
                            try {
                                semaphore.acquire();
                            } catch (InterruptedException ex) {
                                LOGGER.error("", ex);
                            }
                            writeBuffer = ByteBuffer.wrap(message.getBytes());
                            writer.run();
                        }
                    };
                    commandsQueue.put(command);
                } catch (InterruptedException ex) {
                    LOGGER.error("", ex);
                }
            }
        };
        writerProcessor.start();
    }

    private void initIO() {
        reader = new Runnable() {
            @Override
            public void run() {
                channel.read(readBuffer, attachment, readCompletionHandler);
            }
        };

        writer = new Runnable() {
            @Override
            public void run() {
                channel.write(writeBuffer, attachment, writeCompletionHandler);
            }
        };
    }

    public void register(IMessageListener listener) {
        this.listenerList.add(listener);
    }

    private void dispatch(String message) {
        for (IMessageListener listener : listenerList) {
            listener.newMessage(message);
        }
    }

    public void read() {
        reader.run();
    }

    public void write(String message) {
        writerProcessor.processMessage(message);
    }
}
