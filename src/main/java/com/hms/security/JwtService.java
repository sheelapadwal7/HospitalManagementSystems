
package com.hms.security;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.hms.enums.LinkType;
import com.hms.model.Admin;
import com.hms.model.Doctor;
import com.hms.model.Receptionist;
import com.hms.model.TokenLog;
import com.hms.repository.AdminRepository;
import com.hms.repository.TokenLogRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Autowired
	TokenLogRepository tokenLogRepository;
	
	@Autowired
	AdminRepository adminRepository;

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;

	// Constructor injection of TokenLogRepository
	public JwtService(TokenLogRepository tokenLogRepository) {
		this.tokenLogRepository = tokenLogRepository;
	}

	// Other existing fields and methods

	public String generateToken(Admin admin, String UserName) {
		// Create extra claims for the token
		Map<String, Object> extraClaims = Map.of("userType", LinkType.ADMIN, "userId", admin.getId());

		// Generate the token
		String token = generateToken(admin.getUsername(), extraClaims);

		// Save token log to the database
		saveTokenLog(admin, token, UserName);

		return token;
	}

	private String generateToken(String userName, Map<String, Object> extraClaims) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	private void saveTokenLog(Admin admin, String token, String userName) {

		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(1);
		TokenLog tokenLog = new TokenLog();
		tokenLog.setToken(token);
		tokenLog.setUserName(userName);
		tokenLog.setExpiryTime(expiryTime);

		// Set other fields if needed, such as creation time, validity, etc.
		tokenLogRepository.save(tokenLog);
	}

	public String verifyToken(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	/*
	 * boolean isTokenValid(String token, UserDetails userDetails) { return false; }
	 */
	// Valid token
	/*
	 * public boolean isValidToken(String token) { Optional<TokenLog>
	 * tokenLogOptional = tokenLogRepository.findByToken(token);
	 * 
	 * if (tokenLogOptional.isPresent()) { TokenLog tokenLog =
	 * tokenLogOptional.get(); if (tokenLog.isValid()) { LocalDateTime expiryTime =
	 * tokenLog.getExpiryTime();
	 * 
	 * return !isTokenExpired(expiryTime); } } return false; }
	 */

	// Valid token
	/*
	 * public TokenLog verifyToken2(String token) { Optional<TokenLog>
	 * tokenLogOptional = tokenLogRepository.findByToken(token);
	 * 
	 * if (tokenLogOptional.isPresent()) { TokenLog tokenLog =
	 * tokenLogOptional.get(); if (tokenLog.isValid()) { LocalDateTime expiryTime =
	 * tokenLog.getExpiryTime();
	 * 
	 * if (!isTokenExpired(expiryTime)) { return tokenLog; } } } return null; }
	 * 
	 * private boolean isTokenExpired(LocalDateTime expiryTime) { return expiryTime
	 * != null && expiryTime.isBefore(LocalDateTime.now()); }
	 */
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


	public boolean inValidateToken(String token) {
		Optional<TokenLog> tokenLogOptional = tokenLogRepository.findByToken(token);
		if (tokenLogOptional.isPresent()) {
			TokenLog tokenLog = tokenLogOptional.get();
			tokenLog.setValid(false);
			tokenLog.setExpiryTime(LocalDateTime.now().plusMinutes(10)); // Set the expiry time to the current time
			tokenLogRepository.save(tokenLog);
			return true;
		}
		return false;
	}

	public String extractUsernameFromToken(String token) {
		try {
			@SuppressWarnings("deprecation")
			Claims claims = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token).getBody();
			return claims.getSubject(); // Extract username from the subject claim
		} catch (Exception e) {
			return null;
		}
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(Receptionist receptionist, String UserName) {
		// Create extra claims for the token
		Map<String, Object> extraClaims = Map.of("userType", LinkType.ADMIN, "userId", receptionist.getId());

		// Generate the token
		String token = generateToken1(receptionist.getName(), extraClaims);

		// Save token log to the database
		saveTokenLog(receptionist, token, UserName);

		return token;
	}

	private String generateToken1(String Name, Map<String, Object> extraClaims) {
		return Jwts.builder().setClaims(extraClaims).setSubject(Name).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	private void saveTokenLog(Receptionist receptionist, String token, String userName) {
		TokenLog tokenLog = new TokenLog();
		tokenLog.setToken(token);
		tokenLog.setUserName(userName);
		// Set other fields if needed, such as creation time, validity, etc.
		tokenLogRepository.save(tokenLog);
	}

	public String verifyToken1(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	public boolean inValidateToken1(String token) {
		Optional<TokenLog> tokenLogOptional = tokenLogRepository.findByToken(token);
		if (tokenLogOptional.isPresent()) {
			TokenLog tokenLog = tokenLogOptional.get();
			tokenLog.setValid(false);
			tokenLog.setExpiryTime(LocalDateTime.now().plusMinutes(10)); // Set the expiry time to the current time
			tokenLogRepository.save(tokenLog);
			return true;
		}
		return false;
	}

	//For Doctor
	
	
	
	public String generateToken2(Doctor doctor, String UserName) {
		// Create extra claims for the token
		Map<String, Object> extraClaims = Map.of("userType", LinkType.DOCTOR, "userId", doctor.getId());

		// Generate the token
		String token = generateToken2(doctor.getUserName(), extraClaims);

		// Save token log to the database
		saveTokenLog(doctor, token, UserName);

		return token;
	}

	private String generateToken2(String userName, Map<String, Object> extraClaims) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	private void saveTokenLog(Doctor dcotor, String token, String userName) {

		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(1);
		TokenLog tokenLog = new TokenLog();
		tokenLog.setToken(token);
		tokenLog.setUserName(userName);
		tokenLog.setExpiryTime(expiryTime);

		// Set other fields if needed, such as creation time, validity, etc.
		tokenLogRepository.save(tokenLog);
	}

	public String verifyToken3(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

		return claims.getSubject();
	}
	
	UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return adminRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };

	}
}
