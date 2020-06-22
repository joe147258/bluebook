$(document).ready(function () {

    $("#add-student").click(function () {
        addStudent($("#add-student-username").val());
    });

    $("#filterStudents").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#student-list li").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $(".settings-widget").click(function () {
        $("#settings-modal").modal("toggle");
    });

    $("#settings-form").submit(function (e) {
        e.preventDefault();
        updateTitle($("#class-name").val(), $("#class-desc").val());

    });

    $("#class-name").keyup(function() {
        if($("#class-name").val().length > 60) {
            showError(1);
            $("#submit").attr('disabled', 'disabled');
        } else {
            $("#live-error1").hide();
            $("#submit").removeAttr('disabled');
        }
    })
    
    $("#class-desc").keyup(function() {
        if($("#class-desc").val().length > 500) {
            showError(2);
            $("#submit").attr('disabled', 'disabled');
        } else {
            $("#live-error2").hide();
            $("#submit").removeAttr('disabled');
        }
    })

});

/*
    Below are functions, these are called through the document.ready scope
    above.
*/
function addStudent(username) {
    if (username.length == 0) return false;
    $.ajax({
        type: "POST",
        url: "/teacher/add-student/" + encodeURIComponent(username) + "/" + classId,
        success: function (data) {
            if (data == true) {
                $("#successful-add-student-modal").modal("toggle");
                $("#student-list").load(" #student-list > *");
            } else {
                $("#unsuccessful-add-student-modal").modal("toggle");
            }
        },
        error: function () {
            alert("An error has occured :-(");
        },
        complete: function () {
            clearInput("#add-student-username");
        }
    })
}

function updateTitle(title, desc){
    $.ajax({
        type: "POST",
        url: "/teacher/update-title/" + encodeURIComponent(title) + "/" + encodeURIComponent(desc) + "/" + classId,
        success: function (data) {
            if (data == true) {
                $("#settings-modal").modal("toggle");
                $("#title").load(" #title > *");
                
            } else {
                alert("An error has occured. The title / desc is either too long or you do not have permission.")
            }
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}
//thes functions is called by an onclick
function selectStudent(id) {
    $.ajax({
        type: "GET",
        url: "/teacher/get-student-info/" + id,
        success: function (data) {
            $("#selected-username").html(data.username);
            $("#selected-name").html(data.fullName);
            $("#selected-remove").prop("onclick", null).off("click");
            $("#selected-remove").attr('onClick','removeStudent(' + data.id +')');
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

function removeStudent(id) {
    alert("rip" + id + $("#ban-user").prop("checked"));
    
    if($("#ban-user").prop("checked") == true) {
        alert("ultra rip")
    } else {
        alert("meh");
    }
}

//these functions are used throughout the other functions
function clearInput(id) {
    $(id).val("");
}

function showError(code) {
    switch (code) {
        case 1:
            $("#live-error1").show();
            break;
        case 2:
            $("#live-error2").show();
            break;

    }

}