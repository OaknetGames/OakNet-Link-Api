(function () {
    var request = {
        "method": "GET",
        "url": "http://chromely.com/pagecontent/pollOakNetLinkStatus",
        "parameters": null,
        "postData": null,
    };
    window.cefQuery({
        request: JSON.stringify(request),
        onSuccess: function (response) {
            document.getElementById("connectionStateLabel").innerHTML = JSON.parse(response).Data;
        }, onFailure: function (err, msg) {
            console.log(err, msg);
        }
    });


    var request = {
        "method": "GET",
        "url": "http://chromely.com/pagecontent/pollOakNetLinkLog",
        "parameters": null,
        "postData": null,
    };
    window.cefQuery({
        request: JSON.stringify(request),
        onSuccess: function (response) {
            document.getElementById("logField").innerHTML = JSON.parse(response).Data;
        }, onFailure: function (err, msg) {
            console.log(err, msg);
        }
    });
    setTimeout(arguments.callee, 500);
})();

function requestGameCards() {
    var request = {
        "method": "GET",
        "url": "http://chromely.com/pagecontent/gamecards",
        "parameters": null,
        "postData": null,
    };
    window.cefQuery({
        request: JSON.stringify(request),
        onSuccess: function (response) {
            document.getElementById("cards").innerHTML = JSON.parse(response).Data;
        }, onFailure: function (err, msg) {
            console.log(err, msg);
        }
    });
}

function requestMyServers() {
    var request = {
        "method": "GET",
        "url": "http://chromely.com/pagecontent/myservers",
        "parameters": null,
        "postData": null,
    };
    window.cefQuery({
        request: JSON.stringify(request),
        onSuccess: function (response) {
            document.getElementById("tab-content-2").innerHTML = JSON.parse(response).Data;
        }, onFailure: function (err, msg) {
            console.log(err, msg);
        }
    });
}

function gameCardPressed(cardId, displayName) {
    document.getElementById("server-list-title").innerHTML = "Server List (" + displayName + ")";
    new mdb.Modal(document.getElementById("server-list-modal")).show();
}

window.onload = function () {
    requestGameCards();
    requestMyServers();

    new mdb.Modal(document.getElementById("enter-canonical-name-modal")).show();
}

function setCanonicalName() {
    var request = {
        "method": "POST",
        "url": "http://chromely.com/pagecontent/setCanonicalName",
        "parameters": { 'name': document.getElementById("canonicalNameInput").value},
        "postData": null,
    };
    console.log("request");
    window.cefQuery({
        request: JSON.stringify(request),
        onSuccess: function (response) {
            console.log("Success");
        }, onFailure: function (err, msg) {
            console.log(err, msg);
        }
    });
}
