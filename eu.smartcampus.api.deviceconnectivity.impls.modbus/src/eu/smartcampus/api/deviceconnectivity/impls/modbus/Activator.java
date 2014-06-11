package eu.smartcampus.api.deviceconnectivity.impls.modbus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.impls.modbus.master.ModbusMasterLib;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import eu.smartcampus.api.logger.Logger;
import eu.smartcampus.api.logger.LoggerService;

public final class Activator implements BundleActivator {
	static private Logger log = LoggerService.getInstance().getLogger(Activator.class.getName());  

	@Override
	public void start(BundleContext context) throws Exception {
		// Create service implementation
		IDatapointConnectivityService serviceImpl = new DatapointConnectivityServiceModbus();
		// Publish Service
		DeviceConnectivityServiceRegistry.getInstance().addService(
				DatapointConnectivityServiceModbus.class.getName(),
				serviceImpl);
		
		
		final int slaveId = 2;
        final int registerId = 10;
        final int nRegisters = 6;

        try {
        	ModbusMasterLib emu = new ModbusMasterLib();
            emu.createModbusTcpMaster();
//            for (int i = 0; i < nRegisters + 1; i++) {
//                emu.writeCoil(slaveId, registerId + i, true);
//            }
//            emu.readCoils(slaveId, registerId, nRegisters);
//            emu.destroyModbusMaster();
		} catch (Exception e) {
			log.e(e.getMessage());
		}
        
		
		log.i("Modbus Started!");
		
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService(
				DatapointConnectivityServiceModbus.class.getName());
		log.i("Mosbus Stopped!");
	}

}
