package cupet.com.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import cupet.com.demo.vo.User;

@Mapper
@Repository("userMapper")
public interface UserMapper {
	
	@Select("select * from cupetuser where cupet_user_id = #{cupet_user_id}")
	User login(User user);

	
	@Insert("INSERT INTO cupetuser (" +
            "cupet_user_id, " +
            "cupet_userpwd, " +
            "cupet_user_name, " +
            "cupet_user_nickname, " +
            "cupet_user_address, " +
            "cupet_user_gender, " +
            "cupet_user_phonenumber, " +
            "cupet_user_age, " +
            "cupet_user_point, " +
            "cupet_user_principle" +
            ") VALUES (" +
            "#{cupet_user_id}, " +
            "#{cupet_userpwd}, " +
            "#{cupet_user_name}, " +
            "#{cupet_user_nickname}, " +
            "#{cupet_user_address}, " +
            "#{cupet_user_gender}, " +
            "#{cupet_user_phonenumber}, " +
            "#{cupet_user_age}, " +
            "#{cupet_user_point}, " +
            "#{cupet_user_principle}" +
            ")")
    void save(User member);
}
