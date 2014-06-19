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

    var lvalue = parseInt(api.requestDatapointReadSync('knxlight1').reading[0].value);
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



function assingEventHandlers(){


    setupDoor('.opendoor', 'knxdoor');
    setupAllLamps('.onoffall', '.onoffallslider', 'knxlightall');
    setupLamp('.onoff1', '.onoff1slider', 'knxlight1');
    setupLamp('.onoff2', '.onoff2slider', 'knxlight2');
    setupLamp('.onoff3', '.onoff3slider', 'knxlight3');
    setupLamp('.onoff4', '.onoff4slider', 'knxlight4');
    
    setupAllBlinds('.blindallslider', 'knxblindall');
    setupBlind('.blind1slider', 'knxblind1');
    setupBlind('.blind2slider', 'knxblind2');
    setupBlind('.blind3slider', 'knxblind3');

    
    // ================================================================
    //hvac
    $(".onoffhvac").bind('switchChange.bootstrapSwitch', function(event, state) {

        setDataPointValue("knxhvac", state, null);  

    });
    faye.subscribe('/remoteactuation/datapoints/knxhvac', function(data) {
        value = $.parseJSON(data).reading[0].value;
        var isTrueSet = (value === 'true');
        $(".onoffhvac").bootstrapSwitch('state', isTrueSet, true);
    });


    //mode
    $(".modehvac").bootstrapSwitch('onText', 'Hot', true);
    $(".modehvac").bootstrapSwitch('offText', 'Cold', true);
    $(".modehvac").bind('switchChange.bootstrapSwitch', function(event, state) {
        setDataPointValue("knxhvacmode", state, null);  
    });
    faye.subscribe('/remoteactuation/datapoints/knxhvacmode', function(data) {
        value = $.parseJSON(data).reading[0].value;
        var isTrueSet = (value === 'true');
        $(".modehvac").bootstrapSwitch('state', isTrueSet, true);
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





