/*
 * package com.hms.security;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.web.servlet.FilterRegistrationBean; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.method.configuration.
 * EnableMethodSecurity; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.config.http.SessionCreationPolicy; import
 * org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter; import
 * org.springframework.web.filter.OncePerRequestFilter;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity
 * 
 * @EnableMethodSecurity public class WebMvcConfig {
 * 
 * @Autowired SecurityFilter securityFilter;
 * 
 * @Bean public FilterRegistrationBean<SecurityFilter>
 * tokenFilterRegistrationBean(SecurityFilter securityFilter) {
 * FilterRegistrationBean<SecurityFilter> registrationBean = new
 * FilterRegistrationBean<>(); registrationBean.setFilter(securityFilter);
 * registrationBean.addUrlPatterns("/homePage/*");
 * registrationBean.addUrlPatterns("/Admin/*");
 * 
 * registrationBean.addUrlPatterns("/tasks/*"); registrationBean.setOrder(1);
 * return registrationBean; }
 * 
 * @Bean public FilterRegistrationBean<SecurityFilter>
 * authFilterRegistrationBean(SecurityFilter securityFilter) {
 * FilterRegistrationBean<SecurityFilter> registrationBean = new
 * FilterRegistrationBean<>(); registrationBean.setFilter(securityFilter);
 * registrationBean.addUrlPatterns("/auth/**"); registrationBean.setOrder(2);
 * registrationBean.setEnabled(false); return registrationBean; }
 * 
 * protected void configure(HttpSecurity http) throws Exception {
 * http.csrf().disable() .authorizeRequests()
 * .requestMatchers("/auth/**").permitAll() // Permit access to /auth endpoints
 * .requestMatchers("/test").authenticated()
 * .requestMatchers("/auth/admin/login").permitAll()
 * .anyRequest().authenticated() .and() .sessionManagement()
 * .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
 * 
 * http.addFilterBefore(securityFilter,
 * UsernamePasswordAuthenticationFilter.class); } }
 */