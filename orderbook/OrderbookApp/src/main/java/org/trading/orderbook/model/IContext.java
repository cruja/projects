package org.trading.orderbook.model;

import java.io.InputStream;

public interface IContext {

    boolean isLogging();

    void setLogging(boolean isLogging);

    boolean isClientMode();

    void setClientMode(boolean isClientMode);

    long getRefreshDelay();

    void setRefreshDelay(long refreshDelay);

    String getXmlSourcePath();

    void setXmlSourcePath(String xmlSourcePath);

    InputStream getXmlSourceStream();
}
