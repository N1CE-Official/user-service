package ru.ws.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("ru.ws")
@EnableJpaRepositories("ru.ws")
@EntityScan("ru.ws")
@ServletComponentScan("ru.ws.utils.security")
@EnableCircuitBreaker
public class RestApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(RestApplication.class);

	@PostConstruct
    public void postConstruct() {
       logger.debug("postConstruct...");          
    }
	
	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

    @PreDestroy
    public static void preShutdown() {
    	logger.debug("preShutdown...");
    }
    
    /*@Bean(name="crdDataSource")
    @Primary
	public DataSource crdDataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName( "oracle.jdbc.OracleDriver");
		dataSourceBuilder.url("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=tcp)(HOST=10.166.22.113)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=DAIMLER_DE_TEMP)))");
		dataSourceBuilder.username("DAIMLER_DE");
		dataSourceBuilder.password("DAIMLER_DE");

		return dataSourceBuilder.build();
	}*/
   
}

