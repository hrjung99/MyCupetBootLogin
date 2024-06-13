package cupet.com.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import cupet.com.demo.vo.LoginTokenVO;
import cupet.com.demo.vo.User;
import cupet.com.demo.vo.UserAdressVO;

@Mapper
@Repository("userMapper")
public interface UserMapper {

	@Select("select * from cupetuser where cupet_user_id = #{cupet_user_id}")
	User login(User user);

	@Insert("INSERT INTO cupetuser (" + "cupet_user_id, " + "cupet_userpwd, " + "cupet_user_name, "
			+ "cupet_user_nickname, " + "cupet_user_address, " + "cupet_user_gender, " + "cupet_user_phonenumber, "
			+ "cupet_user_birth, " + "cupet_user_point, " + "cupet_user_principle," + "cupet_user_email" + ") VALUES ("
			+ "#{cupet_user_id}, " + "#{cupet_userpwd}, " + "#{cupet_user_name}, " + "#{cupet_user_nickname}, "
			+ "#{cupet_user_address}, " + "#{cupet_user_gender}, " + "#{cupet_user_phonenumber}, "
			+ "#{cupet_user_birth}, " + "#{cupet_user_point}, " + "#{cupet_user_principle}," + "#{cupet_user_email}"
			+ ")")
	int save(User member);

	@Select("select * from cupetuser where cupet_user_id = #{id}")
	List<User> getUserId(String id);

	@Select("select * from cupetuser where cupet_user_email = #{cupet_user_email}")
	User emailvaildcheck(String email);

	@Insert("INSERT INTO cupetuseraddress (" + "cupet_user_id, " + "roadAddress, " + "jibunAddress, "
			+ "detailAddress, " + "locateX, " + "locateY" + // 여기에 쉼표 추가
			") VALUES (" + "#{cupet_user_id}, " + "#{roadAddress}, " + "#{jibunAddress}, " + "#{detailAddress}, "
			+ "#{locateX}, " + // 여기에 쉼표 추가
			"#{locateY}" + ")")
	int saveAddres(UserAdressVO userAdressVO);

	@Insert("insert into cupetlogintoken (cupet_user_id,Expiration_date ) values (#{cupet_user_id},#{Expiration_date})")
	int insertLoginToken(LoginTokenVO loginTokenVO);

	@Select("Select * from cupetlogintoken")
	List<LoginTokenVO> dbLoginTokenList();

	@Delete("DELETE FROM cupetlogintoken WHERE logintoken_idx = #{idx}")
	int deleteExpiredToken(String idx);
	
	@Select("SELECT * FROM cupetlogintoken WHERE cupet_user_id = #{id} ORDER BY Expiration_date DESC")
	List<LoginTokenVO> dbLoginTokenListofLoginUser(String id);

}
