package com.hong.arrivalcharm.controller.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hong.arrivalcharm.service.auth.UserService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
//	@Autowired
//	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	
	@PatchMapping("/{id}")
	@ApiOperation(value = "회원 수정", notes = "")
	public @ResponseBody Map<String, String> updateUser(@PathVariable int id, @RequestParam(value = "displayUsername", required = false) String displayUsername, @Nullable @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		Map<String, String> result = null;
		try {
			result = userService.updateUser(id, displayUsername, file);
		} catch (Exception e) {
			throw e;
		}
		
		return result;
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "회원 탈퇴", notes = "")
	public @ResponseBody Map<String, String> resignUser(@PathVariable int id) throws Exception{
		Map<String, String> result = null;
		try {
			result = userService.resignUser(id);
		} catch (Exception e) {
			throw e;
		}
		
		return result;
	}
	
}
