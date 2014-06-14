package ist.smartoffice.datapointconnectivity.wrappers.pubsub;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointReading;
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
	private Map<DatapointAddress, ClientSessionChannel> clientChannels;


	/**
	 * Instantiates a new datapoint connectivity service rest wrapper.
	 */
	private DatapointConnectivityServicePubSubWrapper() {}

	public static DatapointConnectivityServicePubSubWrapper getInstance() {
		if (instance == null)
			instance = new DatapointConnectivityServicePubSubWrapper();
		return instance;
	}
	
	public void connect(String addr){
		this.client = new BayeuxClient(addr,
				LongPollingTransport.create(null));
		client.handshake();
		client.waitFor(1000, BayeuxClient.State.CONNECTED);
	}
	
	public void disconnect(){
		this.client.disconnect();
	}

	public void addServiceImplementation(String path,
			IDatapointConnectivityService serviceImplementation) {
		this.serviceImplementation = serviceImplementation;
		createDatapointChannels(path);
		addDatapointsListener();
	}

	private void addDatapointsListener() {
		serviceImplementation.addDatapointListener(new DatapointListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onDatapointUpdate(DatapointAddress address,
					DatapointReading[] values) {
				// log.info("WRAPPER update" + values[0]);
				DatapointReading reading = values[0];
				JSONObject result = new JSONObject();
				result.put("value", reading.getValue());
				result.put("timestamp", reading.getTimestamp());

				clientChannels.get(address).publish(result.toJSONString());
			}

			@Override
			public void onDatapointError(DatapointAddress address,
					ErrorType error) {
				// TODO

			}
		});
	}

	private void createDatapointChannels(String path) {
		clientChannels = new HashMap<DatapointAddress, ClientSessionChannel>();
		DatapointAddress[] dps = serviceImplementation.getAllDatapoints();

		for (DatapointAddress datapointAddress : dps) {
			clientChannels.put(
					datapointAddress,
					client.getChannel(path + "/datapoints/"
							+ datapointAddress.getAddress()));
		}
	}

}
