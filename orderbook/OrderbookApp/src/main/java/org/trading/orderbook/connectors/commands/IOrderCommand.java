package org.trading.orderbook.connectors.commands;


import org.trading.orderbook.infra.connectors.processor.IMessageProcessingCommand;

public interface IOrderCommand extends IMessageProcessingCommand {

    public void process();
}

