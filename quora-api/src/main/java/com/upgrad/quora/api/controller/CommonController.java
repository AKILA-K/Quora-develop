package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.SignoutService;
import com.upgrad.quora.service.business.UserProfileService;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping(method = RequestMethod.GET, path="/userprofile/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId")
                                              final String uuid, @RequestHeader("authorization") final String authorization)
                                              throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity = userProfileService.fetchUser(uuid, authorization);
        if(userEntity!=null) {
            UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(userEntity.getFirstName())
                    .lastName(userEntity.getLastName()).emailAddress(userEntity.getEmail())
                    .contactNumber(userEntity.getContactNumber()).userName(userEntity.getUserName())
                    .country(userEntity.getCountry()).aboutMe(userEntity.getAboutme())
                    .dob(userEntity.getDob());
            // .status(UserStatusType.valueOf(UserStatus.getEnum(userEntity.getStatus()).name()));
            return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
        } else {
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }

    }
}
