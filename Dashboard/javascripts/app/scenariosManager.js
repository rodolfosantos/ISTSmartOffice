/**
 * scenariosManager.js
 * Rodolfo Santos, nº 76590
 */


pageID = '.page4' 
var loaded4 = false;


$(pageID).bind('isVisible', function(){
    if(!loaded4){
        //TODO cenas
        assingEventHandlers4();
        loadScenarios();
    }
    loaded4 = true;
});



function assingEventHandlers4(){

    $('.addscenario').bind('click', function(event, state) {
        var scenarioName = $('.newscenarioname').val();
        scenariosApi.requestDatapointWriteSync(scenarioName, {"values" : [""]});
        Notifications.push({
            imagePath: "images/avatar.png",
            text: "<p><b>New Scenario was created: </b></p><div>"+scenarioName+"</div>",
            autoDismiss: 10
        });
        $('.scenarioslist').append('<li class="'+scenarioName+'"><a>'+scenarioName+'</a></li>');
        $('.'+scenarioName).bind('click', function(event, state) {
            $(this).addClass('active');
            scenariosApi.requestDatapointWriteSync(scenarioName, {"values" : [""]});
            Notifications.push({
                imagePath: "images/avatar.png",
                text: "<p><b>Scenario activated: </b></p><div>"+scenarioName+"</div>",
                autoDismiss: 10
            });
        });

    });


}

function loadScenarios(){
    var data = $.parseJSON(scenariosApi.getAllDatapointsSync());
    console.log(data);
    var addresses = data.addresses;
    console.log(addresses);

    for (var i = 0; i<addresses.length;i++) {
        var scenario = addresses[i];
        $('.scenarioslist').append('<li class="'+scenario+'"><a>'+scenario+'</a></li>');
        $('.'+scenario).bind('click', function(event, state) {
            $(this).addClass('active');
            scenariosApi.requestDatapointWriteSync(scenario, {"values" : [""]});
            Notifications.push({
                imagePath: "images/avatar.png",
                text: "<p><b>Scenario activated: </b></p><div>"+scenario+"</div>",
                autoDismiss: 10
            });
        });
    }
}





