package cupet.com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@PropertySource("classpath:/secretpath.properties")
public class MyCupetBootLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyCupetBootLoginApplication.class, args);
	}

}
