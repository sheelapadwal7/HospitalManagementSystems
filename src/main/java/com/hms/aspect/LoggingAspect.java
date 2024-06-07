package com.hms.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hms.controller.AuthController;
import com.hms.dto.LoginRequestDTO;
import com.hms.dto.LoginResponseDTO;
import com.hms.security.JwtService;

@Aspect
@Component
public class LoggingAspect {

	private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Autowired
	AuthController authController;

	@Autowired
	JwtService jwtService;

	@AfterReturning(pointcut = "execution(* com.hms.controller.AuthController.adminLogin(..))", returning = "result")
	public void logSuccessfulLogin(JoinPoint joinPoint, LoginResponseDTO result) {
		LoginRequestDTO loginRequestDto = (LoginRequestDTO) joinPoint.getArgs()[0];
		String username = loginRequestDto.getUserName();
		if (result != null) {
			logger.info("User {} logged in successfully", username);
		} else {
			logger.warn("Login attempt failed for user {}", username);
		}
	}

	@AfterReturning(pointcut = "execution(* com.hms.controller.AuthController.logout(..)) && args(token)", returning = "result")
	public void logSuccessfulLogout(JoinPoint joinPoint, String token, Object result) {
		// Extract username from the token, providing the UserName parameter
		String userName = jwtService.extractUsernameFromToken(token);

		// Log the logout event
		if (userName != null) {
			logger.info("User {} logged out successfully", userName);
		} else {
			logger.warn("Failed to extract username from token during logout");
		}
	}

}
