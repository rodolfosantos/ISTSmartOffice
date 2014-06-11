/**
 * alertsManager.js
 * Rodolfo Santos, nº 76590
 */


function addAlert(labelName, type){
    //insertAlert
    $(".alerts-dropdown > .dropdown-menu").prepend('<li><a href="#">'+labelName+' <span class="label label-'+type+'">'+type+'</span></a></li>');
    
    //increment counter
    counter = parseInt($(".alerts-dropdown > a > .badge").text());
    $(".alerts-dropdown > a > .badge").text(++counter);
}

//click handlers
$(".alerts-dropdown > .dropdown-toggle").click(function(){
    $(".alerts-dropdown > a > .badge").text(0);
});



// Listen for initial load of home page.
$(document).bind("ready", function(e, data) {
	//load alerts from service
});