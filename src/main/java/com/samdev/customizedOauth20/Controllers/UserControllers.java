package com.samdev.customizedOauth20.Controllers;

import com.samdev.customizedOauth20.DTO.UserDTO;
import com.samdev.customizedOauth20.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/apps/api/v1/auth/")
public class UserControllers {

    private final UserService userService;

    public UserControllers(UserService userService) {
        this.userService = userService;
    }

    //create parent user.
    @PostMapping("register-user/")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO){
        return userService.registerUser(userDTO);
    }

    @PostMapping("login/")
    public ResponseEntity<UserDTO> loginParentUser(@RequestBody UserDTO userDTO){
        return userService.loginParentUser(userDTO);
    }

}
