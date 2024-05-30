package cupet.com.demo.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User implements UserDetails {

	private String cupet_user_id;
	private String cupet_userpwd;
	private String cupet_user_name;
	private String cupet_user_nickname;
	private String cupet_user_address;
	private String cupet_user_gender;
	private String cupet_user_phonenumber;
	private String cupet_user_age;
	private int cupet_user_point;
	private String cupet_user_principle;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Collection<GrantedAuthority> collections = new ArrayList<GrantedAuthority>();
		Arrays.stream(cupet_user_principle.split(","))
				.forEach(role -> collections.add(new SimpleGrantedAuthority("ROLE_" + role.trim())));

		System.out.println("this : " + collections);
		return collections;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return cupet_userpwd;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return cupet_user_name;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
