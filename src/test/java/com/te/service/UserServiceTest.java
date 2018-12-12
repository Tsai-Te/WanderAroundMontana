package com.te.service;

import com.te.config.AppConfig;
import com.te.domain.Authority;
import com.te.domain.User;
import com.te.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("unit")
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void findByUsernameTest(){
        User expectedResult=new User();
        expectedResult.setUsername("te");
        expectedResult.setEmail("te@gmail.com");
        expectedResult.setPassword("123456");
        userRepository.save(expectedResult);
        User actualResult=userService.findByUsernameIgnoreCase(expectedResult.getUsername());
        assertEquals(expectedResult.getId(), actualResult.getId());
    }

    @Test
    @Transactional
    public void createUserTest(){
        //1. constructor a user object x
        //invoke createUser() x
        //new user created with primary key ID x
        //retrieve user instance/object from database user x
        //compare one of the unique attribute e.g username x
        // user object in step 1 should has original password,
        // which is not encoded. user object retrieved from database should be encoded. and they are not equal.


        User newUser=new User();
        String password = "123456";
        Authority authority=new Authority();
        newUser.setUsername("tete");
        newUser.setEmail("tete@hotmail.com");
        newUser.setPassword(password);
//        Authority newUserAuthority=

        User user=userService.createUser(newUser);

        User actualUser=userService.findById(newUser.getId());

        assertEquals(newUser.getUsername(), actualUser.getUsername());
        assertNotEquals(password,actualUser.getPassword());
    }


}
