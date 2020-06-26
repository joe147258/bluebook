$(document).ready(function () {


    $("#add-student-form").submit(function (e) {
        e.preventDefault();
        addStudent($("#add-student-username").val());
    });
    
    $("#filterStudents").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#student-list li").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $("#filterStudentsBanned").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#banned-list li").filter(function () {
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
    
    $(".tab").click(function() {
        $(this).blur(); 
        hideAllTabs();//this function hides every tab and gets rid of selected nav
        $(this).parent().addClass("selected-nav"); // adds the selected tab to the parent
        var tab = $(this).val();

        switch(tab) {
            case "TEST_TAB":
                $("#test-tab").show();
                break;
            case "STUDENTS_TAB":
                $("#students-tab").show();
                break;
            case "ANNOUNCEMENT_TAB":
                $("#announcement-tab").show();
                break;
            case "FORUM_TAB":
                $("#forum-tab").show();
                break;
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
            if(data == false){
                alert("An error has occured :-(");
                return false;
            }
            $("#selected-username").html(data.username);
            $("#selected-name").html(data.fullName);
            $("#selected-remove").removeAttr("disabled");
            $("#selected-remove").prop("onclick", null).off("click");
            $("#selected-remove").attr('onClick','removeStudent(' + data.id +')');
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

function removeStudent(id) {
    var url = "/teacher/kick-student/" + id + "/" + classId + "?ban=";
    if($("#ban-user").prop("checked") == true) {
        url += true;
    } else {
        url += false;
    }
    $.ajax({
        type: "POST",
        url: url,
        success: function (data) {
            $("#remove-student-modal").modal('toggle');
            if(data == true) {
                $("#successful-kick-student").modal('toggle');
                $("#selected-username").html("No one selected");
                $("#selected-name").html("-");
                $("#selected-remove").prop("onclick", null).off("click");
                $("#selected-remove").attr("disabled", "disabled");
            } else if (data == false) {
                $("#unsuccessful-kick-student").modal('toggle');
            }
            $("#student-list").load(" #student-list > *");
            $("#banned-list").load(" #banned-list > * ")
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

function pardonStudent(id) {
    $.ajax({
        type: "POST",
        url: "/teacher/pardon-student/" + id + "/" + classId,
        success: function (data) {
            if(data == true) {
                $("#settings-modal").modal("toggle");
                $("#successful-pardon-student").modal("toggle");
            } else if (data == false){
                alert("An error has occured :-(");
            }
            $("#banned-list").load(" #banned-list > *");
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

//these functions are used throughout the other functions
function clearInput(id) {
    $(id).val("");
}
function hideAllTabs(){
    $("#students-tab").hide();
    $("#test-tab").hide();
    $("#announcement-tab").hide();
    $("#forum-tab").hide();
    $(".nav-item").removeClass("selected-nav");
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