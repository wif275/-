package diplom.com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import diplom.com.service.UserService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
class WebSecurityConfig {

	@Autowired
	private UserService UserService;

    @Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {
		// @formatter:off
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/registration", "/static/**", "/activate/*", "/data", "/img/*", "/calculator/*", "/reviews/**", "/news").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout(LogoutConfigurer::permitAll)
			.rememberMe((rememberMe) -> rememberMe
				.tokenValiditySeconds(60 * 60)
		    );
			

		return http.build();
	}



    @Autowired
    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(UserService)
		.passwordEncoder(passwordEncoder);

	}

}