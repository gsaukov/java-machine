// <script> Fetch block
    function doFetch(url, method, data, onResponse, extraDetails) {
        var responseStatus;
        var responseStatusText;
        window.event.preventDefault();
        resetTimer();
        startTimer();
        fetch(url, {
            method: method,
            body: data,
        }).then(
            function(response){
                return doCall(response, {url: url, method: method, data: data});
            }
        ).then(
            function(responseBody) {
                stopTimer();
                return doResponse(responseBody, onResponse, extraDetails);
            }
        ).catch(function(error) {
            stopTimer();
            showModal("Error", "<p>" + error.stack + "</p>" + "<p>" + error.message + "</p>");
        });
    }

    function buildData(formElement) {
        const data = new URLSearchParams();
        for (const pair of new FormData(formElement)) {
            data.append(pair[0], pair[1]);
        }
        return data;
    }

    function doCall(response, request) {
        responseStatus = response.status;
        responseStatusText = response.statusText;
        if(response.ok) {
            return response.text();
        } else {
            return response.text() // return the result of the inner promise, which is an error
                .then((text) => {
                    let errorDesc = "<p> URL: " + request.url + "<br> METHOD: " + request.method + "<br> DATA: " + request.data + "</p>"
                        + "<p>RESPONSE STATUS: " + responseStatus + " " + responseStatusText + "</p>"
                        + "<p>RESPONSE BODY: " + text + "</p>";
                    throw new Error(errorDesc);
                });
        }
    }

    function doResponse(text, onResponse, extraDetails) {
        if (extraDetails === undefined) {
            onResponse(text);
        } else {
            onResponse(text, extraDetails);
        }
    }
// </script> Fetch block

// <script> AutoComplete

    const allCityObj = {
        allCityData : null,
        dataUrl : "getallcities",
        minChars : 3,
        autoComplete : null,

        getData : function () {
            return this.allCityData;
        },

        setData : function (data) {
            this.allCityData = JSON.parse(data);
        }
    }

    const allStatesObj = {
        allStatesData : null,
        dataUrl : "getallstates",
        minChars : 1,
        autoComplete : null,

        getData : function () {
            return this.allStatesData;
        },

        setData : function (data) {
            this.allStatesData =  JSON.parse(data);
        }
    }

    const allSymbolsObj = {
        allSymbolsData : null,
        dataUrl : "getallsymbols",
        separator : ',',
        minChars : 2,
        autoComplete: null,

        getData : function () {
            return this.allSymbolsData;
        },

        setData : function (data) {
            this.allSymbolsData =  JSON.parse(data);
        }
    }

    function createSeparatorAutoComplete(targetId, dataObj) {
        window.event.preventDefault();
        if (!dataObj.getData()) {
            let onResponse = function (response) {
                dataObj.setData(response);
            }

            doFetch(dataObj.dataUrl, "GET", null, onResponse);
        }

        let target = document.querySelectorAll(targetId)[0];
        if (!target || target.getAttribute("autocomplete") === null) {
            new autoComplete({
                selector: targetId,
                minChars: dataObj.minChars,
                separator: dataObj.separator,
                cache: false,
                source: function (term, suggest) {
                    let choices = dataObj.getData();
                    let matches = [];
                    term = term.toUpperCase().split(dataObj.separator).pop(-1).trim();
                    if(term.length < dataObj.minChars) return;
                    for (i = 0; i < choices.length; i++){
                        if(choices[i]){
                            if (~choices[i].toUpperCase().indexOf(term)) matches.push(choices[i]);
                        }
                    }
                    suggest(matches);
                },
                onAssign: function(that, v){
                    if(dataObj.separator){
                        let tempVal = that.value;
                        if(tempVal.includes(dataObj.separator)){
                            tempVal = tempVal.substr(0, tempVal.lastIndexOf(dataObj.separator) + 1);
                            that.value = tempVal + v;
                        } else {
                            that.value = v;
                        }
                    } else {
                        that.value = v;
                    }
                }
            })
        }
    }

    function createSimpleAutoComplete(targetId, dataObj) {
        window.event.preventDefault();
        if (!dataObj.getData()) {
            let onResponse = function (response) {
                dataObj.setData(response);
            }

            doFetch(dataObj.dataUrl, "GET", null, onResponse);
        }

        let target = document.querySelectorAll(targetId)[0];
        if (!target || target.getAttribute("autocomplete") === null) {
            new autoComplete({
                selector: targetId,
                minChars: dataObj.minChars,
                cache: false,
                source: function (term, suggest) {
                    let choices = dataObj.getData();
                    let matches = [];
                    term = term.toUpperCase();
                    if (term.length < dataObj.minChars) return;
                    for (i = 0; i < choices.length; i++){
                        if(choices[i]){
                            if (~choices[i].toUpperCase().indexOf(term)) matches.push(choices[i]);
                        }
                    }
                    suggest(matches);
                }
            })
        }
    }
// </script> AutoComplete

// <script> Element removal
    function removeDetails(detailId) {
        window.event.preventDefault();
        let divId = "#" + detailId;
        $(divId).fadeOut("normal", function() {
            $(this).remove();
        });
    }

    function removeDetailsTable(tableId) {
        removeElement(tableId);
        existingDetailsTableId = null
    }

    function removeElement(elementId) {
        window.event.preventDefault();
        removeAllChildren(elementId)
        let element = "#" + elementId;
        $(element).remove();
    }

    function removeAllChildren(elementId) {
        window.event.preventDefault();
        let element = "#" + elementId;
        $(element).empty();
    }
// </script> Element removal
