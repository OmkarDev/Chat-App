const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/web-socket-endpoint'
});
const connectBtn = document.getElementById("connect");
const disconnectBtn = document.getElementById("disconnect");
connectBtn.addEventListener("click",connect);
disconnectBtn.addEventListener("click",disconnect);

function connect(){
	stompClient.activate();
	connectBtn.disabled = true;
	disconnectBtn.disabled = false;
}

function disconnect(){
	stompClient.deactivate();
	connectBtn.disabled = false;
	disconnectBtn.disabled = true;
}

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
//    stompClient.subscribe('/topic/greetings', (greeting) => {
//		console.log(greeting);
//        showGreeting(JSON.parse(greeting.body).content);
//    });
};


//stompClient.onConnect = (frame) => {
//    setConnected(true);
//    console.log('Connected: ' + frame);
//    stompClient.subscribe('/topic/greetings', (greeting) => {
//		console.log(greeting);
//        showGreeting(JSON.parse(greeting.body).content);
//    });
//};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

//function setConnected(connected) {
//    $("#connect").prop("disabled", connected);
//    $("#disconnect").prop("disabled", !connected);
//    if (connected) {
//        $("#conversation").show();
//    }
//    else {
//        $("#conversation").hide();
//    }
//    $("#greetings").html("");
//}

//function connect() {
//    stompClient.activate();
//}
//
//function disconnect() {
//    stompClient.deactivate();
//    setConnected(false);
//    console.log("Disconnected");
//}
//
//function sendName() {
//    stompClient.publish({
//        destination: "/app/hello",
//        body: JSON.stringify({'sender': $("#name").val(),'content':"what is this right"})
//    });
//}
//
//function showGreeting(message) {
//	console.log(message);
//    $("#greetings").append("<tr><td>" + message + "</td></tr>");
//}
//
//$(function () {
//    $("form").on('submit', (e) => e.preventDefault());
//    $( "#connect" ).click(() => connect());
//    $( "#disconnect" ).click(() => disconnect());
//    $( "#send" ).click(() => sendName());
//});