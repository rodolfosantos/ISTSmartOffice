package ist.smartoffice.deviceconnectivity.osgi.registries;

import ist.smartoffice.deviceconnectivity.IDatapointConnectivityService;
import ist.smartoffice.osgi.registries.AbstractServiceRegistry;

/**
 * The Class DeviceConnectivityServiceRegistry.
 */
public final class DeviceConnectivityServiceRegistry extends
        AbstractServiceRegistry<IDatapointConnectivityService> {

    /** The Constant singleton. */
    private final static DeviceConnectivityServiceRegistry singleton = new DeviceConnectivityServiceRegistry();

    /**
     * Instantiates a new device connectivity service registry.
     */
    private DeviceConnectivityServiceRegistry() {
        // Avoid instantiation
    }

    /**
     * Gets the single instance of DeviceConnectivityServiceRegistry.
     * 
     * @return single instance of DeviceConnectivityServiceRegistry
     */
    public static DeviceConnectivityServiceRegistry getInstance() {
        return singleton;
    }

}
