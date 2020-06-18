package com.bas.backend;

import com.bas.backend.beans.PropertiesBackend;
import com.bas.backend.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties(PropertiesBackend.class)
public class BackendApplication {

    @Autowired
    JdbcTemplate jdbcTemplate;



	@Autowired
	@Qualifier("transferRepository")
	private TransferRepository transferRepository;

    public static void main(String[] args) {
    	SpringApplication.run(BackendApplication.class, args);

    }


	public void run(String... args) {
		System.out.println("bassim");

	}
}
