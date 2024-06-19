package com.hms.dto;

public class ChangePasswordDTO {
	
    private String newPassword;
    private String confirmPassword;

  
    public ChangePasswordDTO() {
    }


	public String getNewPassword() {
		return newPassword;
	}


	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	public String getConfirmPassword() {
		return confirmPassword;
	}


	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

    
    
}

