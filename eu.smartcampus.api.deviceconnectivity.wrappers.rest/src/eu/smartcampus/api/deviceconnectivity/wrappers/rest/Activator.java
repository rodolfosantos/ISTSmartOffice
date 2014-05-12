package eu.smartcampus.api.deviceconnectivity.wrappers.rest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.restlet.Component;
import org.restlet.data.Protocol;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator
        implements BundleActivator {

    private Component component;

    @Override
    public void start(BundleContext context) throws Exception {
        /**
         * TODO: Add a way to set the port and the implementation to use through some
         * configuration file, GUI, or so.
         */
        serverStart(
                8182,
                "eu.smartcampus.api.deviceconnectivity.impls.meterip.DatapointConnectivityServiceMeterIPDriver");
    }

    private void serverStart(int serverPort, String implementationClassName) throws Exception {
        // Create a new Component.
        this.component = new Component();

        // Add a new HTTP server listening on port 8182.
        component.getServers().add(Protocol.HTTP, serverPort);

        // Bound an implementation to the REST adapter
        final IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
                .getInstance().getService(implementationClassName);
        DatapointConnectivityServiceRESTWrapper.getInstance()
                .setServiceImplementation(serviceImplementation);
        
        // Attach device api application
        component.getDefaultHost().attach("/deviceconnectivityapi",
                DatapointConnectivityServiceRESTWrapper.getInstance());

        // Start the component.
        component.start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        component.stop();
    }

}
