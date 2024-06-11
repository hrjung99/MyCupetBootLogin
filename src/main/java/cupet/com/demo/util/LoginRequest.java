package cupet.com.demo.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
	private String cupet_user_id;
	private String cupet_userpwd;
}
