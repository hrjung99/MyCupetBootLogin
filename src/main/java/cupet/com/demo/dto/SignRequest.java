package cupet.com.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Builder
public class SignRequest {

    private String id;

    private String account;

    private String password;

    private String nickname;

    private String name;

    private String email;

}