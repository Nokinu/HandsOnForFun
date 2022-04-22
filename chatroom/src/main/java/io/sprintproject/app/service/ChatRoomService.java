package io.sprintproject.app.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import jakarta.websocket.Session;

@Service
public class ChatRoomService {

	private static Map<String, Session> onlineUsers = new ConcurrentHashMap<String, Session>(100);

	public void addUser(String id, Session session) {
		onlineUsers.putIfAbsent(id, session);
		System.out.println("Welcome User : " + id);
		System.out.println("Current total users in chat room : " + onlineUsers.size());
	}

	public void removeUser(String id) {
		onlineUsers.remove(id);
	}

	public void sendMessageToUser(Session session, String message, String sendUserId) throws IOException {
		if (onlineUsers.containsKey(sendUserId)) {
			onlineUsers.get(sendUserId).getBasicRemote().sendText(session.getId() + " : " + message);
		} else {
			onlineUsers.get(session.getId()).getBasicRemote().sendText("User " + sendUserId + " is offline");
		}
	}

	public void sendMessageToAll(Session session, String message) {
		onlineUsers.forEach((id, s) -> {
			try {
				s.getBasicRemote().sendText(session.getId() + " to everyone : " + message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
