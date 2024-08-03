'use strict'
var memberButtons;
var membersList;
var messageInput;
var chatInput;
var messageList;
var receiver = null;

var uuid = Math.random().toString();

const client = new StompJs.Client({
	brokerURL: "ws://localhost:8081/ws-chat-app"
});

client.onConnect = (frame) => {
	console.log("Connected", frame);

	client.subscribe("/topic/users", (users) => {
		let members = JSON.parse(users.body);
		console.log(members);
		showMembersList(members);
	});

	client.subscribe("/topic/public-messages", (payload) => {
		let message = JSON.parse(payload.body);
		console.log(message);
		showPublicMessage(message);
	});
	client.subscribe("/user/" + userId + "/topic/private-messages", (payload) => {
		let message = JSON.parse(payload.body);
		console.log(message);
		showPrivateMessage(message);
	});

	connected();
};

function connected() {
	sendClientStatus("online");

}

function sendClientStatus(status) {
	client.publish({
		destination: "/app/public/status",
		body: status
	});
}

function showMembersList(members) {
	membersList.innerHTML = "";
	for (let i = 0; i < members.length; i++) {
		if (members[i].username == username) {
			continue;
		}
		if (members[i].online == true) {
			membersList.innerHTML += "<button class='member-button online'>" + members[i].username + "</button>";
		} else {
			membersList.innerHTML += "<button class='member-button offline' disabled>" + members[i].username + "</button>";
		}
	}

	memberButtons = document.querySelectorAll('.member-button');
	memberButtons.forEach(button => {
		if (!button.disabled) {
			button.addEventListener('click', () => {
				if (button.classList.contains('active')) {
					button.classList.remove('active');
					resetToPublicChat();
					receiver = null;
				} else {
					memberButtons.forEach(btn => btn.classList.remove('active'));
					button.classList.add('active');
					const memberName = button.textContent;
					receiver = memberName;
					updateChatForMember(memberName);
				}
			});
		} else {
			if (button.textContent == receiver) {
				resetToPublicChat();
				receiver = null;
			}
		}
	});

}



window.addEventListener("beforeunload", () => {
	sendClientStatus("offline");
	client.deactivate();
});

document.addEventListener('DOMContentLoaded', function() {
	client.activate();
	memberButtons = document.querySelectorAll('.member-button');
	membersList = document.getElementById("membersList");
	messageList = document.getElementById("messages");
	messageInput = document.getElementById("messageInput");
	chatInput = document.getElementById('chatInput');
	chatInput.addEventListener("submit", function(event) {
		console.log(messageInput.value);
		if (messageInput.value) {
			if (receiver) {
				sendPrivateMessage(messageInput.value, receiver);
			} else {
				sendPublicMessage(messageInput.value);
			}
		}
		chatInput.reset();
		event.preventDefault();
	});
});

function sendPublicMessage(message) {
	uuid = Math.random().toString();
	client.publish({
		destination: "/app/public/message",
		body: JSON.stringify({ 'content': message, 'uid': uuid })
	});
}


function showPublicMessage(message) {
	if (message.uid == uuid) {
		messageList.innerHTML += "<div class='message self'><strong>You: </strong>" + message.content + "</div>";
	} else {
		messageList.innerHTML += "<div class='message'><strong>" + message.user + ": </strong>" + message.content + "</div>";
	}
	messageList.scrollTop = messageList.scrollHeight;
}



function sendPrivateMessage(message, receiver) {
	if (receiver) {
		client.publish({
			destination: "/app/private/message",
			body: JSON.stringify({ 'content': message, 'uid': userId, 'receiver': receiver })
		});
	}
}

function showPrivateMessage(message) {
	if (message.user == username) {
		messageList.innerHTML += "<div class='message self'><strong>(Private) You: </strong>" + message.content + "</div>";
	} else {
		messageList.innerHTML += "<div class='message'><strong>(Private) " + message.user + ": </strong>" + message.content + "</div>";
		memberButtons.forEach(button => {
			if (!button.disabled) {
				console.log()
				if(button.textContent == message.user){
					button.click();
				}
			}
		});
	}
	messageList.scrollTop = messageList.scrollHeight;
}


function resetToPublicChat() {
	const chatTitle = document.getElementById('chatTitle');
	chatTitle.textContent = 'Public Chat';
}

function updateChatForMember(memberName) {
	const chatTitle = document.getElementById('chatTitle');
	chatTitle.textContent = `Chat with ${memberName}`;
}

