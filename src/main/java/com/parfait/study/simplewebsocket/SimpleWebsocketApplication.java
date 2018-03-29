package com.parfait.study.simplewebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class SimpleWebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleWebsocketApplication.class, args);
	}

	@Controller
	public static class MainController {
		@GetMapping("/")
		public String main() {
			return "redirect:/chat/rooms";
		}
	}
}
