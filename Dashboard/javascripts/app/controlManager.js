/**
 * controlManager.js
 * Rodolfo Santos, nº 76590
 */


pageID = '.page2' 
var loaded2 = false;


$(pageID).bind('isVisible', function(){
    if(!loaded2){
        renderSwitchButtons();
        assingEventHandlers();
    }
    loaded2 = true;
});


function setupDoor(elementClass, datapoint){

    $(elementClass).click(function() {
        api.requestDatapointWrite(datapoint, {"values" : ['true']}, null);
    });
    faye.subscribe('/remoteactuation/datapoints/'+datapoint, function(data) {
        value = $.parseJSON(data).reading[0].value;
        if(value){
            Notifications.push({
                imagePath: "images/avatar.png",
                text: "<p><b>Event: </b></p><div>Door open!</div>",
                autoDismiss: 10
            });
        }
    });
}

function setupAllLamps(elementClassSwitch, elementClassSlider, datapoint){
    
    //lights
    $(elementClassSwitch).bind('switchChange.bootstrapSwitch', function(event, state) {
        if(state)
            setDataPointValue(datapoint, 100, null);  
        else
            setDataPointValue(datapoint, 0, null);
    });

    $(elementClassSlider).bind('slideStop', function(event) {
        setDataPointValue(datapoint, event.value, null);
    });

    var lvalue = parseInt(api.requestDatapointReadSync('knx158light1').reading[0].value);
    $(elementClassSlider).slider('setValue', lvalue);
    if(lvalue >0 )
        $(elementClassSwitch).bootstrapSwitch('state', true, true);
    else
        $(elementClassSwitch).bootstrapSwitch('state', false, true);  
    
    
    faye.subscribe('/remoteactuation/datapoints/'+datapoint, function(data) {
        value = $.parseJSON(data).reading[0].value;
        $(elementClassSlider).slider('setValue', Number(value));
        if(value >0 )
            $(elementClassSwitch).bootstrapSwitch('state', true, true);
        else
            $(elementClassSwitch).bootstrapSwitch('state', false, true); 
    });
}

function setupLamp(elementClassSwitch, elementClassSlider, datapoint){
    $(elementClassSwitch).bind('switchChange.bootstrapSwitch', function(event, state) {
        if(state)
            setDataPointValue(datapoint, 100, null);  
        else
            setDataPointValue(datapoint, 0, null);
    });
    
    $(elementClassSlider).bind('slideStop', function(event) {
        setDataPointValue(datapoint, event.value, null);
    });
    
    var lvalue = parseInt(api.requestDatapointReadSync(datapoint).reading[0].value);
    $(elementClassSlider).slider('setValue', lvalue);
    if(lvalue >0 )
        $(elementClassSwitch).bootstrapSwitch('state', true, true);
    else
        $(elementClassSwitch).bootstrapSwitch('state', false, true); 
    
    faye.subscribe('/remoteactuation/datapoints/'+datapoint, function(data) {
        value = $.parseJSON(data).reading[0].value;
        $(elementClassSlider).slider('setValue', Number(value));
        if(value >0 )
            $(elementClassSwitch).bootstrapSwitch('state', true, true);
        else
            $(elementClassSwitch).bootstrapSwitch('state', false, true); 
    });
    
}




function setupAllBlinds(elementClassSlider, datapoint){
    $(elementClassSlider).bind('slideStop', function(event) {
        setDataPointValue(datapoint, event.value, null);
    });
    faye.subscribe('/remoteactuation/datapoints/'+datapoint, function(data) {
        value = $.parseJSON(data).reading[0].value;
        $(elementClassSlider).slider('setValue', Number(value));
    });   
}

function setupBlind(elementClassSlider, datapoint){
    $(elementClassSlider).bind('slideStop', function(event) {
        setDataPointValue(datapoint, event.value, null);
    });
    
    $(elementClassSlider).slider('setValue', parseInt(api.requestDatapointReadSync(datapoint).reading[0].value));
    faye.subscribe('/remoteactuation/datapoints/'+datapoint, function(data) {
        value = $.parseJSON(data).reading[0].value;
        $(elementClassSlider).slider('setValue', Number(value));
    });
}

function setupHvacSwitch(elementClass, datapoint){
    $(elementClass).bind('switchChange.bootstrapSwitch', function(event, state) {
        setDataPointValue(datapoint, state, null);  
    });
    
    var value = api.requestDatapointReadSync(datapoint).reading[0].value;
    var isTrueSet = (value === 'true');
    $(elementClass).bootstrapSwitch('state', isTrueSet, true);
    
    faye.subscribe('/remoteactuation/datapoints/'+datapoint, function(data) {
        value = $.parseJSON(data).reading[0].value;
        var isTrueSet = (value === 'true');
        $(elementClass).bootstrapSwitch('state', isTrueSet, true);
    });
}

function setupLightSwitch(elementClass, datapoint){
    $(elementClass).bind('switchChange.bootstrapSwitch', function(event, state) {
        setDataPointValue(datapoint, state, null);  
    });
    
    var value = 0;
    var isTrueSet = (value === 'true');
    $(elementClass).bootstrapSwitch('state', isTrueSet, true);
    
    faye.subscribe('/remoteactuation/datapoints/'+datapoint, function(data) {
        value = $.parseJSON(data).reading[0].value;
        var isTrueSet = (value === 'true');
        $(elementClass).bootstrapSwitch('state', isTrueSet, true);
    });
}



function setDataPointValue(datapoint, value, callback){
    api.requestDatapointWrite(datapoint, {"values" : [value+""]}, null);
}



function renderSwitchButtons(){
    $("[name='my-checkbox']").bootstrapSwitch();
    $('.ex1').slider({
        formater: function(value) {
            return 'Current value: ' + value;
        }
    });
}


function assingEventHandlers(){

    setupDoor('.opendoor', 'knx158door');
    setupAllLamps('.onoffall', '.onoffallslider', 'knx158lightall');
    setupLamp('.onoff1', '.onoff1slider', 'knx158light1');
    setupLamp('.onoff2', '.onoff2slider', 'knx158light2');
    setupLamp('.onoff3', '.onoff3slider', 'knx158light3');
    setupLamp('.onoff4', '.onoff4slider', 'knx158light4');
    
    setupAllBlinds('.blindallslider', 'knx158blindall');
    setupBlind('.blind1slider', 'knx158blind1');
    setupBlind('.blind2slider', 'knx158blind2');
    setupBlind('.blind3slider', 'knx158blind3');
    
    setupHvacSwitch('.onoffhvac', 'knx158hvac');
    
    setupLightSwitch('.knx2n14alllights', 'knx2n14alllights');
    
    $(".modehvac").bootstrapSwitch('onText', 'Hot', true);
    $(".modehvac").bootstrapSwitch('offText', 'Cold', true);
    setupHvacSwitch('.modehvac', 'knx158hvacmode');

}





