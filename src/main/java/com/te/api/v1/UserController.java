package com.te.api.v1;

import com.te.domain.JsView;
import com.te.domain.User;
import com.te.extend.security.JwtTokenUtil;
import com.te.extend.security.RestAuthenticationRequest;
import com.te.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value="/api/users")
public class UserController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/{Id}", method = RequestMethod.GET)
    public User findById(@PathVariable("Id") Long Id) {
        logger.debug("user path variable is:" + Id);
        User result = userService.findById(Id);
        return result;

//        try{
//            Authentication notFullyAuthentication=new IdAuthenticationToken(Id);
//            final Authentication authentication=authenticationManager.authenticate(notFullyAuthentication);
//        }catch (AuthenticationException ex){
//            logger.info("invalid Id", ex);
//        }
    }

//    @RequestMapping(value = "/signup", method = RequestMethod.POST)
//    public User createUser(@RequestBody User user){
//        User result=userService.createUser(user);
//        return result;
//    }

    @RequestMapping(value = "", method = RequestMethod.GET, params = {"username"})
    public User findByUsername(@RequestParam(value = "username") String username, Device device) {
        setJsonViewClass(JsView.User.class); //what situation or role
        disableMapperFeature_DEFAULT_VIEW_INCLUSION();
        logger.debug("parameter name is:" + username);
        User result = userService.findByUsernameIgnoreCase(username);//return userService.findBy(new Car(carId)).get();
        return result;

//        try{
//            Authentication notFullyAuthentication = new UsernameAuthenticationToken(username);
//            final Authentication authentication = authenticationManager.authenticate(notFullyAuthentication);
//        }catch (AuthenticationException ex){
//            logger.info("invalid username", ex);
//        }
    }



    @RequestMapping(value = "", method = RequestMethod.GET, params = {"first_name"})
    public User findByFirstName(@RequestParam("first_name") String firstName) {
        logger.debug("parameter name is:" + firstName);
        User result = userService.findByFirstNameIgnoreCase(firstName);
        return result;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public User signup(@RequestBody User user) {
        User result = userService.createUser(user);
        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login (@RequestBody RestAuthenticationRequest restAuthenticationRequest, Device device) {
        String username=restAuthenticationRequest.getUsername();
        String password=restAuthenticationRequest.getPassword();
        logger.debug("parameter name is:" +username);
        logger.debug("parameter name is:" +password);


        try {
            Authentication notFullyAuthentication = new UsernamePasswordAuthenticationToken(username, password);
            final Authentication authentication = authenticationManager.authenticate(notFullyAuthentication);
            UserDetails userDetails = userService.findByUsernameIgnoreCase(username);

            //how to change the token responsebody from string to the following format {"token": "xxx.xxx.xxx"}
            final String token = jwtTokenUtil.generateToken(userDetails, device);
            HashMap<String,String> tokenString=new HashMap<>();
            tokenString.put("Token",token);
            return ResponseEntity.ok(tokenString);

        } catch (AuthenticationException ex) {
            logger.info("failed message", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid username or password");
        }
    }



}

