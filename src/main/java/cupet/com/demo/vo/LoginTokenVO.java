package cupet.com.demo.vo;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginTokenVO {
	
	String logintoken_idx;
	String cupet_user_id;
	Date Expiration_date;
	
	public boolean aftercheckedExpiration_date(Date now) {
		if(Expiration_date.after(now)) {
			return true;
		}
		return false;
	}

}
