package eu.smartcampus.api.logger.registries;

import eu.smartcampus.api.osgi.registries.AbstractServiceRegistry;


/**
 * The Class LoggerServiceRegistry.
 */
public final class LoggerServiceRegistry extends
        AbstractServiceRegistry<LoggerService> {

    /** The Constant singleton. */
    private final static LoggerServiceRegistry singleton = new LoggerServiceRegistry();

    /**
     * Instantiates a new logger service registry.
     */
    private LoggerServiceRegistry() {
        // Avoid instantiation
    }

    /**
     * Gets the single instance of LoggerServiceRegistry.
     * 
     * @return single instance of LoggerServiceRegistry
     */
    public static LoggerServiceRegistry getInstance() {
        return singleton;
    }

}
