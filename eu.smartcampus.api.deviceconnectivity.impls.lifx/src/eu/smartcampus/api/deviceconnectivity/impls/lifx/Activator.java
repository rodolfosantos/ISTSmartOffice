package eu.smartcampus.api.deviceconnectivity.impls.lifx;

import java.util.Collection;

import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;
import jlifx.bulb.IBulb;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {

		System.out.println("Discovering LIFX gateway bulb...");
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        if (gatewayBulb == null) {
        	System.out.println("No LIFX gateway bulb found!");
        	System.out.println("");
        } else {
        	System.out.println("Found LIFX gateway bulb:");
        	System.out.println("IP address  : " + gatewayBulb.getInetAddress().getHostAddress());
        	System.out.println("MAC address : " + gatewayBulb.getMacAddressAsString());
            Collection<IBulb> allBulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
            System.out.println("Found " + allBulbs.size() + " bulb(s) in network:");
            for (IBulb bulb : allBulbs) {
            	System.out.println("Bulb name   : " + bulb.getName());
            	System.out.println("MAC address : " + bulb.getMacAddressAsString());
            }
        }

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
