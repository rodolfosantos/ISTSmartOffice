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
        api.requestDatapointWrite("knx1", {"values" : [true]}, null);
    });
    faye.subscribe('/datapoints/knx1', function(data) {
        value = $.parseJSON(data).value;
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
            setDataPointValue("knx6", 100, null);  
            $(".onoffallslider").slider('setValue', 100);
        }
        else{
            setDataPointValue("knx6", 0, null);
            $(".onoffallslider").slider('setValue', 0);
        }
    });

    $(".onoffallslider").bind('slideStop', function(event) {
        setDataPointValue("knx6", event.value, null);
        if(event.value >0 )
            $(".onoffall").bootstrapSwitch('state', true, true);
        else
            $(".onoffall").bootstrapSwitch('state', false, true);       
    });

    var lvalue = parseInt(api.requestDatapointReadSync('knx3').value);
    $(".onoffallslider").slider('setValue', lvalue);
    if(lvalue >0 )
        $(".onoffall").bootstrapSwitch('state', true, true);
    else
        $(".onoffall").bootstrapSwitch('state', false, true);  
    faye.subscribe('/datapoints/knx3', function(data) {
        value = $.parseJSON(data).value;
        if(value >0 )
            $(".onoffall").bootstrapSwitch('state', true, true);
        else
            $(".onoffall").bootstrapSwitch('state', false, true); 
        $(".onoffallslider").slider('setValue', Number(value));
    });



    // ================================================================
    $(".blindallslider").bind('slideStop', function(event) {
        setDataPointValue("knx4", event.value, null);
        //$(".blind1slider").slider('setValue', event.value);
        //$(".blind2slider").slider('setValue', event.value);
        //$(".blind3slider").slider('setValue', event.value);
    });


    // blind 1
    $(".blind1slider").bind('slideStop', function(event) {
        setDataPointValue("knx7", event.value, null);
    });
    $(".blind1slider").slider('setValue', parseInt(api.requestDatapointReadSync('knx5').value));
    faye.subscribe('/datapoints/knx5', function(data) {
        value = $.parseJSON(data).value;
        //alert(value);
        $(".blind1slider").slider('setValue', Number(value));
    });

    // blind 2
    $(".blind2slider").bind('slideStop', function(event) {
        setDataPointValue("knx8", event.value, null);
    });
    $(".blind2slider").slider('setValue', parseInt(api.requestDatapointReadSync('knx10').value));
    faye.subscribe('/datapoints/knx10', function(data) {
        value = $.parseJSON(data).value;
        //alert(value);
        $(".blind2slider").slider('setValue', Number(value));
    });

    // blind 3
    $(".blind3slider").bind('slideStop', function(event) {
        setDataPointValue("knx9", event.value, null);
    });
    $(".blind3slider").slider('setValue', parseInt(api.requestDatapointReadSync('knx11').value));
    faye.subscribe('/datapoints/knx11', function(data) {
        value = $.parseJSON(data).value;
        //alert(value);
        $(".blind3slider").slider('setValue', Number(value));
    });

    // ================================================================
    //hvac
    $(".onoffhvac").bind('switchChange.bootstrapSwitch', function(event, state) {

        setDataPointValue("knx15", state, null);  

    });
    faye.subscribe('/datapoints/knx17', function(data) {
        value = $.parseJSON(data).value;
        var isTrueSet = (value === 'true');
        $(".onoffhvac").bootstrapSwitch('state', isTrueSet, true);
    });


    //mode
    $(".modehvac").bootstrapSwitch('onText', 'Hot', true);
    $(".modehvac").bootstrapSwitch('offText', 'Cold', true);
    $(".modehvac").bind('switchChange.bootstrapSwitch', function(event, state) {
        setDataPointValue("knx16", state, null);  
    });
    faye.subscribe('/datapoints/knx18', function(data) {
        value = $.parseJSON(data).value;
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





