/*

@author Rodolfo Santos (rodolfo.santos@tecnico.ulisboa.pt)
The DatapointConnectivityServiceAPI.js for Javascript allows you to easily invoke the DatapointConnectivityService API endpoints.

*/

function DatapointConnectivityService(remoteAddress, remotePort){
    this.remoteAddress = remoteAddress;
    this.remotePort = remotePort;
}

DatapointConnectivityService.prototype.getAllDatapoints = function(callback){
    $.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints",
        dataType: "json",
        success : callback;
        },
        error : function(msg) {
            alert("Falha na comunicação, verifica a tua ligação por favor");
        }
    });
}

DatapointConnectivityService.prototype.getDatapointMetadata = function(address, callback){
    $.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address +"/metadata",
        dataType: "json",
        success: callback,
        failure: callback
    });
}

DatapointConnectivityService.prototype.requestDatapointRead = function(address, callback){
    $.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address,
        dataType: "json",
        success: callback,
        failure: callback
    });
}

DatapointConnectivityService.prototype.requestDatapointWindowRead = function(address, startTimestamp, finishTimestamp, callback){
    $.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address + "/" + startTimestamp + "/" + finishTimestamp,
        dataType: "json",
        success: callback,
        failure: callback
    });
}

//TODO find bug.
DatapointConnectivityService.prototype.requestDatapointWrite = function(address, values, callback){
    $.ajax({
        type: "PUT",
        data: '{ values: [true]}',
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address,
        dataType: "application/json",
        success: callback,
        failure: callback
    });
}

