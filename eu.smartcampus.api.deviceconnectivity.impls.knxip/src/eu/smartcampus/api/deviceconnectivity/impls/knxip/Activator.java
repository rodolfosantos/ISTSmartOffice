package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator implements BundleActivator {
	static private Logger log = Logger.getLogger(Activator.class.getName());  
	
	@Override
	public void start(final BundleContext context) throws Exception {
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// Connect to KNX gateway
					try {
						KNXGatewayIPDriver.getInstance().start();
					} catch (UnknownHostException e) {
						log.info(e.getMessage());
						try {
							stop(context);
						} catch (Exception e1) {}
						return;
					}


				if (KNXGatewayIPDriver.getInstance().isConnected()) {
					log.info("Connected to KNX Gateway!");
					// Create service implementation
					IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceKNXIPDriver();
					// Publish Service
					DeviceConnectivityServiceRegistry.getInstance().addService(
							DatapointConnectivityServiceKNXIPDriver.class
									.getName(), serviceImpl);
					log.info("KNX Started!");
				} else
					try {
						stop(context);
					} catch (Exception e) {}
			}
		}).start();

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (KNXGatewayIPDriver.getInstance().isConnected()) {
			DeviceConnectivityServiceRegistry.getInstance().removeService(
					DatapointConnectivityServiceKNXIPDriver.class.getName());

			KNXGatewayIPDriver.getInstance().stop();
			log.info("KNX Stopped!");
		}

	}

}
