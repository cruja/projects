package org.trading.orderbook.model.impl;

import org.trading.orderbook.model.IContext;
import org.trading.orderbook.model.IContextBuilder;

public class ContextBuilder implements IContextBuilder {

    private static final ContextBuilder INSTANCE = new ContextBuilder();

    public static ContextBuilder getInstance() {
        return INSTANCE;
    }

    private ContextBuilder() {
    }

    @Override
    public IContext build(String[] args) {
        if (args.length < 1 || args.length > 4) {
            System.out.println("Usage: java AppRunner [el] [ec refresh_delay] path_to_xml_file");
            System.exit(0);
        }

        IContext context = new Context();

        if (args.length == 4) {
            if (args[0].toLowerCase().equals("el")) {
                context.setLogging(true);
            }
            if (args[1].toLowerCase().equals("ec")) {
                context.setClientMode(true);
            }
            context.setRefreshDelay(Long.parseLong(args[2]));
            context.setXmlSourcePath(args[3]);
        } else if (args.length == 3) {
            if (args[0].toLowerCase().equals("ec")) {
                context.setClientMode(true);
            }
            context.setRefreshDelay(Long.parseLong(args[1]));
            context.setXmlSourcePath(args[2]);
        } else if (args.length == 2) {
            if (args[0].toLowerCase().equals("el")) {
                context.setLogging(true);
            }
            context.setXmlSourcePath(args[1]);
        } else if (args.length == 1) {
            context.setXmlSourcePath(args[0]);
        }

        return context;
    }
}
