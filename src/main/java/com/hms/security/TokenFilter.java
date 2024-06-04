/*
 * package com.hms.security;
 * 
 * import java.io.IOException;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Component;
 * 
 * import com.hms.service.TokenLogService;
 * 
 * import jakarta.servlet.Filter; import jakarta.servlet.FilterChain; import
 * jakarta.servlet.FilterConfig; import jakarta.servlet.ServletException; import
 * jakarta.servlet.ServletRequest; import jakarta.servlet.ServletResponse;
 * import jakarta.servlet.http.HttpServletRequest; import
 * jakarta.servlet.http.HttpServletResponse;
 * 
 * public class TokenFilter implements Filter {
 * 
 * @Autowired private TokenLogService tokenLogService;
 * 
 * @Autowired private JwtService jwtService;
 * 
 * @Override public void init(FilterConfig filterConfig) throws ServletException
 * {
 * 
 * }
 * 
 * @Override public void doFilter(ServletRequest servletRequest, ServletResponse
 * servletResponse, FilterChain filterChain) throws IOException,
 * ServletException { HttpServletRequest request = (HttpServletRequest)
 * servletRequest; HttpServletResponse response = (HttpServletResponse)
 * servletResponse;
 * 
 * System.out.println("i am in token filter"); String token =
 * request.getHeader("Authorization"); if (token == null || token.isEmpty()) {
 * sendUnauthorizedResponse(response, "Missing token"); return; }
 * 
 * token = token.replace("Bearer ", ""); if
 * (!tokenLogService.isValidToken(token)) {
 * 
 * sendUnauthorizedResponse(response, "Invalid or expired token"); return; }
 * 
 * String userName = jwtService.verifyToken(token); if (userName == null) {
 * sendUnauthorizedResponse(response, "Invalid or expired token"); return; }
 * 
 * request.setAttribute("tokenLog", (Object) userName);
 * 
 * // Token is valid, continue with the filter chain
 * filterChain.doFilter(request, response); }
 * 
 * private void sendUnauthorizedResponse(HttpServletResponse response, String
 * message) throws IOException {
 * response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 * response.getWriter().write(message); }
 * 
 * @Override public void destroy() {
 * 
 * } }
 */