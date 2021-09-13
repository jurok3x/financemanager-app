package com.financemanager.demo.site.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.financemanager.demo.site.config.jwt.JwtFilter;
import com.financemanager.demo.site.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	public CustomUserDetailsService userDetailsService;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                	.authorizeRequests()
                	.antMatchers("/api/auth", "/users/fail").not().fullyAuthenticated()
                	.antMatchers("/users/admin/get").hasRole("ADMIN")
                	.antMatchers("/users/user/get").hasAnyRole("ADMIN","USER")
                	.antMatchers("/").permitAll()
                	.antMatchers("/api/items/all").permitAll()
                	.anyRequest().authenticated()
                .and()
                	.formLogin()
                //	.loginPage("/registration.html")
                	.failureUrl("/users/fail")
                	.defaultSuccessUrl("/")
                	.permitAll()
                .and()
                	.logout()
                	.permitAll()
                	.logoutSuccessUrl("/");
        
    }
	
	 @Autowired
	 private JwtFilter jwtFilter;
}
