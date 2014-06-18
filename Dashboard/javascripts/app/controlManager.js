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



function assingEventHandlers(){


    $(".opendoor").click(function() {
        api.requestDatapointWrite("knxdoor", {"values" : [true]}, null);
    });
    faye.subscribe('/remoteactuation/datapoints/knxdoor', function(data) {
        value = $.parseJSON(data).reading[0].value;
        if(value){
            Notifications.push({
                imagePath: "images/avatar.png",
                text: "<p><b>Event: </b></p><div>Door open!</div>",
                autoDismiss: 10
            });
        }
    });


    //lights
    $(".onoffall").bind('switchChange.bootstrapSwitch', function(event, state) {
        if(state) {
            setDataPointValue("knxlightall", 100, null);  
            $(".onoffallslider").slider('setValue', 100);
        }
        else{
            setDataPointValue("knxlightall", 0, null);
            $(".onoffallslider").slider('setValue', 0);
        }
    });

    $(".onoffallslider").bind('slideStop', function(event) {
        setDataPointValue("knxlightall", event.value, null);
        if(event.value >0 )
            $(".onoffall").bootstrapSwitch('state', true, true);
        else
            $(".onoffall").bootstrapSwitch('state', false, true);       
    });

    var lvalue = parseInt(api.requestDatapointReadSync('knxlight1').reading[0].value);
    $(".onoffallslider").slider('setValue', lvalue);
    if(lvalue >0 )
        $(".onoffall").bootstrapSwitch('state', true, true);
    else
        $(".onoffall").bootstrapSwitch('state', false, true);  
    faye.subscribe('/remoteactuation/datapoints/knx3', function(data) {
        value = $.parseJSON(data).reading[0].value;
        if(value >0 )
            $(".onoffall").bootstrapSwitch('state', true, true);
        else
            $(".onoffall").bootstrapSwitch('state', false, true); 
        $(".onoffallslider").slider('setValue', Number(value));
    });



    // ================================================================
    $(".blindallslider").bind('slideStop', function(event) {
        setDataPointValue("knxblindall", event.value, null);
        //$(".blind1slider").slider('setValue', event.value);
        //$(".blind2slider").slider('setValue', event.value);
        //$(".blind3slider").slider('setValue', event.value);
    });


    // blind 1
    $(".blind1slider").bind('slideStop', function(event) {
        setDataPointValue("knxblind1", event.value, null);
    });
    $(".blind1slider").slider('setValue', parseInt(api.requestDatapointReadSync('knxblind1').reading[0].value));
    faye.subscribe('/remoteactuation/datapoints/knxblind1', function(data) {
        value = $.parseJSON(data).reading[0].value;
        //alert(value);
        $(".blind1slider").slider('setValue', Number(value));
    });

    // blind 2
    $(".blind2slider").bind('slideStop', function(event) {
        setDataPointValue("knxblind2", event.value, null);
    });
    $(".blind2slider").slider('setValue', parseInt(api.requestDatapointReadSync('knxblind2').reading[0].value));
    faye.subscribe('/remoteactuation/datapoints/knxblind2', function(data) {
        value = $.parseJSON(data).reading[0].value;
        //alert(value);
        $(".blind2slider").slider('setValue', Number(value));
    });

    // blind 3
    $(".blind3slider").bind('slideStop', function(event) {
        setDataPointValue("knxblind3", event.value, null);
    });
    $(".blind3slider").slider('setValue', parseInt(api.requestDatapointReadSync('knxblind3').reading[0].value));
    faye.subscribe('/remoteactuation/datapoints/knxblind3', function(data) {
        value = $.parseJSON(data).reading[0].value;
        //alert(value);
        $(".blind3slider").slider('setValue', Number(value));
    });

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





