package brum.common.logger.impl;

import brum.common.logger.BrumLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodLevelBrumLogger implements BrumLogger {

    private static final String PATTERN_WITH_ARGS = "[{}][{}]";
    private static final String PATTERN_NO_ARGS = "[{}][{}]{}";

    private final Logger log;
    private final String methodName;
    private int counter;

    public MethodLevelBrumLogger(Class<?> clazz, String methodName) {
        this.log = LoggerFactory.getLogger(clazz);
        this.methodName = methodName;
    }

    @Override
    public void info(String logMessage, Object... objects) {
        var pattern = PATTERN_WITH_ARGS + logMessage;
        log.info(pattern, methodName, counter, objects);
        counter++;
    }

    @Override
    public void info(String logMessage) {
        log.info(PATTERN_NO_ARGS, methodName, counter, logMessage);
        counter++;
    }

    @Override
    public void warn(String logMessage, Object... objects) {
        var pattern = PATTERN_WITH_ARGS + logMessage;
        log.warn(pattern, methodName, counter, objects);
        counter++;
    }

    @Override
    public void warn(String logMessage) {
        log.warn(PATTERN_NO_ARGS, methodName, counter, logMessage);
        counter++;
    }

    @Override
    public void error(String logMessage, Object... objects) {
        var pattern = PATTERN_WITH_ARGS + logMessage;
        log.error(pattern, methodName, counter, objects);
        counter++;
    }

    @Override
    public void error(String logMessage) {
        log.error(PATTERN_NO_ARGS, methodName, counter, logMessage);
        counter++;
    }

    @Override
    public void debug(String logMessage, Object... objects) {
        var pattern = PATTERN_WITH_ARGS + logMessage;
        log.debug(pattern, methodName, counter, objects);
        counter++;
    }

    @Override
    public void debug(String logMessage) {
        log.debug(PATTERN_NO_ARGS, methodName, counter, logMessage);
        counter++;
    }
}
