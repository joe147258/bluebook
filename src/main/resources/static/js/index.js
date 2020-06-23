$(document).ready(function(){
    $("#join-class-input").keyup(function(){
        if($("#join-class-input").val().length <= 0) {
            $("#join-classroom-button").html("Search for classroom");
            return false
        }
        $.ajax({
            type: "GET",
            url: "/student/check-class/" + encodeURI($("#join-class-input").val()),
            success: function (data) {
                switch(data.response){
                    case "SUCCESS":
                        $("#join-classroom-button").removeAttr("disabled");
                        $("#join-classroom-button").html("Join Classroom");
                        break;
                    case "IN_CLASS":
                        $("#join-classroom-buttont").attr('disabled', 'disabled');
                        $("#join-classroom-button").html("You're in this class");
                        break; 
                    case "BANNED":
                        $("#join-classroom-button").attr('disabled', 'disabled');
                        $("#join-classroom-button").html("Banned!");
                        $("#teacher-username").html(data.teacher);
                        $("#banned-modal").modal('toggle');
                        break;
                    case "NO_CLASS":
                        $("#join-classroom-buttont").attr('disabled', 'disabled');
                        $("#join-classroom-button").html("No Class Found");
                        break;
                }
            },
            error: function () {
                alert("An error has occured :-(");
            }
        })
    })

})