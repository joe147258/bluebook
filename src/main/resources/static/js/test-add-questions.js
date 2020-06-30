$(document).ready(function() {
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

    $("#question-type").change(function(){
        let type = $(this).val();
        hideAllQuestionTabs();
        switch(type) {
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
    
    $(".fb-btn").click(function(){
        let newFbtype = $(this).val();
        if(newFbtype == currentFbType) return false;
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
        url: testId + "/add-multi-choice/"  + "?questionString=" + questionString + "&correctAnswer=" + correctAnswer + 
            "&incorrectAnswer1=" + incorrectAnswers[0] + "&incorrectAnswer2=" + incorrectAnswers[1] + "&incorrectAnswer3=" + incorrectAnswers[2],
        success: function (data) {
            if(data == true) {
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                alert("An error has occured :-(");
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
        url: testId + "/add-true-false/"  + "?questionString=" + questionString + "&correctAnswer=" + correctAnswer,
        success: function (data) {
            if(data == true) {
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                alert("An error has occured :-(");
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

    $.ajax({
        type: "POST",
        url: testId + "/add-input/"  + "?questionString=" + questionString + "&correctAnswer=" + correctAnswer,
        success: function (data) {
            if(data == true) {
                $("#question-list").load(" #question-list > *");
            } else if (data == false) {
                alert("An error has occured :-(");
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
            if(data == true) {
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

function hideAllQuestionTabs() {
    $("#mc-question-form").hide();
    $("#tf-question-form").hide();
    $("#input-question-form").hide();
}