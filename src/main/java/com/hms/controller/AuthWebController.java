package com.hms.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hms.dto.ChangePasswordDTO;
import com.hms.model.Receptionist;
import com.hms.model.TokenLog;
import com.hms.repository.ReceptionistRepository;
import com.hms.repository.TokenLogRepository;
import com.hms.service.TokenLogService;


@RequestMapping("/auth/web")
@Controller
public class AuthWebController {

    @Autowired
     TokenLogService tokenLogService;
    @Autowired
	TokenLogRepository tokenLogRepository;
    
    @Autowired
    ReceptionistRepository receptionistRepository;

    @GetMapping("/change-password/{token}")
    public String changePassword(@PathVariable String token, Model model) {
        boolean isValidToken = tokenLogService.isValidToken(token);
        model.addAttribute("isError", !isValidToken);
        model.addAttribute("status", false);
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        return "resetpassword";
    }


    @PostMapping("/change-password/{token}")
    public String changePasswordSave(@PathVariable String token, @ModelAttribute("changePasswordDTO") ChangePasswordDTO change,
                                     Model model) {
        boolean isValidToken = tokenLogService.isValidToken(token);

    	model.addAttribute("isError", false);
        if(!isValidToken) {
        	model.addAttribute("isError", !isValidToken);

            return "resetpassword";
        }
        
        
        boolean isChangesValid = 
        		change != null && 
        		change.getNewPassword() != null && 
        		change.getConfirmPassword() != null && 
        		change.getNewPassword().equals(change.getConfirmPassword());

        if (!isChangesValid) {
        	model.addAttribute("status", false);
        	model.addAttribute("message", "password is not valid or matching with confirm password");
            return "resetpassword";
        }

        String username = getUsernameFromToken(token);
        boolean isPasswordUpdated = updatePassword(username, change.getNewPassword());
        
        if(!isPasswordUpdated) {
        	model.addAttribute("status", false);
        	model.addAttribute("message", "password is not updating");
            return "resetpassword";
        }
        
        // invalidate the token

    	model.addAttribute("status", true);
    	model.addAttribute("message", "successfully changed");
        return "resetpassword";
    }

    private String getUsernameFromToken(String token) {
        Optional<TokenLog> tokenLogOptional = tokenLogRepository.findByToken(token);

        if (tokenLogOptional.isPresent()) {
            TokenLog tokenLog = tokenLogOptional.get();
            return tokenLog.getUserName(); // Assuming the username is stored in the TokenLog entity
        }
        return null;
    }

    public boolean updatePassword(String name, String newPassword) {
        Optional<Receptionist> userOptional = receptionistRepository.findByName(name);
        
        if (userOptional.isPresent()) {
        	Receptionist receptionist = userOptional.get();
        	receptionist.setPassword(newPassword); // Update the password
        	receptionistRepository.save(receptionist); // Save the updated user entity
            return true; // Password updated successfully
        } else {
            return false; // User not found
        }
    }
}
