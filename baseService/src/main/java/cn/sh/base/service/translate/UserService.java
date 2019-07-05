package cn.sh.base.service.translate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sh.base.dao.user.UserDao;
import cn.sh.base.dao.user.domain.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	public User getUser(String name, String password){
		User user = userDao.getByLoginNameAndPass(name, password);
		if(user==null){
			return null;
		}
		userDao.toString();
		return user;
	}
}
