package ru.ws.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ws.model.User;
import ru.ws.repository.UserRepository;
import ru.ws.utils.security.AESChiper;

@Service
@Transactional
public class UserManager {

	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserManager.class);    

	public void user(User user) throws Exception {
		logger.debug("Start - user");

		try {
			logger.debug("user - crypt password");
			user.setPassword(AESChiper.encrypt(user.getPassword()));
			logger.debug("user - crypt password is ok!");
			userRepository.save(user);
		} catch (Exception e) {
			logger.error("The error is: ", e);
			throw e;
		} finally {
			logger.debug("End - user");
		}
	}

	public User login(String username, String password) throws Exception {
		logger.debug("Start - login - username: {}, password: ********", username);
		User user;

		try {
			user = userRepository.findUserByUsername(username);
			if(user != null) {
				String pwd = AESChiper.decrypt(user.getPassword());
				if(!pwd.equals(password))
					user = null;
			}
		} catch (Exception e) {
			logger.error("The error is: ", e);
			throw e;
		} finally {
			logger.debug("End - login");
		}

		return user;
	}


	public User getUser(Integer id) {
		logger.debug("Start - getUser - id: {}", id);
		User user;

		try {
			user = userRepository.findUserById(id);
		} catch (Exception e) {
			logger.error("The error is: ", e);
			throw e;
		} finally {
			logger.debug("End - getUser");
		}

		return user;
	}
	
	public void deleteUser(Integer id) {
		logger.debug("Start - deleteUser - id: {}", id);

		try {
			userRepository.deleteUserById(id);
		} catch (Exception e) {
			logger.error("The error is: ", e);
			throw e;
		} finally {
			logger.debug("End - deleteUser");
		}
	}

	public List<User> userList() {
		logger.debug("Start - userList");
		List<User> list;
		try {
			list = userRepository.findAll();       
		} catch (Exception e) {
			logger.error("The error is: ", e);
			throw e;
		} finally {
			logger.debug("End - userList");
		}
		return list;
	}

}
