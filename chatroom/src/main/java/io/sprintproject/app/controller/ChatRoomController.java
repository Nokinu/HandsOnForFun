package io.sprintproject.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import io.sprintproject.app.service.ChatRoomService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@Controller
@ServerEndpoint("/chatroom")
public class ChatRoomController {

	private static ChatRoomService chatRoomService;

	@Autowired
	private void setChatRoomService(ChatRoomService chatRoomService) {
		ChatRoomController.chatRoomService = chatRoomService;
	}

	@OnOpen
	public void joinChatRoom(Session session) {
		chatRoomService.addUser(session.getId(), session);
	}

	/**
	 * TODO should use json rather than string for message
	 */
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {

		// private message format - @sendUserID-message content
		if (message.startsWith("@")) {
			message = message.substring(1);
			String sendUserId = message.split("-")[0];
			message = message.split("-")[1];
			chatRoomService.sendMessageToUser(session, message, sendUserId);
		} else { // send message to all
			chatRoomService.sendMessageToAll(session, message);
		}
	}

	@OnClose
	public void onClose(Session session) {
		chatRoomService.removeUser(session.getId());
	}

	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
	}
}
