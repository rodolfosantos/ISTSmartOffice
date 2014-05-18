package jlifx.commandline;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;


public class MainTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//discover();
		
		
	}

	private static void discover() throws IOException {
		PrintStream out = System.out;
		out.println("Discovering LIFX gateway bulb...");
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
            out.println("No LIFX gateway bulb found!");
            out.println("");
        } else {
            out.println("Found LIFX gateway bulb:");
            out.println("IP address  : " + gatewayBulb.getInetAddress().getHostAddress());
            out.println("MAC address : " + gatewayBulb.getMacAddressAsString());
            Collection<IBulb> allBulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
            out.println("Found " + allBulbs.size() + " bulb(s) in network:");
            for (IBulb bulb : allBulbs) {
                out.println("Bulb name   : " + bulb.getName());
                out.println("MAC address : " + bulb.getMacAddressAsString());
            }
        }
	}
	
	
	

}
