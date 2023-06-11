package apbase.online;

import java.util.stream.Collectors;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

import apbase.env.Backend;
import apbase.env.Frontend;

@Configuration(proxyBeanMethods = false)
public class ApBaseWebSecurityConfig {

	@Bean
	@ConfigurationProperties(prefix = "apbase.online.security.front")
	ApBaseOnlineSecurityProperties apBaseOnlineSecurityFrontProperties() {
		return new ApBaseOnlineSecurityProperties();
	}

	@Bean
	@ConfigurationProperties(prefix = "apbase.online.security.back")
	ApBaseOnlineSecurityProperties apBaseOnlineSecurityBackProperties() {
		return new ApBaseOnlineSecurityProperties();
	}

	@Configuration(proxyBeanMethods = false)
	@EnableWebSecurity
	@Frontend
	static class FrontWebSecurityConfig extends WebSecurityConfigurerAdapter {

		private static Logger logger = LoggerFactory.getLogger(FrontWebSecurityConfig.class);

		private ApBaseOnlineSecurityProperties securityProperties;

		public FrontWebSecurityConfig(
				@Named("apBaseOnlineSecurityFrontProperties") ApBaseOnlineSecurityProperties securityProperties) {
			logger.trace("### FrontWebSecurityConfig");
			this.securityProperties = securityProperties;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.formLogin().disable();
			http.httpBasic().disable();
			http.logout().disable();

			// CSRFの設定
			String[] csrgIgnores = securityProperties.getCsrfIgnore().stream()
					.filter(c -> c != null && !c.isBlank()).map(c -> c.trim()).toArray(String[]::new);
			if (csrgIgnores != null && csrgIgnores.length != 0) {
				http.csrf().ignoringAntMatchers(csrgIgnores);
			}
			// CSPの設定
			String policyDirectives = securityProperties.getHeaders().getCsp().getPolicyDirectives().entrySet().stream()
					.map(entry -> {
						String key = entry.getKey();
						String value = entry.getValue().stream().filter(v -> !v.isBlank())
								.collect(Collectors.joining(" "));
						return key + " " + value;
					}).collect(Collectors.joining("; "));
			if (policyDirectives != null && !policyDirectives.isBlank()) {
				HeadersConfigurer<HttpSecurity>.ContentSecurityPolicyConfig csp = http.headers()
						.contentSecurityPolicy(policyDirectives);
				if (securityProperties.getHeaders().getCsp().isReportOnly()) {
					csp.reportOnly();
				}
			}
			
		}

	}

	@Configuration(proxyBeanMethods = false)
	@EnableWebSecurity
	@Backend
	static class BackWebSecurityConfig extends WebSecurityConfigurerAdapter {

		private static Logger logger = LoggerFactory.getLogger(BackWebSecurityConfig.class);

		private ApBaseOnlineSecurityProperties securityProperties;

		public BackWebSecurityConfig(
				@Named("apBaseOnlineSecurityBackProperties") ApBaseOnlineSecurityProperties securityProperties) {
			logger.trace("### BackWebSecurityConfig");
			this.securityProperties = securityProperties;
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.formLogin().disable();
			http.httpBasic().disable();
			http.sessionManagement().disable();
		}

	}

}
