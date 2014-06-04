package eu.smartcampus.api.deviceconnectivity.impls.knxip;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import eu.smartcampus.api.deviceconnectivity.Logger;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.exception.KNXTimeoutException;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicationBase;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;

/**
 * The Class KNXGatewayIPDriver is responsible for KNX datapoints communication
 * through the KNX-IP Gateway.
 */
public class KNXGatewayIPDriver {
	static private Logger log = Logger.getLogger(KNXGatewayIPDriver.class
			.getName());

	private static KNXGatewayIPDriver instance = null;
	private String remoteHost;
	private KNXNetworkLink knxLink;
	private ProcessCommunicator pc;
	private boolean isConnected;
	private boolean isReconnecting;

	/**
	 * Instantiates a new KNX gateway ip driver.
	 * 
	 * @param remoteHost
	 *            the remote host address
	 */
	private KNXGatewayIPDriver() {
		super();
		this.remoteHost = KNXGatewayIPConfig.loadGatewayConfig();
		this.isConnected = false;
		this.isReconnecting = false;
	}

	public static KNXGatewayIPDriver getInstance() {
		if (instance == null) {
			instance = new KNXGatewayIPDriver();
		}
		return instance;
	}

	/**
	 * Checks if the driver is connected to KNX Gateway.
	 * 
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Start a new connection with KNX Gateway.
	 * 
	 * @throws UnknownHostException
	 */
	public void start() throws UnknownHostException {

		try {

			InetSocketAddress localEP = new InetSocketAddress(
					InetAddress.getByName("0.0.0.0"), 0);
			final InetSocketAddress remoteEP = new InetSocketAddress(
					remoteHost, 3671);

			// knxLink = new KNXNetworkLinkIP(remoteHost, TPSettings.TP1);
			knxLink = new KNXNetworkLinkIP(KNXNetworkLinkIP.TUNNELING, localEP,
					remoteEP, true, TPSettings.TP1);

			pc = new ProcessCommunicatorImpl(knxLink);
			isConnected = true;
			log.i("Connected to KNX-IP Gateway!");
		} catch (KNXException e) {
			log.e(e.getMessage());
			e.printStackTrace();
			isConnected = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.e(e.getMessage());
			e.printStackTrace();
			isConnected = false;
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				stop();
			}
		}));
	}

	/**
	 * Stop the existent KNX Gateway connection.
	 */
	public void stop() {
		// we don't need the process communicator anymore, detach it from the
		// link
		if (pc != null)
			pc.detach();
		// close the KNX link
		if (knxLink != null)
			knxLink.close();

		this.isConnected = false;
		log.i("KNX-IP connection closed.");
	}

	public void reconnect() {
		if (isReconnecting)
			return;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int wait = 2000;

				isConnected = false;
				stop();
				while (!isConnected) {
					try {
						Thread.sleep(wait);
						wait = wait * 2;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.i("Reconnecting to KNX Gateway...");
					try {
						start();
					} catch (UnknownHostException e) {
						log.e(e.getMessage());
					}
				}
				isReconnecting = false;
			}
		}).start();		
	}

	/**
	 * Write a boolean value to KNX datapoint.
	 * 
	 * @param addr
	 *            the datapoint address
	 * @param value
	 *            the value
	 */
	public boolean writeBool(String addr, boolean value) {
		synchronized (pc) {
			

			GroupAddress dst;
			try {
				dst = new GroupAddress(addr);
				pc.write(dst, value);
				return true;
			} catch (KNXFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KNXTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KNXLinkClosedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * Write scalar int value to KNX link.
	 * 
	 * @param addr
	 *            the address
	 * @param value
	 *            the int value between 0 and 100
	 */
	public boolean writeScalar(String addr, int value) {
		// TODO verificar se aceita float
		synchronized (pc) {

			try {
				GroupAddress dst;
				dst = new GroupAddress(addr);
				pc.write(dst, value, ProcessCommunicationBase.SCALING);
				return true;
			} catch (KNXFormatException e) {
				log.e(e.getMessage());
			} catch (KNXException e) {
				log.e(e.getMessage());
				reconnect();
			}

		}
		return false;
	}

	/**
	 * Read a boolean value from KNX datapoint
	 * 
	 * @param addr
	 *            the datapoint address
	 * @return true, if successful
	 */
	public boolean readBoolean(String addr) {
		synchronized (pc) {

			try {
				GroupAddress dst = new GroupAddress(addr);
				return pc.readBool(dst);
			} catch (KNXFormatException e) {
				log.e(e.getMessage());
			} catch (KNXException e) {
				log.e(e.getMessage());
				reconnect();
			} catch (InterruptedException e) {
				log.e(e.getMessage());
			}

		}
		return false;
	}

	/**
	 * Read scalar value from KNX datapoint
	 * 
	 * @param addr
	 *            the datapoint address
	 * @return the float
	 */
	public float readScalar(String addr) {
		synchronized (pc) {

			try {
				GroupAddress dst = new GroupAddress(addr);
				return pc.readUnsigned(dst, ProcessCommunicator.SCALING);
			} catch (KNXFormatException e) {
				log.e(e.getMessage());
			} catch (KNXException e) {
				log.e(e.getMessage());
				reconnect();
			} catch (InterruptedException e) {
				log.e("InterruptedException" + e.getMessage());
			}
		}
		return Float.MIN_VALUE;
	}

	/**
	 * Read a float value from KNX datapoint.
	 * 
	 * @param addr
	 *            the datapoint address
	 * @return the float
	 */
	public float readFloat(String addr) {
		synchronized (pc) {

			try {
				GroupAddress dst = new GroupAddress(addr);
				return pc.readFloat(dst, false);
			} catch (KNXFormatException e) {
				log.e(e.getMessage());
			} catch (KNXException e) {
				log.e(e.getMessage());
				reconnect();
			} catch (InterruptedException e) {
				log.e(e.getMessage());
			}

		}
		return Float.MIN_VALUE;
	}

}
