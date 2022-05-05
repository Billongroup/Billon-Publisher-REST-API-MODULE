package brum.common.logger.impl;

import brum.common.logger.BrumLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBrumLogger implements BrumLogger {

    private final Logger log;

    public DefaultBrumLogger(Class<?> clazz) { this.log = LoggerFactory.getLogger(clazz); }

    @Override
    public void info(String logMessage, Object... objects) { log.info(logMessage, objects); }

    @Override
    public void info(String logMessage) { log.info(logMessage); }

    @Override
    public void warn(String logMessage, Object... objects) { log.warn(logMessage, objects); }

    @Override
    public void warn(String logMessage) { log.warn(logMessage); }

    @Override
    public void error(String logMessage, Object... objects) { log.error(logMessage, objects); }

    @Override
    public void error(String logMessage) { log.error(logMessage); }

    @Override
    public void debug(String logMessage, Object... objects) { log.debug(logMessage, objects); }

    @Override
    public void debug(String logMessage) { log.debug(logMessage); }
}