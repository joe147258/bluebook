$(document).ready(function () {

    var error = getUrlParameter('error');
    if(error != "") showError(parseInt(error));


    $("#class-name").keyup(function() {
        if($("#class-name").val().length > 60) {
            showError(3);
            $("#submit").attr('disabled', 'disabled');
        } else {
            $("#live-error1").hide();
            $("#submit").removeAttr('disabled');
        }
    })
    
    $("#class-desc").keyup(function() {
        if($("#class-desc").val().length > 500) {
            showError(4);
            $("#submit").attr('disabled', 'disabled');
        } else {
            $("#live-error2").hide();
            $("#submit").removeAttr('disabled');
        }
    })
})

function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
}

function showError(code) {
    switch (code) {
        case 1:
            $(".small-form-container").append("<p style='color: red;'> You must fill in both fields. </p>");
            break;
        case 2:
            $(".small-form-container").append("<p style='color: red;'> The title or the description were too long. </p>");
            break;
        case 3:
            $("#live-error1").show();
            break;
        case 4:
            $("#live-error2").show();
            break;

    }

}