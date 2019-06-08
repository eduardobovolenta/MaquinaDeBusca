package com.maquinadebusca.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

  public static void main (String[] args) {
  System.setProperty("server.servlet.context-path", "/maquina-de-busca");
    SpringApplication.run (AppApplication.class, args);
  }
}
