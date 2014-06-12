package ist.smartoffice.datapointconnectivity.osgi.registries;

import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.osgi.registries.AbstractServiceRegistry;

/**
 * The Class DatapointConnectivityServiceRegistry.
 */
public final class DatapointConnectivityServiceRegistry extends
        AbstractServiceRegistry<IDatapointConnectivityService> {

    /** The Constant singleton. */
    private final static DatapointConnectivityServiceRegistry singleton = new DatapointConnectivityServiceRegistry();

    /**
     * Instantiates a new device connectivity service registry.
     */
    private DatapointConnectivityServiceRegistry() {
        // Avoid instantiation
    }

    /**
     * Gets the single instance of DatapointConnectivityServiceRegistry.
     * 
     * @return single instance of DatapointConnectivityServiceRegistry
     */
    public static DatapointConnectivityServiceRegistry getInstance() {
        return singleton;
    }

}
