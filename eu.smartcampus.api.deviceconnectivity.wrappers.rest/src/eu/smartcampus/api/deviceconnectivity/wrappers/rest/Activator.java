package eu.smartcampus.api.deviceconnectivity.wrappers.rest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.restlet.Component;
import org.restlet.data.Protocol;

public final class Activator implements BundleActivator {

	private Component component;

	@Override
	public void start(BundleContext context) throws Exception {
		// Create a new Component.
		Component component = new Component();

		// Add a new HTTP server listening on port 8182.
		component.getServers().add(Protocol.HTTP, 8182);

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
