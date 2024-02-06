package co.com.pruebTecnicaNexosP1MS;

import java.lang.reflect.InvocationTargetException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Ing. Jose Augusto Cupasachoa
 */
@SpringBootApplication
public class PruebaTecnicaNexosP1MsApplication {

	@Autowired
	private Environment environment;

	@Bean
	public DataSource driverManagerDataSource() throws IllegalAccessException, InvocationTargetException, InstantiationException {

		HikariDataSource hikariDataSource = new HikariDataSource();
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setPoolName(this.environment.getProperty("spring.datasource.hikari.pool-name"));
		hikariConfig.setMaxLifetime(Long.valueOf(this.environment.getProperty("spring.datasource.hikari.max-lifetime")));
		hikariConfig.setIdleTimeout(Long.valueOf(this.environment.getProperty("spring.datasource.hikari.idle-timeout")));
		hikariConfig.setConnectionTimeout(Long.valueOf(this.environment.getProperty("spring.datasource.hikari.connection-timeout")));
		hikariConfig.setMinimumIdle(Integer.valueOf(this.environment.getProperty("spring.datasource.hikari.minimum-idle")));
		hikariConfig.setMaximumPoolSize(Integer.valueOf(this.environment.getProperty("spring.datasource.hikari.maximum-pool-size")));
		hikariConfig.setAutoCommit(Boolean.valueOf(this.environment.getProperty("spring.datasource.hikari.auto-commit")));
		hikariConfig.copyStateTo(hikariDataSource);
		hikariDataSource.setJdbcUrl("jdbc:postgresql://db-nexos-pt.cxu646ea42al.us-east-2.rds.amazonaws.com:5432/cbcop");
		hikariDataSource.setUsername("postgres");
		hikariDataSource.setPassword("Root1234");
		hikariDataSource.setDriverClassName("org.postgresql.Driver");
		return hikariDataSource;
	}

	@Bean
	public RestTemplate getresttemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(PruebaTecnicaNexosP1MsApplication.class, args);
	}
}
