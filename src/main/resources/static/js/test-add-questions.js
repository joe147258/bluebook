$(document).ready(function() {

    $("#add-question-form").submit(function (e) {
        e.preventDefault();
        var type = $("#question-type").val();

        switch(type) {
            case "MULTI_CHOICE":
                addMultiChoiceQuestion()
                break;
            case "etc, etc":
                
                break;
        }
    });
})

//functions to be used in document.ready
function addMultiChoiceQuestion() {
    let incorrectAnswers = [
        $("#mc-incorrect-answer1").val(), 
        $("#mc-incorrect-answer2").val(), 
        $("#mc-incorrect-answer3").val()
    ]; 
    var questionString = $("#question-string").val();
    var correctAnswer = $("#mc-correct-answer").val();
    $.ajax({
        type: "POST",
        url: testId + "/add-multi-choice/"  + "?questionString=" + questionString + "&correctAnswer=" + correctAnswer + 
            "&incorrectAnswer1=" + incorrectAnswers[0] + "&incorrectAnswer2=" + incorrectAnswers[1] + "&incorrectAnswer3=" + incorrectAnswers[2],
        success: function (data) {
            if(data == true) {
                alert("nice - just make this a refresh of the question list me thinks");
            } else if (data == false) {
                alert("An error has occured :-(");
            }
        },
        error: function () {
            alert("An error has occured :-(");
        },
        complete: function () {
            clearInput("#question-string");
            clearInput("#mc-correct-answer");
            clearInput("#mc-incorrect-answer1");
            clearInput("#mc-incorrect-answer2");
            clearInput("#mc-incorrect-answer3");
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