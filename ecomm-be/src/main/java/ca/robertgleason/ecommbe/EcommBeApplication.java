package ca.robertgleason.ecommbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the E-commerce Backend application.
 *
 * @SpringBootApplication combines:
 * - @Configuration
 * - @EnableAutoConfiguration
 * - @ComponentScan
 */
@SpringBootApplication
public class EcommBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommBeApplication.class, args);
    }

}
