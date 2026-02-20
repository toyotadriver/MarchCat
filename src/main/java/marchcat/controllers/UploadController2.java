package marchcat.controllers;

import marchcat.pictures.UploadService;
import marchcat.security.TokenManager;

public class UploadController2 {
	TokenManager tokenManager;
	UploadService uploadService;
	
	UploadController2(TokenManager tokenManager, UploadService uploadService){
		this.tokenManager = tokenManager;
		this.uploadService = uploadService;
	}
	
	

}
