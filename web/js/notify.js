
var Notification = window.Notification || window.mozNotification || window.webkitNotification;

Notification.requestPermission(function (permission) {
    // console.log(permission);
});

function show(title, message) {
    var instance = new Notification(
            title, {
                body: message
            }
    );

    instance.onclick = function () {
        // Something to do
    };
    instance.onerror = function () {
        // Something to do
    };
    instance.onshow = function () {
        // Something to do
    };
    instance.onclose = function () {
        // Something to do
    };
}

//<button type="button" onclick="show()">Notify me!</button>