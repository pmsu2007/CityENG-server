package kr.city.eng.pendding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class PenddingApplication {

  public static void main(String[] args) {
    SpringApplication.run(PenddingApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void init(ApplicationReadyEvent event) {
    ApplicationContext context = event.getApplicationContext();

    Environment env = context.getBean(Environment.class);
    log.info("app: {}", env.getProperty("info.app.description"));
    log.info("version: {}", env.getProperty("info.app.version"));
  }

}
