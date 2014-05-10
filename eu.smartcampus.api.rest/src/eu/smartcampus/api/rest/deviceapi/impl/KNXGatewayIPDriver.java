package eu.smartcampus.api.rest.deviceapi.impl;

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

public class KNXGatewayIPDriver {

	private String remoteHost;
	private KNXNetworkLink knxLink;
	private ProcessCommunicator pc;
	private boolean isConnected;

	public KNXGatewayIPDriver(String remoteHost) {
		super();
		this.remoteHost = remoteHost;
		this.isConnected = false;
	}
	
	

	public boolean isConnected() {
		return isConnected;
	}



	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}



	public void start() {
		try {
			knxLink = new KNXNetworkLinkIP(remoteHost, TPSettings.TP1);
			pc = new ProcessCommunicatorImpl(knxLink);
			this.isConnected = true;
		} catch (KNXException e) {
			e.printStackTrace();
			this.isConnected = false;
		} catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            this.isConnected = false;
        }

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				stop();
			}
		}));
	}

	public void stop() {
		// we don't need the process communicator anymore, detach it from the
		// link
		if (pc != null)
			pc.detach();
		// close the KNX link
		if (knxLink != null)
			knxLink.close();

		this.isConnected = false;
		System.out.println("Connection closed.");
	}

	public void writeBool(String addr, boolean value)
			throws KNXTimeoutException, KNXLinkClosedException, KNXFormatException {
		synchronized (pc) {
		    GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			pc.write(dst, value);
		}
	}

	public void writeScalar(String addr, int value) throws KNXException {
		//TODO verificar se aceita float
		synchronized (pc) {
		    GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			pc.write(dst, value, ProcessCommunicationBase.SCALING);
		}
	}

	public boolean readBoolean(String addr) throws KNXException,
			InterruptedException {
		synchronized (pc) {
		    GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			return pc.readBool(dst);
		}
	}

	public float readScalar(String addr) throws KNXException,
			InterruptedException {
		synchronized (pc) {
		    GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			return pc.readUnsigned(dst, ProcessCommunicator.SCALING);
		}
	}

	public float readFloat(String addr) throws KNXException, InterruptedException {
		synchronized (pc) {
		    GroupAddress dst = new GroupAddress(addr.replace('-', '/'));
			return pc.readFloat(dst, false);
		}
	}

}
