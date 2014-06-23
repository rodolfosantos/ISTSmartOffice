/**
 * main.js
 * Rodolfo Santos, nº 76590
 */

var pageNameMapping = {
    page1: "Dashboard", 
    page2: "Office Control", 
    page3: "Scheduling", 
    page4: "Scenarios", 
    page5: "Alarms"
};




$(window).load(function() {
 // executes when complete page is fully loaded, including frames,objects and images
}); 

// Listen for initial load of home page.
$(document).bind("ready", function (e, data) {
    api = new DatapointConnectivityService("remoteactuation", "it4energy.tagus.ist.utl.pt", 8182);
    scenariosApi = new DatapointConnectivityService("scenarios", "it4energy.tagus.ist.utl.pt", 8182);
    //api = new DatapointConnectivityService("remoteactuation", "localhost", 8182);
    //scenariosApi = new DatapointConnectivityService("scenarios", "localhost", 8182);
    
    faye = new Faye.Client('http://it4energy.tagus.ist.utl.pt:8000/faye');
    
    
   // loadDashboardPage();
    changePage("page1", null);

   
});


function changePage(newHash, oldHash) {
    //TODO old not used
    $("nav > ul  > li.active").removeClass("active")
    $("div[class^='page'],div[class*='page']").hide()
  

    $('nav > ul  > li > a[href$="' + newHash + '"]').parent().addClass('active')
    $("." + newHash).show().trigger('isVisible');;
  
    
    changeBreadcrumb(pageNameMapping[newHash]);
}

function changeBreadcrumb(pagename){
    $("ul.breadcrumb").children("li").eq(1).children().text(pagename);
}



$(window).on('hashchange', function (e) {
    newURL = e.originalEvent.newURL;
    oldURL = e.originalEvent.oldURL;

    console.log("debug:hashchange " + oldURL + " -> " + newURL);

    newHash = newURL.substring(newURL.indexOf("#") + 1);
    oldHash = oldURL.substring(oldURL.indexOf("#") + 1);

    changePage(newHash, oldHash);
});


function pushNotification(text, autoDismissTiming){
    Notifications.push({
            imagePath: "images/avatar.png",
            text: "<p><b>Alert!</b></p><div>"+text+"</div>",
            autoDismiss: 10
        });
}