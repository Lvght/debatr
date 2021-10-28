var xmlHttp;

function genericXmlHttpRequest(route, method, parameters, div) {
    if (typeof XMLHttpRequest !== "undefined") {
        xmlHttp = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    if (xmlHttp === null) {
        alert("Browser does not support XMLHTTP Request");
        return;
    }

    var url = method == "GET" ? `${route}?${parameters}` : route;
    xmlHttp.onreadystatechange = () => genericDivChange(div);
    xmlHttp.open(method, url, true);
    xmlHttp.send(method == "POST" ? parameters : null);

}

function genericDivChange(div) {
    if (xmlHttp.readyState === 4 || xmlHttp.readyState === "complete") {
        document.getElementById(div).innerHTML = xmlHttp.responseText;
    }
}