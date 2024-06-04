//package com.hms.security;
//
//
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Repository;
//import org.springframework.stereotype.Service;
//
//import com.hms.model.Admin;
//import com.hms.repository.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//
//
//
//
//@Service
//
//public class UserServiceImpl implements UserService {
//	
//	@Autowired
//    UserRepository userRepository;
//    @Override
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsService() {
//
//        	 public UserDetails loadUserByUsername(String email) {
//                 return userRepository.findByEmail(email);
//                        
//             }
//            
//        };
//}
//	@Override
//	public UserDetails loadUserByUsername(String userEmail) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}