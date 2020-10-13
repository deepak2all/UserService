package au.com.deepak.user.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import au.com.deepak.user.security.model.Role;

//@Profile(Profiles.BASIC_AUTH)
@Configuration
@EnableWebSecurity
public class BasicAuthSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Autowired
    private UserDetailsService userDetailsService;
 
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        /*
         PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
          .withUser("spring")
          .password(encoder.encode("secret"))
          .roles(Role.USER);
         */
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF
        http.csrf().disable()
                // Only admin can perform HTTP PUT operation
                .authorizeRequests().antMatchers(HttpMethod.PUT).hasRole(Role.ADMIN)
                // any authenticated user can perform all other operations
                .antMatchers("/api/v1/users/**").hasAnyRole(Role.ADMIN, Role.USER).and().httpBasic()
                // Permit all other request without authentication
                .and().authorizeRequests()
                // Permit all other request without authentication to h2 db
                .antMatchers("/h2/**").permitAll()
                // Permit all other request without authentication to home page
                .antMatchers("/api/v1").permitAll()
                // Permit all other request 
                .anyRequest().permitAll()
                // We don't need sessions to be created.
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
 
    @Override
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder(10);
    	return new BCryptPasswordEncoder();
    }
}
