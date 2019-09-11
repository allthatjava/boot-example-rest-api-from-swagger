package brian.example.boot.rest.config.logging;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

	@Bean
	public LoggingFilter dispatcherServlet() {
		return new LoggingFilter();
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
    public ServletRegistrationBean dispatcherRegistration() {
        return new ServletRegistrationBean(dispatcherServlet());
    }
}
