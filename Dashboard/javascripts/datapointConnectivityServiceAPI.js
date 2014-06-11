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
        success: callback,
        failure: callback
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

DatapointConnectivityService.prototype.requestDatapointWrite = function(address, values, callback){
    $.ajax({
        type: "PUT",
        data: JSON.stringify(values),
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address,
        dataType: "application/json",
        success: callback,
        failure: callback
    });
}

// ================================================================

DatapointConnectivityService.prototype.getAllDatapointsSync = function(){
    return $.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints",
        dataType: "json",
        async: false
    }).responseText;
}

DatapointConnectivityService.prototype.getAllDatapointsSync = function(){
    return $.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints",
        dataType: "json",
        async: false
    }).responseText;
}

DatapointConnectivityService.prototype.getDatapointMetadataSync = function(address){
    return $.parseJSON($.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address +"/metadata",
        dataType: "json",
        async: false
    }).responseText);
}

DatapointConnectivityService.prototype.requestDatapointReadSync = function(address){
    return $.parseJSON($.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address,
        dataType: "json",
        async: false
    }).responseText);
}

DatapointConnectivityService.prototype.requestDatapointWindowReadSync = function(address, startTimestamp, finishTimestamp){
    return $.parseJSON($.ajax({
        type: "GET",
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address + "/" + startTimestamp + "/" + finishTimestamp,
        dataType: "json",
        async: false
    }).responseText);
}


DatapointConnectivityService.prototype.requestDatapointWriteSync = function(address, values){
    return $.parseJSON($.ajax({
        type: "PUT",
        data: JSON.stringify(values),
        url: "http://" + this.remoteAddress +":"+this.remotePort+ "/deviceconnectivityapi/datapoints/"+ address,
        dataType: "application/json",
        async: false
    }).responseText);
}

