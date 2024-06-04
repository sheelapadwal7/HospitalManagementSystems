package com.hms.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.enums.LinkType;
import com.hms.model.Admin;
import com.hms.model.Receptionist;
import com.hms.model.TokenLog;
import com.hms.repository.TokenLogRepository;

@Service
public class TokenLogService {

	@Autowired
	TokenLogRepository tokenLogRepository;

	// Generate token
	public String generateToken(Admin admin) {
		String token = UUID.randomUUID().toString();

		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);

		TokenLog tokenLog = new TokenLog();
		tokenLog.setToken(token);
		tokenLog.setExpiryTime(expiryTime);
		tokenLog.setValid(true);
		tokenLog.setLinkType(LinkType.ADMIN);
		tokenLog.setLinkId(admin.getId());

		tokenLogRepository.save(tokenLog);

		return token;
	}

	// Valid token
	public boolean isValidToken(String token) {
		Optional<TokenLog> tokenLogOptional = tokenLogRepository.findByToken(token);

		if (tokenLogOptional.isPresent()) {
			TokenLog tokenLog = tokenLogOptional.get();
			if (tokenLog.isValid()) {
				LocalDateTime expiryTime = tokenLog.getExpiryTime();

				return !isTokenExpired(expiryTime);
			}
		}
		return false;
	}

	// Valid token
	public TokenLog verifyToken2(String token) {
		Optional<TokenLog> tokenLogOptional = tokenLogRepository.findByToken(token);

		if (tokenLogOptional.isPresent()) {
			TokenLog tokenLog = tokenLogOptional.get();
			if (tokenLog.isValid()) {
				LocalDateTime expiryTime = tokenLog.getExpiryTime();

				if (!isTokenExpired(expiryTime)) {
					return tokenLog;
				}
			}
		}
		return null;
	}

	private boolean isTokenExpired(LocalDateTime expiryTime) {
		return expiryTime != null && expiryTime.isBefore(LocalDateTime.now());
	}

	// invalidate token
	public boolean invalidateToken(String token) {
		Optional<TokenLog> tokenLogOptional = tokenLogRepository.findByToken(token);

		if (tokenLogOptional.isPresent()) {
			TokenLog log = tokenLogOptional.get();
			log.setValid(false);
			log.setExpiryTime(LocalDateTime.now().plusMinutes(1));
			log.setLogoutTime(LocalDateTime.now());
			tokenLogRepository.save(log);
			return true;
		}
		return false;
	}
	
	 public boolean inValidateToken(String token) {
	        Optional<TokenLog> tokenLogOptional = tokenLogRepository.findByToken(token);
	        if (tokenLogOptional.isPresent()) {
	            TokenLog tokenLog = tokenLogOptional.get();
	            tokenLog.setValid(false);
	            tokenLog.setExpiryTime(LocalDateTime.now().plusMinutes(1)); // Set the expiry time to the current time
	            tokenLogRepository.save(tokenLog);
	            return true;
	        }
	        return false; 
	    }

	public List<TokenLog> getTokenLog() {

		return (List<TokenLog>) tokenLogRepository.findAll();

	}

	public TokenLog getTokenLog(String token) {

		token = token.replace("Bearer ", "");
		TokenLog tl = tokenLogRepository.findByToken(token).orElse(null);

		if (tl == null) {
			throw new Error("No login found");
		}

		return tl;
	}

	public boolean isStudent(TokenLog tokenLog) {
		return tokenLog.getLinkType().equals(LinkType.ADMIN);
	}

	public boolean isAdmin(TokenLog tokenLog) {
		return tokenLog.getLinkType().equals(LinkType.ADMIN);
	}

	public boolean isAdminOrProffesor(TokenLog tokenLog) {
		return (tokenLog.getLinkType().equals(LinkType.ADMIN) || tokenLog.getLinkType().equals(LinkType.DOCTOR));
	}

	public Optional<TokenLog> getTokenLogById(Integer id) {
		return tokenLogRepository.findById(id);

	}

	public List<String> validate(TokenLog tokenLog) {

		List<String> error = new ArrayList<>();

		if (tokenLog.getUserName() == null) {
			error.add("TokenLog Username can not be empty");
		}

		if (tokenLog.getToken() == null) {
			error.add("Token can not be empty");
		}
		if (tokenLog.getIp() == null) {
			error.add("IP can not be empty");
		}

		if (tokenLog.getLinkId() == 0) {
			error.add("LinkId can not be empty");
		}

		if (tokenLog.getAttempt() == 0) {
			error.add("Attempt can not be empty");
		}

		return error;
	}

	public TokenLog addLogForStudentLogin(String token, int studentId, String email, LocalDateTime expiryTime) {
		TokenLog tl = new TokenLog();

		tl.setToken(token);
		tl.setValid(true);

		tl.setUserName(email);
		tl.setExpiryTime(expiryTime); // Set expiry time
		return tokenLogRepository.save(tl);
	}

	public TokenLog updateTokenLog(Integer id, TokenLog tokenLog) {
		TokenLog existingStaff = tokenLogRepository.findById(id).orElse(null);
		existingStaff.setUserName(tokenLog.getUserName());
		existingStaff.setToken(tokenLog.getToken());
		existingStaff.setIp(tokenLog.getIp());
		existingStaff.setLinkId(tokenLog.getLinkId());
		existingStaff.setAttempt(tokenLog.getAttempt());
		return tokenLogRepository.save(existingStaff);
	}

	public boolean deleteTokenLog(Integer id) {

		boolean exists = tokenLogRepository.existsById(id);
		if (exists) {
			tokenLogRepository.deleteById(id);
			return true;
		} else {

			return false;
		}

	}

	public boolean verifyToken(String token) {
		Optional<TokenLog> tokenLogO = tokenLogRepository.findByToken(token);
		if (!tokenLogO.isPresent()) {
			return false;
		}

		return tokenLogO.get().isValid();
	}
	
	public String generateToken(Receptionist receptionist) {
		String token = UUID.randomUUID().toString();

		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);

		TokenLog tokenLog = new TokenLog();
		tokenLog.setToken(token);
		tokenLog.setExpiryTime(expiryTime);
		tokenLog.setValid(true);
		tokenLog.setLinkType(LinkType.ADMIN);
		tokenLog.setLinkId(receptionist.getId());

		tokenLogRepository.save(tokenLog);

		return token;
	}

}
