/**
 * main.js
 * Rodolfo Santos, nº 76590
 */

function changePage(newHash, oldHash) {
    $("li.active").removeClass("active")
    $('div[class^="page"]').hide()


    $('a[href$="' + newHash + '"]').parent().addClass('active')
    $("#page-wrapper > ." + newHash).show();

}


// Listen for initial load of home page.
$(document).bind("ready", function (e, data) {
    changePage('page1', null);



    //remove later
    $('input:checkbox').bootstrapSwitch();
});

$(window).on('hashchange', function (e) {
    newURL = e.originalEvent.newURL;
    oldURL = e.originalEvent.oldURL;

    newHash = newURL.substring(newURL.indexOf("#") + 1);
    oldHash = oldURL.substring(oldURL.indexOf("#") + 1);

    changePage(newHash, oldHash);
});