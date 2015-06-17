package org.trading.orderbook.connectors;

import org.trading.orderbook.connectors.commands.ICommandBuilder;
import org.trading.orderbook.connectors.processor.AbstractMessageProcessor;
import org.trading.orderbook.connectors.processor.IMessageProcessingCommand;
import org.trading.orderbook.connectors.processor.IMessageProcessor;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommunicationEndpoint {

    private SocketAddress address;
    private AsynchronousSocketChannel channel;
    private final Object attachment;
    private final CompletionHandler<Void, Object> completionHandler;
    private final CompletionHandler<Integer, Object> readCompletionHandler;
    private final CompletionHandler<Integer, Object> writeCompletionHandler;
    private ByteBuffer incommingBuffer;
    private ByteBuffer outgoingBuffer;
    private Runnable reader;
    private Runnable writer;
    private MessageBuilder messageBuilder;
    private final AsynchronousChannelGroup group;

    private final IMessageListener messageListener;

    private final List<IIncomingMessageListener> listenerList;

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
        this.incommingBuffer = ByteBuffer.allocate(1024);
        this.outgoingBuffer = ByteBuffer.allocate(1024);
        this.messageListener = new IMessageListener() {
            @Override
            public void newMessage(String message) {
                dispatch(message);
            }
        };
        this.messageBuilder = new MessageBuilder(incommingBuffer, messageListener);
        this.completionHandler = new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                System.out.println("Connected to " + address);
                read();
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.err.println("Failed connecting to " + address + "due to " + exc.getMessage());
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
                System.out.println("Failed reading due to " + exc.getMessage());
                exc.printStackTrace();
            }
        };
        writeCompletionHandler = new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                outgoingBuffer.flip();
                System.out.println("Sent " + result + " bytes: " + new String(outgoingBuffer.array()));
                writerProcessor.enableAdvancing();
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("Failed sending due to " + exc.getMessage());
                exc.printStackTrace();
            }
        };
        writerProcessor = new AbstractMessageProcessor() {

            private final Semaphore semaphore = new Semaphore(1);

            public void enableAdvancing(){
                semaphore.release();
            }

            @Override
            protected ICommandBuilder getCommandBuilder() {
                return null;
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
                                Logger.getLogger(CommunicationEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            outgoingBuffer = ByteBuffer.wrap(message.getBytes());
                            writer.run();
                        }
                    };
                    commandsQueue.put(command);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CommunicationEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        writerProcessor.start();
    }

    private void initIO() {
        reader = new Runnable() {
            @Override
            public void run() {
                channel.read(incommingBuffer, attachment, readCompletionHandler);
            }
        };

        writer = new Runnable() {
            @Override
            public void run() {
                channel.write(outgoingBuffer, attachment, writeCompletionHandler);
            }
        };
    }

    public void register(IIncomingMessageListener listener) {
        this.listenerList.add(listener);
    }

    private void dispatch(String message) {
        for (IIncomingMessageListener listener : listenerList) {
            listener.newMessage(message);
        }
    }

    private void read() {
        reader.run();
    }

    public void write(String message) {
        writerProcessor.processMessage(message);
    }
}
