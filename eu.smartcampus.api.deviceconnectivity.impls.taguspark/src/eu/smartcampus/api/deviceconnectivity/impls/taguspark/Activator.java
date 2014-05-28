package eu.smartcampus.api.deviceconnectivity.impls.taguspark;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import eu.smartcampus.api.deviceconnectivity.DatapointAddress;
import eu.smartcampus.api.deviceconnectivity.DatapointMetadata;
import eu.smartcampus.api.deviceconnectivity.IDatapointConnectivityService;
import eu.smartcampus.api.deviceconnectivity.adapters.protocolintegration.DatapointConnectivityServiceAdapter;
import eu.smartcampus.api.deviceconnectivity.impls.knxip.DatapointConnectivityServiceKNXIPDriver;
import eu.smartcampus.api.deviceconnectivity.impls.knxip.KNXGatewayIPDriver;
import eu.smartcampus.api.deviceconnectivity.impls.meterip.DatapointConnectivityServiceMeterIPDriver;
import eu.smartcampus.api.deviceconnectivity.osgi.registries.DeviceConnectivityServiceRegistry;
import eu.smartcampus.api.historydatastorage.HistoryDataStorageServiceImpl;
import eu.smartcampus.api.historydatastorage.IHistoryDataStorageService;
import eu.smartcampus.api.historydatastorage.osgi.registries.HistoryDataStorageServiceRegistry;

public final class Activator implements BundleActivator {

	private Thread activatorThread;

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		return;
		
//		final String metersFilename = "Settings_tagusparkMeters.json";
//		final String knxFilename = "Settings_tagusparkKNX.json";
//		
//		IHistoryDataStorageService service = new HistoryDataStorageServiceImpl();
//		HistoryDataStorageServiceRegistry.getInstance().addService(HistoryDataStorageServiceImpl.class.getName(), service);
//
//		Runnable activatorJob = new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				Map<DatapointAddress, DatapointMetadata> knxDatapoints = ServiceSettings
//						.loadDatapointSettings(knxFilename);
//				Map<DatapointAddress, DatapointMetadata> meterDatapoints = ServiceSettings
//						.loadDatapointSettings(metersFilename);
//
//				if(knxDatapoints == null)
//					knxDatapoints = ServiceSettings.setDefaultKNXDatapoints(knxFilename);
//				if(meterDatapoints == null)
//					meterDatapoints = ServiceSettings.setDefaultMetersDatapoints(metersFilename);
//
//				
//				IDatapointConnectivityService knxDriver = new DatapointConnectivityServiceKNXIPDriver(
//						knxDatapoints);
//				IDatapointConnectivityService meterDriver = new DatapointConnectivityServiceMeterIPDriver(
//						"root", "root", meterDatapoints);
//
//				Set<IDatapointConnectivityService> datapointsDrivers = new HashSet<IDatapointConnectivityService>();
//				datapointsDrivers.add(meterDriver);
//
//				try {
//					KNXGatewayIPDriver.getInstance().start();
//					if (KNXGatewayIPDriver.getInstance().isConnected()) {
//						datapointsDrivers.add(knxDriver);
//					}
//				} catch (UnknownHostException e) {
//					e.printStackTrace();
//				}
//				
//				/** Create the instance and regist service. */
//				IDatapointConnectivityService driverAdapter = new DatapointConnectivityServiceAdapter(
//						datapointsDrivers);
//				DeviceConnectivityServiceRegistry.getInstance().addService(
//						DatapointConnectivityServiceAdapter.class.getName(),
//						driverAdapter);
//				
//				
//			}
//		};
//		
//		activatorThread = new Thread(activatorJob);
//		activatorThread.start();		

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DeviceConnectivityServiceRegistry.getInstance().removeService(DatapointConnectivityServiceAdapter.class.getName());
		
		if(KNXGatewayIPDriver.getInstance().isConnected())
			KNXGatewayIPDriver.getInstance().stop();
		
		activatorThread.join();
	}

}
