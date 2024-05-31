package cupet.com.demo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAdressVO {
	String cupet_user_id;
	String roadAddress;
	String jibunAddress;
	String detailAddress;
	String locateX;
	String locateY;
}
