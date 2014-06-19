package ist.smartoffice.datapointconnectivity.wrappers.pubsub;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointValue;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.DatapointListener;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.ErrorType;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

import java.util.HashMap;
import java.util.Map;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * The Class DatapointConnectivityServicePubSubWrapper.
 */
public class DatapointConnectivityServicePubSubWrapper {
	static private Logger log = LoggerService.getInstance().getLogger(
			DatapointConnectivityServicePubSubWrapper.class.getName());

	private static DatapointConnectivityServicePubSubWrapper instance;

	/** The service implementation. */
	private IDatapointConnectivityService serviceImplementation;
	private BayeuxClient client;

	/**
	 * Instantiates a new datapoint connectivity service rest wrapper.
	 */
	private DatapointConnectivityServicePubSubWrapper() {
	}

	public static DatapointConnectivityServicePubSubWrapper getInstance() {
		if (instance == null)
			instance = new DatapointConnectivityServicePubSubWrapper();
		return instance;
	}

	public void connect(String addr) {
		this.client = new BayeuxClient(addr, LongPollingTransport.create(null));
		client.handshake();
		client.waitFor(1000, BayeuxClient.State.CONNECTED);
		log.i("Connected to Faye Server!");
	}

	public void disconnect() {
		this.client.disconnect();
	}

	public void addServiceImplementation(String path,
			IDatapointConnectivityService serviceImplementation) {
		this.serviceImplementation = serviceImplementation;
		addDatapointsListener(path);
	}

	private void addDatapointsListener(final String path) {
		serviceImplementation.addDatapointListener(new DatapointListener() {

		
			@Override
			public void onDatapointUpdate(DatapointAddress address,
					DatapointValue[] values) {

				JSONObject result = new JSONObject();

				JSONArray readingsArray = new JSONArray();
				for (DatapointValue reading : values) {
					JSONObject tmp = new JSONObject();
					tmp.put("value", reading.getValue());
					tmp.put("timestamp", reading.getTimestamp());
					readingsArray.add(tmp);
				}

				result.put("reading", readingsArray);
				String channelName = path + "/datapoints/"
						+ address.getAddress();
				
				//log.d(channelName);
				client.getChannel(channelName).publish(result.toJSONString());
			}

			@Override
			public void onDatapointError(DatapointAddress address,
					ErrorType error) {
				
			}

			@Override
			public void onDatapointAddressListChanged(DatapointAddress[] address) {
				JSONObject result = new JSONObject();
				JSONArray readingsArray = new JSONArray();
				for (DatapointAddress reading : address) {
					readingsArray.add(reading.getAddress());
				}
				result.put("addresses", readingsArray);
				
				String channelName = path + "/datapoints";
				client.getChannel(channelName).publish(result.toJSONString());
			}
		});
	}


}
