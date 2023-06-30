package com.example.resourceservice.filters

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


@Configuration
class SecurityConfig(
    private val loginFilter: LoginFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
                it.requestMatchers("/open").permitAll()
            }.authorizeHttpRequests {
                it.anyRequest().authenticated()
            }.addFilterBefore(loginFilter, BasicAuthenticationFilter::class.java)

        return http.build()
    }
}