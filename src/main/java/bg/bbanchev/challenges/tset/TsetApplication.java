package bg.bbanchev.challenges.tset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class TsetApplication {

	public static void main(String[] args) {
		SpringApplication.run(TsetApplication.class, args);
	}

}
