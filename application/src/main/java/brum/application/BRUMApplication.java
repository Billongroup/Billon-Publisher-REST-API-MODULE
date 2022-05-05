package brum.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = EmbeddedWebServerFactoryCustomizerAutoConfiguration.class)
@EnableSwagger2
@EnableScheduling
@ComponentScan(basePackages = {"brum"})
@EntityScan(basePackages = {"brum.persistence.entity"})
@EnableJpaRepositories(basePackages = {"brum.persistence.repository"})
@ImportResource("classpath:beans.xml")
public class BRUMApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BRUMApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BRUMApplication.class);
    }
}
