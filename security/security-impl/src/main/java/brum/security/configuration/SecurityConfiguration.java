package brum.security.configuration;

import brum.common.enums.security.SecurityDocumentation;
import brum.persistence.ParameterPersistenceGateway;
import brum.security.filter.JWTAuthorizationFilter;
import brum.security.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private final UserDetailsServiceImpl userDetailsService;
    private final ParameterPersistenceGateway parameterPersistenceGateway;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, ParameterPersistenceGateway parameterPersistenceGateway) {
        this.userDetailsService = userDetailsService;
        this.parameterPersistenceGateway = parameterPersistenceGateway;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable().authorizeRequests()
                .antMatchers(SWAGGER_AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .and()
                .addFilterBefore(new JWTAuthorizationFilter(authenticationManager(), userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().sameOrigin();

        for (SecurityDocumentation.Entry entry : SecurityDocumentation.getDocumentation()) {
            String endpoint = entry.getEndpoint().replaceAll("\\{.*?}+", "*");
            endpoint = "/*" + endpoint;
            if (entry.getRoles().length == 0) {
                http.authorizeRequests().antMatchers(HttpMethod.valueOf(entry.getHttpMethod().name()), endpoint).permitAll();
            } else {
                http.authorizeRequests().antMatchers(HttpMethod.valueOf(entry.getHttpMethod().name()), endpoint).hasAuthority(entry.getPrivilege().name());
            }
        }
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }

    @Bean
    public CustomDaoAuthenticationProvider getAuthenticationProvider() {
        CustomDaoAuthenticationProvider authProvider = new CustomDaoAuthenticationProvider(userDetailsService, parameterPersistenceGateway);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    CorsConfiguration corsConfiguration() {
        CorsConfiguration cors = new CorsConfiguration().applyPermitDefaultValues();
        cors.setAllowedMethods(List.of("*"));
        cors.setExposedHeaders(List.of("Authorization","Access-Control-Allow-Origin"));
        return cors;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration());
        return source;
    }

}
