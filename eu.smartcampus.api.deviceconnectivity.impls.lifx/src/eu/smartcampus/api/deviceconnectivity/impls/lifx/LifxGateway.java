package eu.smartcampus.api.deviceconnectivity.impls.lifx;
import java.awt.Color;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;
import jlifx.commandline.Utils;
import jlifx.packet.PacketService;

public class LifxGateway {

	private GatewayBulb gatewayBulb;
	private Map<String, IBulb> devices;

	public LifxGateway() {
		super();
		this.devices = null;
		this.gatewayBulb = null;
	}
	
	public void start(){
		try {
			discoverDevices();
		} catch (IOException e) {
			devices = new HashMap<String, IBulb>();
		}
		
	}
	
	private void discoverDevices() throws IOException{
		gatewayBulb = DiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
        	devices = new HashMap<String, IBulb>();
        } else {
            Collection<IBulb> allBulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
            devices = new HashMap<String, IBulb>();
            
            Iterator<IBulb> it = allBulbs.iterator();
            while (it.hasNext()) {
				IBulb iBulb = (IBulb) it.next();
				devices.put(iBulb.getMacAddressAsString(), iBulb);				
			}
        }
	}
	
	public void stop(){
		try {
			PacketService.closeSocket();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public Map<String, IBulb> getAllDevices(boolean doDiscover){
		if(doDiscover)
			try {
				discoverDevices();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		
		return devices;
	}
	

	public void switchOn(String macAddress){
		try {
			devices.get(macAddress).switchOn();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void switchOff(String macAddress){
		try {
			devices.get(macAddress).switchOff();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}		
	}
	
	public void setColor(String macAddress, String colorName, int fadeTime, String brigthness){
		Color color = Utils.stringToColor(colorName);
		try {
			devices.get(macAddress).colorize(color, fadeTime, Float.parseFloat(brigthness));
		} catch (NumberFormatException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}		
	}
	
	
}
