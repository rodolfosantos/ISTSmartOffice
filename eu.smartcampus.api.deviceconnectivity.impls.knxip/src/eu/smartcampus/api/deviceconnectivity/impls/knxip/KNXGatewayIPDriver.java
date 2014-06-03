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
			isConnected = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.e(e.getMessage());
			isConnected = false;
		}

		// new Thread(new Runnable() {

		// @Override
		// public void run() {
		// try {
		// knxLink = new KNXNetworkLinkIP(remoteHost, TPSettings.TP1);
		// pc = new ProcessCommunicatorImpl(knxLink);
		// isConnected = true;
		// log.info("Connected to KNX-IP Gateway!");
		// } catch (KNXException e) {
		// log.info(e.getMessage());
		// isConnected = false;
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// log.info(e.getMessage());
		// isConnected = false;
		// }
		// }
		// }).start();

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

	/**
	 * Write a boolean value to KNX datapoint.
	 * 
	 * @param addr
	 *            the datapoint address
	 * @param value
	 *            the value
	 * @throws KNXTimeoutException
	 *             the KNX timeout exception
	 * @throws KNXLinkClosedException
	 *             the KNX link closed exception
	 * @throws KNXFormatException
	 *             the KNX format exception
	 */
	public void writeBool(String addr, boolean value)
			throws KNXTimeoutException, KNXLinkClosedException,
			KNXFormatException {
		synchronized (pc) {
			addr = addr.replace('-', '/');

			GroupAddress dst = new GroupAddress(addr);
			pc.write(dst, value);
		}
	}

	/**
	 * Write scalar int value to KNX link.
	 * 
	 * @param addr
	 *            the address
	 * @param value
	 *            the int value between 0 and 100
	 * @throws KNXException
	 *             the KNX exception
	 */
	public void writeScalar(String addr, int value) throws KNXException {
		// TODO verificar se aceita float
		synchronized (pc) {
			GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			pc.write(dst, value, ProcessCommunicationBase.SCALING);
		}
	}

	/**
	 * Read a boolean value from KNX datapoint
	 * 
	 * @param addr
	 *            the datapoint address
	 * @return true, if successful
	 * @throws KNXException
	 *             the KNX exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public boolean readBoolean(String addr) throws KNXException,
			InterruptedException {
		synchronized (pc) {
			GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			return pc.readBool(dst);
		}
	}

	/**
	 * Read scalar value from KNX datapoint
	 * 
	 * @param addr
	 *            the datapoint address
	 * @return the float
	 * @throws KNXException
	 *             the KNX exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public float readScalar(String addr) throws KNXException,
			InterruptedException {
		synchronized (pc) {
			GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			return pc.readUnsigned(dst, ProcessCommunicator.SCALING);
		}
	}

	/**
	 * Read a float value from KNX datapoint.
	 * 
	 * @param addr
	 *            the datapoint address
	 * @return the float
	 * @throws KNXException
	 *             the KNX exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public float readFloat(String addr) throws KNXException,
			InterruptedException {
		synchronized (pc) {
			GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			return pc.readFloat(dst, false);
		}
	}

}
