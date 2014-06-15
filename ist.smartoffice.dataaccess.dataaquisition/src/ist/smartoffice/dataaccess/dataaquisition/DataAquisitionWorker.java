package ist.smartoffice.dataaccess.dataaquisition;

import ist.smartoffice.datapointconnectivity.DatapointAddress;
import ist.smartoffice.datapointconnectivity.DatapointReading;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.DatapointListener;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.ErrorType;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.WriteCallback;
import ist.smartoffice.datapointconnectivity.IDatapointConnectivityService.WritingConfirmationLevel;
import ist.smartoffice.datapointconnectivity.osgi.registries.DatapointConnectivityServiceRegistry;
import ist.smartoffice.osgi.registries.IServiceRegistry.ServiceRegistryListener;


public class DataAquisitionWorker implements Runnable {

	String remoteSensingServiceName;
	String historyDataServiceName;
	IDatapointConnectivityService remoteSensingService;
	IDatapointConnectivityService historyDataService;
	
	//TODO store temporary values if history service isn't available

	public DataAquisitionWorker(String remoteSensingServiceName,
			String historyDataServiceName) {
		super();
		this.remoteSensingServiceName = remoteSensingServiceName;
		this.historyDataServiceName = historyDataServiceName;
	}

	@Override
	public void run() {

		remoteSensingService = DatapointConnectivityServiceRegistry
				.getInstance().getService(remoteSensingServiceName);
		
		historyDataService = DatapointConnectivityServiceRegistry.getInstance()
				.getService(historyDataServiceName);
		
		if(remoteSensingService != null){
			remoteSensingService.addDatapointListener(remoteSensingDatapointListener());
		}
		else{
			DatapointConnectivityServiceRegistry.getInstance().addServiceListener(new ServiceRegistryListener() {
				
				@Override
				public void serviceRemoved(String serviceName) {
					if(serviceName.equals(remoteSensingServiceName)){
						remoteSensingService = null;
					}										
				}
				
				@Override
				public void serviceModified(String serviceName) {
					if(serviceName.equals(remoteSensingServiceName)){
					remoteSensingService = null;
					remoteSensingService = DatapointConnectivityServiceRegistry
							.getInstance().getService(remoteSensingServiceName);
					remoteSensingService.addDatapointListener(remoteSensingDatapointListener());
					}
					
				}
				
				@Override
				public void serviceAdded(String serviceName) {
					if(serviceName.equals(remoteSensingServiceName)){
						remoteSensingService = DatapointConnectivityServiceRegistry
								.getInstance().getService(remoteSensingServiceName);
						remoteSensingService.addDatapointListener(remoteSensingDatapointListener());
					}
					
				}
			});
		}	
		
	}

	
	private DatapointListener remoteSensingDatapointListener(){
		return new DatapointListener() {
			@Override
			public void onDatapointUpdate(DatapointAddress address,
					DatapointReading[] values) {
				
				String[] wValues = new String[values.length];
				for (int i = 0; i < wValues.length; i++) {
					wValues[i] = values[i].getValue();
				}
				
				historyDataService = DatapointConnectivityServiceRegistry.getInstance()
						.getService(historyDataServiceName);
				//TODO store temporary values
				if(historyDataService == null)
					return;
				
				historyDataService.requestDatapointWrite(address, wValues, new WriteCallback(){
					@Override
					public void onWriteAborted(DatapointAddress address,
							ErrorType reason, int requestId) {}

					@Override
					public void onWriteCompleted(DatapointAddress address,
							WritingConfirmationLevel confirmationLevel,
							int requestId) {}});
				
			}
			

			@Override
			public void onDatapointError(
					DatapointAddress address,ErrorType error) {
									
			}
		};
	}

}
