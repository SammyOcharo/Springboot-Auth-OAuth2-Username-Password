package com.samdev.customizedOauth20.Service;

import com.samdev.customizedOauth20.DTO.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<UserDTO> registerUser(UserDTO userDTO);

    ResponseEntity<UserDTO> loginParentUser(UserDTO userDTO);
}
