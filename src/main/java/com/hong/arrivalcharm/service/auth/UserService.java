package com.hong.arrivalcharm.service.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hong.arrivalcharm.lib.FileHandler;
import com.hong.arrivalcharm.lib.ImageChecker;
import com.hong.arrivalcharm.model.auth.User;
import com.hong.arrivalcharm.model.destination.Destination;
import com.hong.arrivalcharm.repository.UserRepository;
import com.hong.arrivalcharm.repository.destination.DestinationRepository;
import com.hong.arrivalcharm.service.ServiceAbstract;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService extends ServiceAbstract {
	private final UserRepository userRepository;
	private final DestinationRepository destinationRepository;
	
	@Value("${file.dir}")
    private String fileDir;
	// 유저 수정
	@Transactional
    public Map<String, String> updateUser(int userId, String displayUsername, MultipartFile file) throws Exception{
		User user = this.getUserSession();
		String savePath = user.getProfilePath();
		displayUsername = displayUsername.trim();
		
		if(userId != user.getId()) {
			throw new Exception("본인의 계정만 수정 가능합니다.");
		}
		// 파일 저장
		if(file != null) {
			FileHandler fh = new FileHandler(fileDir);
			
			// 기존 사진 파일 삭제
			fh.deleteImage(user.getProfilePath());
			
			// 이미지 확장자 확인
			ImageChecker ci = new ImageChecker(file);
			ci.checkImage(file);
			
			// 새 사진 파일 생성
			savePath = fh.saveImage(file);
			
			user.setProfilePath(savePath);
		}
		if(displayUsername != null && !displayUsername.isEmpty()) {
			user.setDisplayUsername(displayUsername);
		}
		userRepository.save(user);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
		return result;
    }
	
	
	// 유저 삭제(회원 탈퇴) - 파일 데이터 삭제, 물리 파일 삭제, 유저 삭제
	@Transactional
    public Map<String, String> resignUser(int userId) throws Exception{
		User user = this.getUserSession();
		if(user.getId() != userId) {
			throw new Exception("본인의 계정만 탈퇴 가능합니다.");
		}
//		Route 데이터 삭제
		List<Destination> myRoutes = destinationRepository.myDestinations(userId);
		for(Destination r : myRoutes) {
			destinationRepository.delete(r);
		}
		
		// 사진 물리 파일 삭제
		FileHandler fh = new FileHandler(fileDir);
		fh.deleteImage(user.getProfilePath());
		// User 삭제
		userRepository.delete(user);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
		return result;
    }
	
}
