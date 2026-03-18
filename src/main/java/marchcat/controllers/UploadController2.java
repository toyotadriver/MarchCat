package marchcat.controllers;

import marchcat.pictures.DataService;
import marchcat.security.TokenManager;

public class UploadController2 {
	TokenManager tokenManager;
	DataService uploadService;
	
	UploadController2(TokenManager tokenManager, DataService uploadService){
		this.tokenManager = tokenManager;
		this.uploadService = uploadService;
	}
	
	

}
