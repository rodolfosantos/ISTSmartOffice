package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import java.net.UnknownHostException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.Logger;
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
				} catch (UnknownHostException e1) {
					log.e(e1.getMessage());
				}

				boolean connected = KNXGatewayIPDriver.getInstance()
						.isConnected();

				while (!connected) {
					try {
						KNXGatewayIPDriver.getInstance().start();
						connected = KNXGatewayIPDriver.getInstance()
								.isConnected();
						Thread.sleep(10000);
					} catch (UnknownHostException e) {
						log.e(e.getMessage());
					} catch (InterruptedException e) {
						log.e(e.getMessage());
					}

				}

				if (KNXGatewayIPDriver.getInstance().isConnected()) {
					log.i("Connected to KNX Gateway!");
					// Create service implementation
					IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceKNXIPDriver();
					// Publish Service
					DeviceConnectivityServiceRegistry.getInstance().addService(
							DatapointConnectivityServiceKNXIPDriver.class
									.getName(), serviceImpl);
					log.i("KNX Started!");
				} else
					try {
						stop(context);
					} catch (Exception e) {
						log.e(e.getMessage());
					}
			}
		}).start();

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (KNXGatewayIPDriver.getInstance().isConnected()) {
			DeviceConnectivityServiceRegistry.getInstance().removeService(
					DatapointConnectivityServiceKNXIPDriver.class.getName());

			KNXGatewayIPDriver.getInstance().stop();
			log.i("KNX Stopped!");
		}

	}

}
