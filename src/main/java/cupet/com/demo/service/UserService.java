package cupet.com.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cupet.com.demo.mapper.UserMapper;
import cupet.com.demo.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService{

	private final UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Date date = new Date();
		System.out.println("User 로그인 접근 확인 -" + date);
		User res = userMapper.login(User.builder().cupet_user_id(username).build());
		if(res == null) {
			throw new UsernameNotFoundException(username + " 사용자가 존재하지 않습니다");
		}

		return res;
	}
	
	public List<User> getLoginId(String id) {
		if(id == null) {
			return null;
		}
		List<User> res = userMapper.getUserId(id);
		if(res == null) {
			return null;
		}
		return res;
		
	}

}
