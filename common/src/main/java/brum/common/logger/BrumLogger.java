package brum.common.logger;

public interface BrumLogger {

    void info(String logMessage, Object... objects);
    void info(String logMessage);
    void warn(String logMessage, Object... objects);
    void warn(String logMessage);
    void error(String logMessage, Object... objects);
    void error(String logMessage);
    void debug(String logMessage, Object... objects);
    void debug(String logMessage);
}
