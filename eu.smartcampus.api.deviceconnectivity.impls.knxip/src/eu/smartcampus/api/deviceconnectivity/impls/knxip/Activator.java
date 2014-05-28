package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import java.net.UnknownHostException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;

public final class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// Connect to KNX gateway
				try {
					KNXGatewayIPDriver.getInstance().start();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (KNXGatewayIPDriver.getInstance().isConnected()) {
					System.out.println("Connected to KNX Gateway!");
					// Create service implementation
					IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceKNXIPDriver();
					// Publish Service
					DeviceConnectivityServiceRegistry.getInstance().addService(
							DatapointConnectivityServiceKNXIPDriver.class
									.getName(), serviceImpl);
					System.out.println("KNX Started!");
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
			System.out.println("KNX Stopped!");
		}

	}

}
