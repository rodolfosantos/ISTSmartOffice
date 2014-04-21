package eu.smartcampus.api.rest;

import eu.smartcampus.api.IDatapointConnectivityService;
import eu.smartcampus.api.IDatapointConnectivityService.ErrorType;
import eu.smartcampus.util.DatapointAddress;
import eu.smartcampus.util.Reading;

public class RestReadCallback implements
		IDatapointConnectivityService.ReadCallback {

	private Reading reading = null;

	@Override
	public void onReadCompleted(DatapointAddress address, Reading[] readings,
			int requestId) {
		reading = readings[0]; // TODO: Hard coded... Sabemos que só foi feito
								// um reading, apenas por magia.

	}

	public Reading getReading() {
		Reading r = reading;
		reading = null;
		return r;
	}

	@Override
	public void onReadAborted(DatapointAddress address, ErrorType reason,
			int requestId) {
		// TODO Auto-generated method stub

	}

}