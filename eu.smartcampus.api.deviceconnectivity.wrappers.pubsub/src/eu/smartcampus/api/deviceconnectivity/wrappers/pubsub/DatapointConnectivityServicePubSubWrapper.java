package eu.smartcampus.api.deviceconnectivity.wrappers.pubsub;

import java.util.HashMap;
import java.util.Map;
import eu.smartcampus.api.deviceconnectivity.Logger;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.json.simple.JSONObject;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointReading;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService.DatapointListener;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService.ErrorType;

/**
 * The Class DatapointConnectivityServicePubSubWrapper.
 */
public class DatapointConnectivityServicePubSubWrapper {
	static private Logger log = Logger.getLogger(DatapointConnectivityServicePubSubWrapper.class.getName());  

	/** The service implementation. */
	private IDatapointConnectivityService serviceImplementation;
	private BayeuxClient client;
	private Map<DatapointAddress, ClientSessionChannel> clientChannels;

	/**
	 * Instantiates a new datapoint connectivity service rest wrapper.
	 */
	DatapointConnectivityServicePubSubWrapper() {
		this.client = new BayeuxClient("http://localhost:8000/faye",
				LongPollingTransport.create(null));
		client.handshake();
		client.waitFor(1000, BayeuxClient.State.CONNECTED);
	}

	public void setServiceImplementation(
			IDatapointConnectivityService serviceImplementation) {
		this.serviceImplementation = serviceImplementation;
		createDatapointChannels();
		addDatapointsListener();
	}

	private void addDatapointsListener() {
		serviceImplementation.addDatapointListener(new DatapointListener() {

			@Override
			public void onDatapointUpdate(DatapointAddress address,
					DatapointReading[] values) {
				//log.info("WRAPPER update" + values[0]);
				DatapointReading reading = values[0];
				JSONObject result = new JSONObject();
				result.put("value", reading.getValue());
				result.put("timestamp", reading.getTimestamp());

				clientChannels.get(address).publish(result.toJSONString());
				// client.getChannel("/foo/1").publish(result.toJSONString());
			}

			@Override
			public void onDatapointError(DatapointAddress address,
					ErrorType error) {
				//

			}
		});
	}

	private void createDatapointChannels() {
		clientChannels = new HashMap<DatapointAddress, ClientSessionChannel>();
		DatapointAddress[] dps = serviceImplementation.getAllDatapoints();

		for (DatapointAddress datapointAddress : dps) {
			clientChannels.put(
					datapointAddress,
					client.getChannel("/datapoints/"
							+ datapointAddress.getAddress()));
		}
	}

}
