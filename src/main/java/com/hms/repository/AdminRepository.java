package com.hms.repository;


import org.springframework.stereotype.Repository;

import com.hms.model.Admin;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Optional<Admin> findByUserName(String userName);
	List<Admin> findByAccountStatus(String accountStatus);
	
	Optional<Admin> findByEmail(String email);
	
    
}
