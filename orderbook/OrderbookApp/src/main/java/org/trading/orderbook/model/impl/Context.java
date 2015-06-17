package org.trading.orderbook.model.impl;

import org.trading.orderbook.model.IContext;

import java.io.InputStream;

public class Context implements IContext {

    private boolean isLogging;

    private boolean isClientMode;

    private long refreshDelay;

    private String xmlSourcePath;

    @Override
    public boolean isLogging() {
        return isLogging;
    }

    public void setLogging(boolean isLogging) {
        this.isLogging = isLogging;
    }

    @Override
    public boolean isClientMode() {
        return isClientMode;
    }

    public void setClientMode(boolean isClientMode) {
        this.isClientMode = isClientMode;
    }

    @Override
    public long getRefreshDelay() {
        return refreshDelay;
    }

    public void setRefreshDelay(long refreshDelay) {
        this.refreshDelay = refreshDelay;
    }

    public String getXmlSourcePath() {
        return xmlSourcePath;
    }

    public void setXmlSourcePath(String xmlSourcePath) {
        this.xmlSourcePath = xmlSourcePath;
    }

    @Override
    public InputStream getXmlSourceStream() {
        if (xmlSourcePath != null) {
            InputStream resourceAsStream = getClass().getResourceAsStream(xmlSourcePath);
            return resourceAsStream;
        }
        return null;
    }
}
