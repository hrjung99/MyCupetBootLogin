package cupet.com.demo.dto;

import java.util.List;

import cupet.com.demo.vo.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

	private String id;


	private String nickname;

	private String name;


	private String token;

	public SignResponse(User member) {
		this.id = member.getCupet_user_id();
		this.nickname = member.getCupet_user_nickname();
		this.name = member.getCupet_user_name();
	}
}