$(document).ready(function () {

    var error = getUrlParameter('error');
    if(error != "") showError(parseInt(error));


    $("#class-name").keyup(function() {
        if($("#class-name").val().length > 60) {
            showError(3);
            $("#submit").attr('disabled', 'disabled');
        } else if($("#class-name").val().includes(";")){
            $("#submit").attr('disabled', 'disabled');
            showError(5);
        } else {
            $("#live-error1").hide();
            //checks if there is any semi colons
            if(!$("#class-name").val().includes(";") && !$("#class-desc").val().includes(";")){
                $("#live-error3").hide();
                $("#submit").removeAttr('disabled');
            }
            
        }
    })
    
    $("#class-desc").keyup(function() {
        if($("#class-desc").val().length > 500) {
            showError(4);
            $("#submit").attr('disabled', 'disabled');
        } else if($("#class-desc").val().includes(";")) {
            $("#submit").attr('disabled', 'disabled');
            showError(5);
        } else {
            $("#live-error2").hide();
            //checks if there is any semi colons
            if(!$("#class-name").val().includes(";") && !$("#class-desc").val().includes(";")){
                $("#live-error3").hide();
                $("#submit").removeAttr('disabled');
            }
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
            $(".small-form-container").append("<p style='color: red;'> Class name did not meet requirements. It must be less than 60 characters. </p>");
            break;
        case 2:
            $(".small-form-container").append("<p style='color: red;'> Class descprtion did not meet requirements. It must be less than 500 characters. </p>");
            break;
        case 3:
            $("#live-error1").show();
            break;
        case 4:
            $("#live-error2").show();
            break;
        case 5:
            $("#live-error3").show();
            break;
    }

}