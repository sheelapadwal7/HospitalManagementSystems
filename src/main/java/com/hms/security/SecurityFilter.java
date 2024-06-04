/*
 * package com.hms.security;
 * 
 * import java.io.IOException;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.security.web.authentication.
 * WebAuthenticationDetailsSource; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.filter.OncePerRequestFilter;
 * 
 * import jakarta.servlet.FilterChain; import jakarta.servlet.ServletException;
 * import jakarta.servlet.http.HttpServletRequest; import
 * jakarta.servlet.http.HttpServletResponse;
 * 
 * @Component public class SecurityFilter extends OncePerRequestFilter {
 * 
 * @Autowired private JwtService jwtService;
 * 
 * @Override protected void doFilterInternal(HttpServletRequest request,
 * HttpServletResponse response, FilterChain filterChain) throws
 * ServletException, IOException {
 * 
 * System.out.println("i am in security filter"); String token =
 * request.getHeader("Authorization"); if (token == null || token.isEmpty()) {
 * sendUnauthorizedResponse(response, "Missing token"); return; }
 * 
 * token = token.replace("Bearer ", ""); if (!jwtService.isValidToken(token)) {
 * sendUnauthorizedResponse(response, "Invalid or expired token"); return; }
 * 
 * String userName = jwtService.verifyToken(token); if (userName == null) {
 * sendUnauthorizedResponse(response, "Invalid or expired token"); return; }
 * 
 * UsernamePasswordAuthenticationToken authToken = new
 * UsernamePasswordAuthenticationToken(userName, null, null);
 * 
 * authToken.setDetails(new
 * WebAuthenticationDetailsSource().buildDetails(request));
 * SecurityContextHolder.getContext().setAuthentication(authToken);
 * 
 * // in controller by this //
 * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 * 
 * // Token is valid, continue with the filter chain
 * filterChain.doFilter(request, response);
 * 
 * }
 * 
 * private void sendUnauthorizedResponse(HttpServletResponse response, String
 * message) throws IOException {
 * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 * response.getWriter().write(message); }
 * 
 * }
 */