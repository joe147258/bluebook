$(document).ready(function () {
    //This variable is whether its an aritst, track, album, etc
    //defaults on artist
    var lastFmType = "ARTIST";

    $(".lastfm-type").click(function () {
        //changes the selection on the bar
        $(".lastfm-type").removeClass("selected");
        $(this).addClass("selected");
        $(this).blur();
        //updated the type value
        lastFmType = $(this).val();
        //makes 7 days selected
        $(".lastfm-time-frame").removeClass("selected");
        $("#nav-7-days").addClass("selected");
        //nows runs code to display 7 day information
        switch (lastFmType) {
            case "ARTIST":
                getAndDisplayArtistJSON(this.value);
                break;
            case "ALBUM":
                getAndDisplayAlbumJSON(this.value);
                break;
            case "TRACK":
                getAndDisplayTrackJSON(this.value);
                break;

        }
    })

    $(".lastfm-time-frame").click(function () {
        switch (lastFmType) {
            case "ARTIST":
                getAndDisplayArtistJSON(this.value);
                break;
            case "ALBUM":
                getAndDisplayAlbumJSON(this.value);
                break;
            case "TRACK":
                getAndDisplayTrackJSON(this.value);
                break;

        }


        $(".lastfm-time-frame").removeClass("selected");
        $(this).addClass("selected");
        $(this).blur();

    })

    $("#exit-lastfm-alert").click(function () {
        closeLastFmAlert();
    })

    //runs at the start of this page
    getAndDisplayArtistJSON("WEEK");

})

//functions used in the document.ready
function closeLastFmAlert() {
    $("#alert-lastfm").hide();
}

function getAndDisplayArtistJSON(timePeriod) {
    if (lastFmUserName != "null") {
        loadBar("ON");
        $.ajax({
            type: "GET",
            url: "/" + lastFmUserName + "/get-artists?timePeriod=" + timePeriod,
            success: function (data) {
                displayArtists(data);
            },
            error: function () {
                alert("An error has occured :(");
                loadBar("OFF");
            },
            complete: function () {
                loadBar("OFF");
            }
        })

    }
}

function getAndDisplayAlbumJSON(timePeriod) {
    if (lastFmUserName != "null") {
        loadBar("ON");
        $.ajax({
            type: "GET",
            url: "/" + lastFmUserName + "/get-albums?timePeriod=" + timePeriod,
            success: function (data) {
                displayAlbums(data);
            },
            error: function () {
                alert("An error has occured :(");
                loadBar("OFF");
            },
            complete: function () {
                loadBar("OFF");
            }
        })

    }
}

function getAndDisplayTrackJSON(timePeriod) {
    if (lastFmUserName != "null") {
        loadBar("ON");
        $.ajax({
            type: "GET",
            url: "/" + lastFmUserName + "/get-tracks?timePeriod=" + timePeriod,
            success: function (data) {
                displayTracks(data);
            },
            error: function () {
                alert("An error has occured :(");
                loadBar("OFF");
            },
            complete: function () {
                loadBar("OFF");
            }
        })

    }
}

/*
These are the display function that is used to dynamically add the needed html updated by the JSON
Below are some examples when the HTML is compiled on the client's computer:

    displayArtists:
        <div class="card" id="card0"> <img class="card-img-top"
                src="https://lastfm.freetls.fastly.net/i/u/174s/fcb30d7819bb349545ca6941fe9070a2.png" width="200px"
                height="200px">
            <div class="card-body" id="card-body0">
                <h6 class="card-title custom-card-title">Death Valley Girls</h6>
                <div class="overflow-horizontal">16 listen(s) <small>(scrobbles)</small> </div>
            </div><a href="#" class="btn btn-square btn-main btn-block">View Artist Page</a>
        </div>

    displayAlbums:
        <div class="card" id="card0"> <img class="card-img-top"
                src="https://lastfm.freetls.fastly.net/i/u/174s/2f12af9bfd054aac52d6f8a89068837f.jpg" width="200px"
                height="200px">
            <div class="card-body" id="card-body0">
                <h6 class="card-title custom-card-title mb-0">Every Bad</h6>
                <div class="overflow-horizontal mb-2">Porridge Radio</div>
                <div class="overflow-horizontal">13 listen(s) <small>(scrobbles)</small> </div>
            </div><a href="#" class="btn btn-square btn-main btn-block">View Artist Page</a>
        </div>

    displayTracks:
        <div class="track-item overflow-horizontal" id="track-item0"><i class="material-icons"
                style="vertical-align: super; margin-right: 5px;">music_note</i>
            <div class="track-spacing-long overflow-horizontal">
                <h5>End Of Daze (with EARTHGANG &amp; JID feat. Jurdan Bryant, Mereba &amp; Hollywood JB)</h5>
            </div>
            <div class="track-spacing overflow-horizontal">
                    <h5>Spillage VIllage</h5>
            </div>
            <div class="track-spacing overflow-horizontal">
                <h5>15 listens</h5>
            </div><i class="material-icons" style="text-align: right; vertical-align: super;">star_border</i>
        </div>
*/
function displayArtists(arr) {
    var uniqueId = 0;
    for (x of arr) {
        $("#lastfm-data-container").append('<div class="card" id=' + 'card' + uniqueId + '> </div>');
        $("#card" + uniqueId).append('<img class="card-img-top" src="' + x.imgUrl + '" width="200px" height="200px" />');
        $("#card" + uniqueId).append('<div class="card-body" id="card-body' + uniqueId + '"> <h6 class="card-title custom-card-title">' + x.title + '</h6> </div>');
        $("#card-body" + uniqueId).append('<div class="overflow-horizontal">' + x.listens + ' listen(s) <small>(scrobbles)</small> </div>');
        $("#card" + uniqueId).append('<a href="#" class="btn btn-square btn-main btn-block">View Artist Page</a>');

        uniqueId++;
    }
}
function displayAlbums(arr) {
    var uniqueId = 0;
    for (x of arr) {
        $("#lastfm-data-container").append('<div class="card" id=' + 'card' + uniqueId + '> </div>');
        $("#card" + uniqueId).append('<img class="card-img-top" src="' + x.imgUrl + '" width="200px" height="200px" />');
        $("#card" + uniqueId).append('<div class="card-body" id="card-body' + uniqueId + '"> <h6 class="card-title custom-card-title mb-0">' + x.title + '</h6> </div>');
        $("#card-body" + uniqueId).append('<div class="overflow-horizontal mb-2">' + x.artist +'</div>');
        $("#card-body" + uniqueId).append('<div class="overflow-horizontal">' + x.listens + ' listen(s) <small>(scrobbles)</small> </div>');
        $("#card" + uniqueId).append('<a href="#" class="btn btn-square btn-main btn-block">View Artist Page</a>');

        uniqueId++;
    }
}
function displayTracks(arr) {
    var uniqueId = 0;
    for (x of arr) {
        $("#lastfm-data-container").append('<div class="track-item overflow-horizontal" id="track-item' + uniqueId + '" ></div>');
        $("#track-item" + uniqueId).append('<i class="material-icons" style="vertical-align: super; margin-right: 5px;">music_note</i>');
        $("#track-item" + uniqueId).append('<div class="track-spacing-long overflow-horizontal"><h5>' + x.title + '</h5></div>');
        $("#track-item" + uniqueId).append('<div class="track-spacing overflow-horizontal"><h5>' + x.artist + '</h5></div>');
        $("#track-item" + uniqueId).append('<div class="track-spacing overflow-horizontal"><h5>' + x.listens + ' listens' + '</h5></div>');
        $("#track-item" + uniqueId).append('<i class="material-icons" style="text-align: right; vertical-align: super;">star_border</i>');
        uniqueId++;
    }
}

/*
This function takes "ON" or "OFF"
to show a loading bar or not
*/
function loadBar(status) {
    if (status == "ON") {
        $("#lastfm-data-container").empty();
        $("#lastfm-data-container").append('<button id="loadbar" class="btn btn-main btn-square" type="button" disabled> <span class="spinner-border spinner-border-sm"></span><span> Loading...</span></button>');

    } else if (status == "OFF") {
        $("#loadbar").remove();
    } else {
        console.log("Developer issue - this function must only take the inputs 'ON' or 'OFF'");
        alert("An error has occured - refer to console")
    }
}