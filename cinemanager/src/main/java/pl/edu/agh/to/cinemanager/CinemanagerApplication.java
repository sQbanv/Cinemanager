package pl.edu.agh.to.cinemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.edu.agh.to.cinemanager.configuration.CorsProperties;
import pl.edu.agh.to.cinemanager.configuration.RsaKeyProperties;

@EnableConfigurationProperties({RsaKeyProperties.class, CorsProperties.class})
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
@EnableScheduling
public class CinemanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemanagerApplication.class, args);
    }

}
