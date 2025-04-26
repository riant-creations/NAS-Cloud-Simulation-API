package com.example.nascloudsimulation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;

@SpringBootApplication
public class NasCloudSimulationApplication {

    public static void main(String[] args) {
        // Use this instead of SpringApplication.run() to help with cache issues
        new SpringApplicationBuilder(NasCloudSimulationApplication.class)
            .properties("spring.objenesis.ignore=true") // Helps with CGLIB cache issues
            .run(args);
    }
    
    // These beans ensure error components are available if not already defined
    @Bean
    @ConditionalOnMissingBean
    public ErrorAttributes errorAttributes() {
        return new CustomErrorAttributes();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public ErrorController errorController(ErrorAttributes errorAttributes) {
        return new CustomErrorController();
    }
}
