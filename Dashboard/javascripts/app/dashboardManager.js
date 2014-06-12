/**
 * dashboardManager.js
 * Rodolfo Santos, nº 76590
 */

pageID = '.page1' 
loaded1 = false;
charts = [];
timeout = -1;

$(pageID).bind('isVisible', function(){
    loadDashboardPage();
});

$(".chartselection > li > a").bind('click', function(){
    var address = this.className
    plotRealTimeEnergyConsumption("powerconsumption", address, 3600000/4);
});

//$(".chartselection > li > a")[0].className

function loadDashboardPage(){
    if(!loaded1){
        //load
        plotRealTimeEnergyConsumption("powerconsumption", "meter2", 3600000/4);
        plotAllConsumptions(["meter0", "meter1", "meter2", "meter3", "meter4", "meter5", "meter6", "meter7" ]);

        plotSensorData("indoor1", "knx14", "ºC");
        plotSensorData("indoor2", "knx13", "ppm");
        plotSensorData("indoor3", "knx12", "Lux");        
        loaded1 = true;
    }
    else{
        //update
        for (var i = 0; i<charts.length;i++)
            charts[i].render();
    }
}


function plotRealTimeEnergyConsumption(chartSelector, datapointAddress, historyWindow){
    if(timeout!=-1)
        clearTimeout(timeout);

    var dps = [];

    var chart = new CanvasJS.Chart(chartSelector,{			
        data: [{
            type: "spline",
            xValueType: "dateTime",
            dataPoints: dps

        }],
        axisY: {
            suffix: " Wh",  
        },
        zoomEnabled: true,
        backgroundColor: ""
    });

    var dataLength = 50; // number of dataPoints visible at any point
    var updateChart = function (readings) {
        for (var j = 0; j < readings.length; j++) {	
            var ts = parseFloat(readings[j]['timestamp']);
            var value = parseFloat(readings[j]['value']);
            dps.push({
                x: ts,
                y: value
            });
        };

        if (dps.length > dataLength)
        {
            dps.shift();				
        }

        if((maxY(dps)-minY(dps))>0){
            chart.options.axisY.interval = (maxY(dps)-minY(dps))/3;
            chart.options.axisY.maximum = maxY(dps) + chart.options.axisY.interval/2;
            chart.options.axisY.minimum = minY(dps) - chart.options.axisY.interval/2;
        }
        chart.render();	

    };

    // get the first set of dataPoints (last hour)
    var nowTS = new Date().getTime();
    api.requestDatapointWindowRead(datapointAddress, nowTS - historyWindow, nowTS , function(data){updateChart(data['readings']); });
    charts.push(chart);
    $(".chartName").text("Power Consumption | " + api.getDatapointMetadataSync(datapointAddress).description);    

    var subscription = faye.subscribe('/datapoints/'+datapointAddress, function(data) {
        updateChart([$.parseJSON(data)])
        //console.log(data);
    });
}

function plotAllConsumptions(addresses){

    // initial values of dataPoints
    var dps = [];

    for(var i = 0; i<addresses.length; i++){
        dps.push({label: addresses[i], y: 0});
    }


    var total = "Total: 0";

    var chart = new CanvasJS.Chart("powerconsumptionall",{							
        legend:{
            verticalAlign: "top",
            horizontalAlign: "centre",
            fontSize: 20

        },
        axisY: {
            suffix: " Wh",  
        },
        backgroundColor: "",
        data : [{
            type: "column",
            showInLegend: true,
            legendMarkerType: "none",				
            legendText: total,
            indexLabel: "{y}",
            dataPoints: dps
        }]
    });

    // renders initial chart
    chart.render();

    var sum = 0;	 //initial sum 

    var updateDatapoint = function (dataPointIndex, data) {
        var value = parseFloat(data['value']);
        // adding random value to random dataPoint
        lastValue = dps[dataPointIndex].y
        dps[dataPointIndex].y = value;

        // updating legend text. 
        sum = sum - lastValue + value;

        //round
        sum = Math.floor(sum * 100) / 100


        total = "Total: " + sum + " Wh";			
        chart.options.data[0].legendText = total;	

        chart.render();
    };
    
    var subscribeUpdates = function (address, index){
        var subscription = faye.subscribe('/datapoints/'+address, function(data) {
            updateDatapoint(index, $.parseJSON(data));
            //console.log(data);
        });
    }


    //update();
    charts.push(chart);

    //update labels with metadata
    for(var i = 0; i<addresses.length; i++){
        chart.options.data[0].dataPoints[i].label = api.getDatapointMetadataSync(chart.options.data[0].dataPoints[i].label).description;
        chart.render();
    }
    
    for(var i = 0; i<addresses.length;i++){
        subscribeUpdates(addresses[i], i);
    } 

}

function maxY(dps){
    var tmp = [];
    for(var i=0; i<dps.length-1;i++){
        tmp.push(dps[i].y);
    }
    return Math.max.apply(Math, tmp);    
}

function minY(dps){
    var tmp = [];
    for(var i=0; i<dps.length-1;i++){
        tmp.push(dps[i].y);
    }
    return Math.min.apply(Math, tmp);    
}

function plotSensorData(chartSelector, datapointAddress, units){
    var dps = [];

    var chart = new CanvasJS.Chart(chartSelector,{			
        data: [{
            type: "spline",
            xValueType: "dateTime",
            dataPoints: dps,
            color: "yellow"

        }],
        axisY: {
            labelFontSize: 14,
            suffix: " " + units,  
        },
        axisX:{
            labelFontSize: 12,
        },
        zoomEnabled: true,
        backgroundColor: ""
    });

    var dataLength = 50; // number of dataPoints visible at any point
    var updateChart = function (readings) {
        for (var j = 0; j < readings.length; j++) {	
            var ts = parseFloat(readings[j]['timestamp']);
            var value = parseFloat(readings[j]['value']);
            dps.push({
                x: ts,
                y: value
            });
        };

        if (dps.length > dataLength)
        {
            dps.shift();				
        }
        if((maxY(dps)-minY(dps))>0){
            chart.options.axisY.interval = (maxY(dps)-minY(dps))/3;
            chart.options.axisY.maximum = maxY(dps) + chart.options.axisY.interval/2;
            chart.options.axisY.minimum = minY(dps) - chart.options.axisY.interval/2;
        }
        chart.render();	
    };

    // get the first set of dataPoints (last hour)
    var nowTS = new Date().getTime();

    api.requestDatapointWindowRead(datapointAddress, nowTS - 3600000, nowTS , function(data){updateChart(data['readings'])});
    charts.push(chart);

    var subscription = faye.subscribe('/datapoints/'+datapointAddress, function(data) {
        updateChart([$.parseJSON(data)])
        //console.log(data);
    });

}

