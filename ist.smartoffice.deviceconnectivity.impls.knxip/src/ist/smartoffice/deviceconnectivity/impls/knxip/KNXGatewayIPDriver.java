package ist.smartoffice.deviceconnectivity.impls.knxip;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.exception.KNXTimeoutException;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.link.KNXNetworkLink;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.NetworkLinkListener;
import tuwien.auto.calimero.link.medium.TPSettings;
import tuwien.auto.calimero.process.ProcessCommunicationBase;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;
import ist.smartoffice.logger.Logger;
import ist.smartoffice.logger.LoggerService;

/**
 * The Class KNXGatewayIPDriver is responsible for KNX datapoints communication
 * through the KNX-IP Gateway.
 */
public class KNXGatewayIPDriver {

	static private Logger log = LoggerService.getInstance().getLogger(KNXGatewayIPDriver.class
			.getName());

	private String remoteHost;
	private KNXNetworkLink knxLink;
	private ProcessCommunicator pc;
	private boolean isConnected;
	private boolean isReconnecting;
	private Set<ProcessListener> processListeners;

	/**
	 * Instantiates a new KNX gateway ip driver.
	 * 
	 * @param remoteHost
	 *            the remote host address
	 */
	public KNXGatewayIPDriver(String remoteHost) {
		this.remoteHost = remoteHost;
		this.isConnected = false;
		this.isReconnecting = false;
		this.processListeners = new HashSet<ProcessListener>();
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

			InetSocketAddress localIP = new InetSocketAddress(
					InetAddress.getByName("0.0.0.0"), 0);
			InetSocketAddress remoteIP = new InetSocketAddress(remoteHost, 3671);

			knxLink = new KNXNetworkLinkIP(KNXNetworkLinkIP.TUNNELING, localIP,
					remoteIP, true, TPSettings.TP1);

			knxLink.addLinkListener(new NetworkLinkListener() {

				@Override
				public void linkClosed(CloseEvent arg0) {
					log.e(arg0.getReason());
					reconnectGateway();
				}

				@Override
				public void indication(FrameEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void confirmation(FrameEvent arg0) {
					// TODO Auto-generated method stub

				}
			});

			pc = new ProcessCommunicatorImpl(knxLink);

			pc.addProcessListener(new ProcessListener() {

				@Override
				public void groupWrite(ProcessEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void detached(DetachEvent arg0) {

				}
			});

			synchronized (pc) {
				pc.setResponseTimeout(1);// 1 second timeout

				// add current listeners

				Iterator<ProcessListener> it = processListeners.iterator();
				while (it.hasNext()) {
					ProcessListener processListener = (ProcessListener) it
							.next();
					pc.addProcessListener(processListener);
				}

				isConnected = true;
				log.i("Connected to KNX-IP Gateway (" + remoteHost + ")");
			}

		} catch (KNXException e) {
			log.e(e.getMessage());
			isConnected = false;
			reconnectGateway();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.e(e.getMessage());
			isConnected = false;
			reconnectGateway();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				stop();
			}
		}));

	}

	public void addProcessEventListener(ProcessListener l) {
		if (pc != null)
			pc.addProcessListener(l);
		processListeners.add(l);
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

	public void reconnectGateway() {
		if (isReconnecting)
			return;

		new Thread(new Runnable() {

			@Override
			public void run() {
				isReconnecting = true;
				int wait = 2000;
				int attempt = 0;

				stop();
				isConnected = false;
				while (!isConnected) {
					try {
						Thread.sleep(wait);
						wait = wait * 2;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.i("Reconnecting to KNX Gateway... (attempt "
							+ ++attempt + ")");
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
	 * =========== KNX Operations ============
	 * 
	 */

	public boolean readSwitch(String addr) throws Exception {

		try {
			synchronized (pc) {
				GroupAddress dst = new GroupAddress(addr);
				return pc.readBool(dst);
			}
		} catch (KNXFormatException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (KNXException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (InterruptedException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	public void writeSwitch(String addr, boolean value) throws Exception {// 1.001
		// switch

		GroupAddress dst;
		try {
			synchronized (pc) {
				dst = new GroupAddress(addr);
				pc.write(dst, value);
				return;
			}
		} catch (KNXFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (KNXTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (KNXLinkClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public float readPercentage(String addr) throws Exception { // 5.001 scaling

		try {
			synchronized (pc) {
				GroupAddress dst = new GroupAddress(addr);
				return pc.readUnsigned(dst, ProcessCommunicationBase.SCALING);
			}
		} catch (KNXFormatException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (KNXTimeoutException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (KNXException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (InterruptedException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	public void writePercentage(String addr, int value) throws Exception {
		// TODO verificar se aceita float

		try {
			synchronized (pc) {
				GroupAddress dst;
				dst = new GroupAddress(addr);
				pc.write(dst, value, ProcessCommunicationBase.SCALING);
				return;
			}
		} catch (KNXFormatException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (KNXException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Read 2 bytes.
	 * 
	 * @param addr
	 *            the addr
	 * @return the float
	 * @throws Exception
	 *             the exception
	 */
	public float read2Bytes(String addr) throws Exception {

		try {
			synchronized (pc) {
				GroupAddress dst = new GroupAddress(addr);
				return pc.readFloat(dst, false);
			}
		} catch (KNXFormatException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (KNXTimeoutException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (KNXException e) {
			log.e(e.getMessage());

			throw new Exception(e.getMessage());
		} catch (InterruptedException e) {
			log.e(e.getMessage());
			throw new Exception(e.getMessage());
		}

	}

}
