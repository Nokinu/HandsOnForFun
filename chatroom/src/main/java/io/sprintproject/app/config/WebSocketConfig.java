package io.sprintproject.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@Component
public class WebSocketConfig {

	@Bean("serverEndpointExporter")
	public ServerEndpointExporter getServerEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
