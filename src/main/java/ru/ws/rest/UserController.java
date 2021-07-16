package ru.ws.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import ru.ws.manager.UserManager;
import ru.ws.model.User;
import ru.ws.utils.Constants;
import ru.ws.utils.RestURIConstants;
import ru.ws.utils.Utility;

@RestController
@EnableEurekaClient
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManager userManager;

    @RequestMapping(value = RestURIConstants.POST_USER, method = RequestMethod.POST)
    public ResponseEntity<String> user(@RequestBody User userBean) {
        logger.debug("Start - user");
        
        try {
            Utility.checkParameters(new Object[]{userBean.getUsername(), userBean.getPassword()});

            Utility.checkSizeMin(userBean.getUsername(), 4);
            Utility.checkSizeMax(userBean.getUsername(), 50);
            Utility.checkSizeMin(userBean.getPassword(), 4);
            Utility.checkSizeMax(userBean.getPassword(), 20);

            logger.debug("user - user {} ", userBean.toString());                         
            userManager.user(userBean); 
            
        } catch (Exception e) {
            logger.error("The error is: ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("End - user");
        }
        return new ResponseEntity<>(Constants.RESULT_OK, HttpStatus.OK);
    }

    @RequestMapping(value = RestURIConstants.POST_LOGIN, method = RequestMethod.POST)
    public ResponseEntity<User> login(@RequestBody User userBean) {

        logger.debug("Start - login");
        User user;

        try {
            Utility.checkParameters(new Object[]{userBean.getUsername(), userBean.getPassword()});
           
            logger.debug("login - username: {}, password: ******** ", userBean.getUsername());
            user = userManager.login(userBean.getUsername(), userBean.getPassword());
            if (user != null) {
                user.setPassword("********");
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            logger.error("User not found!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("The error is: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("End - login");
        }
    }
    
    @RequestMapping(value = RestURIConstants.GET_USER_BY_ID, method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("id") Integer id) {

        logger.debug("Start - getUser");
        User user;

        try {
            Utility.checkParameters(new Object[]{id});
            Utility.checkInteger(id);
            logger.debug("getUser - id {} ", id);
            user = userManager.getUser(id);
            if (user != null) {
                user.setPassword("********");
            }
        } catch (Exception e) {
            logger.error("The error is: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("End - getUser");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @RequestMapping(value = RestURIConstants.DELETE_USER_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable("id") Integer id) {
        logger.debug("Start - deleteUser");

        try {
            Utility.checkParameters(new Object[]{id});
            Utility.checkInteger(id);
            logger.debug("deleteUser - id {} ", id);
            userManager.deleteUser(id);
        } catch (Exception e) {
            logger.error("The error is: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("End - deleteUser");
        }
        return new ResponseEntity<>(Constants.RESULT_OK, HttpStatus.OK);
    }

    
    @RequestMapping(value = RestURIConstants.GET_USER_LIST, method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "userListDefault")
    public ResponseEntity<List<User>> userList() {
        logger.debug("Start - userList");
        List<User> list;

        try {
            logger.debug("userList");
            list = userManager.userList();
        } catch (Exception e) {
            logger.error("The error is: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            logger.debug("End - userList");
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    
    public ResponseEntity<List<User>> userListDefault() {
        logger.debug("Start - userListDefault");
        List<User> list = new ArrayList<User>();     
        logger.debug("End - userListDefault");
        return new ResponseEntity<>(list, HttpStatus.OK);
    } 

}
