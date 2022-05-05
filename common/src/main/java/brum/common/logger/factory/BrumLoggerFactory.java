package brum.common.logger.factory;

import brum.common.logger.BrumLogger;
import brum.common.logger.impl.DefaultBrumLogger;
import brum.common.logger.impl.MethodLevelBrumLogger;

public class BrumLoggerFactory {

    private BrumLoggerFactory() {}

    public static BrumLogger create(Class<?> clazz) {
        return new DefaultBrumLogger(clazz);
    }

    public static BrumLogger create(Class<?> clazz, String methodName) {
        return new MethodLevelBrumLogger(clazz, methodName);
    }
}
