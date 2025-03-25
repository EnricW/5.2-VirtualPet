package s05.virtualpet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VirtualpetApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualpetApplication.class, args);
	}

}
