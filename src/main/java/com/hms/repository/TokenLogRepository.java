package com.hms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.enums.LinkType;
import com.hms.model.TokenLog;


@Repository
public interface TokenLogRepository extends JpaRepository<TokenLog , Integer> {

	Optional<TokenLog> findByToken(String token);
	
	Optional<TokenLog> findByTokenAndLinkType(String token, LinkType linkType);


}
