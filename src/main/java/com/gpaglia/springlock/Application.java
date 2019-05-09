package com.gpaglia.springlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication()
public class Application implements CommandLineRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @SuppressWarnings("unused")
  private final ApplicationContext ctx;

  @Autowired
  public Application(
      ApplicationContext ctx
  ) {
    this.ctx = ctx;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  
  @Override
  public void run(String... args) {
    LOGGER.info("Starting application code...");
  }
  
}
