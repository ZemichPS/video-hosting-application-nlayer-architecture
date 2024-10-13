package by.zemich.videohosting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VideoHostingApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoHostingApplication.class, args);
    }

}
