package com.samdev.customizedOauth20.ServiceImpl;

import com.samdev.customizedOauth20.DTO.UserDTO;
import com.samdev.customizedOauth20.Entity.User;
import com.samdev.customizedOauth20.Entity.UserRegistrationVerification;
import com.samdev.customizedOauth20.Repository.UserForgotPasswordOtpRepository;
import com.samdev.customizedOauth20.Repository.UserRegistrationVerificationRepository;
import com.samdev.customizedOauth20.Repository.UserRepository;
import com.samdev.customizedOauth20.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRegistrationVerificationRepository userRegistrationVerificationRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserForgotPasswordOtpRepository userForgotPasswordOtpRepository;

    public UserServiceImpl(UserRepository userRepository, UserRegistrationVerificationRepository userRegistrationVerificationRepository, PasswordEncoder encoder, AuthenticationManager authenticationManager, JWTService jwtService, UserForgotPasswordOtpRepository userForgotPasswordOtpRepository) {
        this.userRepository = userRepository;
        this.userRegistrationVerificationRepository = userRegistrationVerificationRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userForgotPasswordOtpRepository = userForgotPasswordOtpRepository;
    }

    @Override
    public ResponseEntity<UserDTO> registerUser(UserDTO userDTO) {
        UserDTO userDTO1 = new UserDTO();
        User user = new User();
        UserRegistrationVerification userRegistrationVerification = new UserRegistrationVerification();

        if(userRepository.existsByEmail(userDTO.getEmail()) || userRepository.existsByUsername(userDTO.getUsername())){
            userDTO1.setResponseMessage("User already exists");
            userDTO1.setStatusCode(400);
            return new ResponseEntity<>(userDTO1, HttpStatus.BAD_REQUEST);
        }
        try{
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(encoder.encode(userDTO.getPassword()));

            user.setRole("basic_user");

            userRepository.save(user);

            //todo: implement activation of account via otp
            Random random = new Random();
            Integer otp = random.nextInt(9000) + 1000;


            userRegistrationVerification.setUsername(userDTO.getUsername());
            userRegistrationVerification.setOtp(otp);

            userRegistrationVerification.setExpired(new Date(System.currentTimeMillis() + 30*60*1000));

            userRegistrationVerification.setUsed(Boolean.FALSE);

            userRegistrationVerificationRepository.save(userRegistrationVerification);

            String subject = "Activate account OTP";
            String body = "Use this otp " + otp + " to activate your account";

            // todo implement email sending capability to the user for the otp

            userDTO1.setStatusCode(201);
            userDTO1.setResponseMessage("User created successfully");

            return new ResponseEntity<>(userDTO1, HttpStatus.CREATED);

        }catch(Exception e){
            userDTO1.setStatusCode(500);
            userDTO1.setResponseMessage("An error occurred");

            return new ResponseEntity<>(userDTO1, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<UserDTO> loginParentUser(UserDTO userDTO) {

        UserDTO userDTO1 = new UserDTO();
        try{

            User user = userRepository.findByUsername(userDTO.getUsername());
            if(user == null){
                userDTO1.setResponseMessage("user does not exist!");
                userDTO1.setStatusCode(404);
                return new ResponseEntity<>(userDTO1, HttpStatus.NOT_FOUND);
            }
            if(user.getIsActive() == 0){
                userDTO1.setResponseMessage("Account is not active");
                userDTO1.setStatusCode(400);
                return new ResponseEntity<>(userDTO1, HttpStatus.BAD_REQUEST);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(),userDTO.getPassword()));
            String token = jwtService.GenerateToken(user);

            userDTO1.setToken(token);
            userDTO1.setStatusCode(200);
            userDTO1.setResponseMessage("Login successful");

            return new ResponseEntity<>(userDTO1, HttpStatus.OK);

        }catch(Exception e){
            userDTO1.setResponseMessage("Internal Server Error occurred!" + e.getMessage());
            userDTO1.setStatusCode(500);
            return new ResponseEntity<>(userDTO1, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
