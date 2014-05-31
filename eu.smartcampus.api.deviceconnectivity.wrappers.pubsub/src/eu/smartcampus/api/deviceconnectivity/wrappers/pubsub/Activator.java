package eu.smartcampus.api.deviceconnectivity.wrappers.pubsub;

import java.text.MessageFormat;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import eu.smartcampus.api.osgi.registries.IServiceRegistry.ServiceRegistryListener;

public final class Activator implements BundleActivator {

	@Override
	public void start(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("PUBSUUUUB!!");
		
		final DatapointConnectivityServicePubSubWrapper pubsubwrapper = new DatapointConnectivityServicePubSubWrapper();
		
		
		IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
				.getInstance().getService(
						IDatapointConnectivityService.class.getName());
		
		

		// try attach implementation
		if (serviceImplementation != null) {
			pubsubwrapper.setServiceImplementation(serviceImplementation);
		}

		// add service listener
		DeviceConnectivityServiceRegistry.getInstance().addServiceListener(
				new ServiceRegistryListener() {

					@Override
					public void serviceRemoved(String serviceName) {
						//Stop
					}

					@Override
					public void serviceModified(String serviceName) {
						System.out.println("Wrapper- Service Modif  "
								+ serviceName);
						if (serviceName
								.equals(IDatapointConnectivityService.class
										.getName())) {
							IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
									.getInstance().getService(
											IDatapointConnectivityService.class
													.getName());
							//TODO
						}
					}

					@Override
					public void serviceAdded(String serviceName) {
						System.out.println("Wrapper- Service Added  "
								+ serviceName);
						// Bound an implementation to the REST adapter
						if (serviceName
								.equals(IDatapointConnectivityService.class
										.getName())) {
							IDatapointConnectivityService serviceImplementation = DeviceConnectivityServiceRegistry
									.getInstance().getService(
											IDatapointConnectivityService.class
													.getName());

							pubsubwrapper.setServiceImplementation(serviceImplementation);
						}
					}
				});
		
		
//		final BayeuxClient client = new BayeuxClient(
//				"http://localhost:8000/faye", LongPollingTransport.create(null));
//		client.handshake();	
//		client.waitFor(1000, BayeuxClient.State.CONNECTED);
//		ClientSessionChannel channel = client.getChannel("/foo");
//		ClientSessionChannel channel2 = client.getChannel("/nodes/private");
//		channel.subscribe(new ClientSessionChannel.MessageListener() {
//			public void onMessage(ClientSessionChannel channel, Message message) {
//				System.out.println(MessageFormat.format(
//						"Got {0} on Channel {1}", message.getData(), channel));
//			}
//		});
//		
//		channel2.subscribe(new ClientSessionChannel.MessageListener() {
//			public void onMessage(ClientSessionChannel channel, Message message) {
//				System.err.println(MessageFormat.format(
//						"Got {0} on Channel {1}", message.getData(), channel));
//			}
//		});
//		boolean t = true;
//		
//	
//			channel.publish("Hello RabbitttFenix");
//	
//		
//		client.disconnect();
//		client.waitFor(1000, BayeuxClient.State.DISCONNECTED);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}



}
