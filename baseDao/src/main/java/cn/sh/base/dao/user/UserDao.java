package cn.sh.base.dao.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.sh.base.dao.user.domain.User;

@Mapper
public interface UserDao {

	 User getByLoginNameAndPass(@Param("loginName")String username, @Param("pass")String password);
}
