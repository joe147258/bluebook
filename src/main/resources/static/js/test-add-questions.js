$(document).ready(function () {

    var error = getUrlParameter('error');
    if (error != "") showError(parseInt(error));

    $("#edit-mc-question-form").submit(function (e) {
        e.preventDefault();
        editMultiChoiceQuestion();
    })

    $("#edit-tf-question-form").submit(function (e) {
        e.preventDefault();
        editTrueFalseQuestion();
    })

    $("#edit-input-question-form").submit(function (e) {
        e.preventDefault();
        editInputQuestion();
    })

    $("#change-title-input").on("keyup", function () {
        if ($("#change-title-input").val().includes(";")) {
            $("#semicolon-error").show();
            $("#change-title-btn").attr("disabled", "disabled");
        } else {
            $("#semicolon-error").hide();
            $("#change-title-btn").removeAttr("disabled");
        }
    })

    $("#dueform").submit(function (e) {
        e.preventDefault();
        let duedate = $("#dueDate").val();
        let duetime = $("#dueTime").val();
        setDueDate(duedate, duetime);
    });

    $("#hide-ques").click(function () {
        let icon = $("#hide-ques").html();
        if (icon == "keyboard_arrow_up") {
            $("#question-form-container").hide();
            $("#hide-ques").html("keyboard_arrow_down");
        } else {
            $("#question-form-container").show();
            $("#hide-ques").html("keyboard_arrow_up");
        }

    })

    $("#mc-question-form").submit(function (e) {
        e.preventDefault();
        addMultiChoiceQuestion();
    });

    $("#tf-question-form").submit(function (e) {
        e.preventDefault();
        addTrueFalseQuestion();
    });

    $("#input-question-form").submit(function (e) {
        e.preventDefault();
        addInputQuestion();
    });

    $("#question-type").change(function () {
        let type = $(this).val();
        hideAllQuestionTabs();
        switch (type) {
            case "MULTI_CHOICE":
                $("#mc-question-form").show();
                break;
            case "TRUE_FALSE":
                $("#tf-question-form").show();
                break;
            case "INPUT":
                $("#input-question-form").show();
                break;
        }
    })

    $(".fb-btn").click(function () {
        let newFbtype = $(this).val();
        if (newFbtype == currentFbType) return false;
        changeFeedbackType(newFbtype);

    })
})

//functions to be used in document.ready
function addMultiChoiceQuestion() {
    let incorrectAnswers = [
        $("#mc-incorrect-answer1").val(),
        $("#mc-incorrect-answer2").val(),
        $("#mc-incorrect-answer3").val()
    ];

    let questionString = $("#mc-question-string").val();
    let correctAnswer = $("#mc-correct-answer").val();

    $.ajax({
        type: "POST",
        url: testId + "/add-multi-choice/" + "?questionString=" + questionString + "&correctAnswer=" + correctAnswer +
            "&incorrectAnswer1=" + incorrectAnswers[0] + "&incorrectAnswer2=" + incorrectAnswers[1] + "&incorrectAnswer3=" + incorrectAnswers[2],
        success: function (data) {
            if (data == true) {
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                $("#semicolon-warning").show();
            }
        },
        error: function () {
            alert("An error has occured :-(");
        },
        complete: function () {
            clearInput("#mc-question-string");
            clearInput("#mc-correct-answer");
            clearInput("#mc-incorrect-answer1");
            clearInput("#mc-incorrect-answer2");
            clearInput("#mc-incorrect-answer3");
        }
    })
}

function addTrueFalseQuestion() {

    let questionString = $("#tf-question-string").val();
    let correctAnswer = $("#tf-correct-answer").val();

    $.ajax({
        type: "POST",
        url: testId + "/add-true-false/" + "?questionString=" + questionString + "&correctAnswer=" + correctAnswer,
        success: function (data) {
            if (data == true) {
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                $("#semicolon-warning").show();
            }
        },
        error: function () {
            alert("An error has occured :-(");
        },
        complete: function () {
            clearInput("#tf-question-string");
        }
    })
}

function addInputQuestion() {

    let questionString = $("#input-question-string").val();
    let correctAnswer = $("#input-correct-answer").val();
    let distance = $("#input-distance").val();

    $.ajax({
        type: "POST",
        url: testId + "/add-input/" + "?questionString=" + questionString + "&correctAnswer=" + correctAnswer + "&distance=" + distance,
        success: function (data) {
            if (data == true) {
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                $("#semicolon-warning").show();
            }
        },
        error: function () {
            alert("An error has occured :-(");
        },
        complete: function () {
            clearInput("#input-question-string");
            clearInput("#input-correct-answer");
        }
    })
}

function changeFeedbackType(fbType) {
    $.ajax({
        type: "POST",
        url: testId + "/change-fbtype/" + fbType,
        success: function (data) {
            if (data == true) {
                currentFbType = fbType;
                $("#fb-type").html(fbType);
            } else if (data == false) {
                alert("An error has occured :-(");
            }
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

function updateTestTitle() {
    let newTitle = $("#change-title-input").val();
    $.ajax({
        type: "POST",
        url: testId + "/change-title?newTitle=" + newTitle,
        success: function (data) {
            if (data == true) {
                $("#title-container").load(" #title-container > * ")
                $("#change-title").modal('toggle');
            } else if (data == false) {
                alert("An error has occured :-(");
            }
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

function setDueDate(duedate, duetime) {
    $.ajax({
        type: "POST",
        url: testId + "/set-due?date=" + duedate + "&time=" + duetime,
        success: function (data) {
            if (data == true) {
                $("#saved-due").show();
                $("#saved-due").delay(3000).fadeOut();
            } else if (data == false) {
                alert("An error has occured :-(");
            }
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

function openEditModal(qId) {
    $.ajax({
        type: "GET",
        url: testId + "/get-ques-info?qId=" + qId,
        success: function (data) {
            switch (data.type) {
                case "multiChoice":
                    $("#edit-ques-modal-multi").modal('toggle');
                    $("#edit-mc-question-string").val(data.questionString);
                    $("#edit-mc-correct-answer").val(data.correctAnswer);
                    $("#edit-mc-incorrect-answer1").val(data.incorrectAnswers[0]);
                    $("#edit-mc-id").val(data.quesId);
                    if (data.incorrectAnswers[1] != "") $("#edit-mc-incorrect-answer2").val(data.incorrectAnswers[1]);
                    if (data.incorrectAnswers[2] != "") $("#edit-mc-incorrect-answer3").val(data.incorrectAnswers[2]);
                    break;
                case "trueFalse":
                    $("#edit-ques-modal-tf").modal('toggle');
                    $("#edit-tf-question-string").val(data.questionString);
                    $("#edit-tf-id").val(data.quesId);
                    //this shows the selected option
                    if(data.correctAnswer == "true") {
                        $("#tf-opt-f").removeAttr("selected");
                        $("#tf-opt-t").attr('selected', 'selected');
                    } else {
                        $("#tf-opt-t").removeAttr("selected");
                        $("#tf-opt-f").attr('selected', 'selected');
                    }
                    break;
                case "input":
                    $("#edit-ques-modal-input").modal('toggle');
                    $("#edit-input-question-string").val(data.questionString);
                    $("#edit-input-correct-answer").val(data.correctAnswer);
                    $("#edit-input-distance").val(data.distance);
                    $("#edit-input-id").val(data.quesId);
                    break;
                default:
                    alert("An error occured :-(");
                    break;
            }

        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

function editMultiChoiceQuestion() {
    let qId = $("#edit-mc-id").val();
    let incorrectAnswers = [
        $("#edit-mc-incorrect-answer1").val(),
        $("#edit-mc-incorrect-answer2").val(),
        $("#edit-mc-incorrect-answer3").val()
    ];

    let questionString = $("#edit-mc-question-string").val();
    let correctAnswer = $("#edit-mc-correct-answer").val();

    $.ajax({
        type: "POST",
        url: testId + "/edit-question?qId=" + qId + "&questionString=" + questionString + "&correctAnswer=" 
        + correctAnswer + "&incorrectAnswer1=" + incorrectAnswers[0] + "&incorrectAnswer2=" + incorrectAnswers[1] 
        + "&incorrectAnswer3=" + incorrectAnswers[2] + "&type=MULTI",
        success: function (data) {
            if(data == true) {
                $("#edit-ques-modal-multi").modal('toggle');
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                $("#semicolon-warning1").show();
            }
        },
        error: function () {
            alert("An error has occured :-(");
        }
    })
}

function editTrueFalseQuestion() {
    let questionString = $("#edit-tf-question-string").val();
    let correctAnswer = $("#edit-tf-correct-answer").val();
    let qId = $("#edit-tf-id").val();
    $.ajax({
        type: "POST",
        url: testId + "/edit-question/" + "?qId=" + qId + "&questionString=" + questionString + "&correctAnswer=" + correctAnswer + "&type=BOOL",
        success: function (data) {
            if (data == true) {
                $("#edit-ques-modal-tf").modal('toggle');
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                $("#semicolon-warning2").show();
            }
        },
        error: function () {
            alert("An error has occured :-(");
        },
        complete: function () {
            clearInput("#tf-question-string");
        }
    })
}

function editInputQuestion() {
    let questionString = $("#edit-input-question-string").val();
    let correctAnswer = $("#edit-input-correct-answer").val();
    let qId = $("#edit-input-id").val();
    let distance = $("#edit-input-distance").val();
    $.ajax({
        type: "POST",
        url: testId + "/edit-question/" + "?qId=" + qId + "&questionString=" + questionString + "&correctAnswer=" + correctAnswer + "&distance=" + distance + "&type=INPUT",
        success: function (data) {
            if (data == true) {
                $("#edit-ques-modal-input").modal('toggle');
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                $("#semicolon-warning2").show();
            }
        },
        error: function () {
            alert("An error has occured :-(");
        },
        complete: function () {
            clearInput("#tf-question-string");
        }
    })
}

function hideSemicolonWarning() {
    $("#semicolon-warning").hide();
}
function hideSemicolonWarning1() {
    $("#semicolon-warning1").hide();
}
function hideSemicolonWarning2() {
    $("#semicolon-warning2").hide();
}
function hideSemicolonWarning3() {
    $("#semicolon-warning3").hide();
}
function hideErrorAlert() {
    $("#error-alert1").hide();
}

//these functions are used throughout the other functions
function clearInput(id) {
    $(id).val("");
}

function hideAllTabs() {
    $("#students-tab").hide();
    $("#test-tab").hide();
    $("#announcement-tab").hide();
    $("#forum-tab").hide();
    $(".nav-item").removeClass("selected-nav");
}

function hideAllQuestionTabs() {
    $("#mc-question-form").hide();
    $("#tf-question-form").hide();
    $("#input-question-form").hide();
}

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
            $("#error-alert1").show();
            break;

    }

}