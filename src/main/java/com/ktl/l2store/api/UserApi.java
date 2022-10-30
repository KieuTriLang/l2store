package com.ktl.l2store.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktl.l2store.common.RegisterForm;
import com.ktl.l2store.dto.ReqChangePass;
import com.ktl.l2store.dto.ReqUpdateUser;
import com.ktl.l2store.dto.UserDto;
import com.ktl.l2store.entity.FileDB;
import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.Token;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.provider.AuthorizationHeader;
import com.ktl.l2store.service.Email.EmailService;
import com.ktl.l2store.service.Token.TokenService;
import com.ktl.l2store.service.user.UserService;
import com.ktl.l2store.utils.PagingParam;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper mapper;

    // Get all user or search
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> getUsers(
            @RequestParam(name = "search", required = false) String keyword,
            @PagingParam Pageable pageable) {

        Page<User> users = userService.getUsers(pageable);

        List<UserDto> userDtos = users.stream().map(u -> mapper.map(u, UserDto.class)).toList();

        Page<UserDto> resPageDto = new PageImpl<>(userDtos, pageable, users.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(resPageDto);
    }

    // Get all user or search
    @RequestMapping(value = "/role-user", method = RequestMethod.GET)
    public ResponseEntity<Object> getUsersRoleUser(
            @PagingParam Pageable pageable) {

        Page<User> users = userService.getUsersByRole("ROLE_USER", pageable);

        List<UserDto> userDtos = users.stream().map(u -> mapper.map(u, UserDto.class)).toList();

        Page<UserDto> resPageDto = new PageImpl<>(userDtos, pageable, users.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(resPageDto);
    }

    // Get user by user name
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserByUsername(@PathVariable("username") String username)
            throws ItemNotfoundException {
        UserDto userDto = mapper.map(userService.getUser(username), UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    // Get user by user name
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<Object> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader)
            throws ItemNotfoundException {
        String username = AuthorizationHeader.getSub(authorizationHeader);
        UserDto userDto = mapper.map(userService.getUser(username), UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    // Update user
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestPart String dataJson,
            @RequestPart(required = false) MultipartFile file) throws IOException {
        String username = AuthorizationHeader.getSub(authorizationHeader);

        ReqUpdateUser reqUserDto = new ObjectMapper().readValue(dataJson, ReqUpdateUser.class);
        User user = User.builder()
                .username(username)
                .firstName(reqUserDto.getFirstName())
                .lastName(reqUserDto.getLastName())
                .gender(reqUserDto.isGender())
                .address(reqUserDto.getAddress())
                .dob(reqUserDto.getDob().toInstant().atZone(ZoneId.of("Z")))
                .build();
        if (file != null) {
            user.setAvatar(
                    new FileDB(null, null, file.getBytes(), "avatar", file.getContentType()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(mapper.map(userService.updateUser(user), UserDto.class));

    }

    // Register user
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody RegisterForm form) throws IOException {

        // Path path = new
        // File("src\\main\\java\\com\\ktl\\l2store\\avatar.jpg").toPath();
        User newUser = User.builder()
                .id(null)
                .username(form.getUsername())
                .email(form.getEmail())
                .password(form.getPassword())
                .enable(false)
                .roles(new ArrayList<>())
                .favProducts(new ArrayList<>())
                .orders(new ArrayList<>())
                .comboProducts(new ArrayList<>())
                .updatedAt(ZonedDateTime.now(ZoneId.of("Z"))).build();

        User user = userService.saveUser(newUser);

        // .avatar(new FileDB(null, Calendar.getInstance().getTimeInMillis(),
        // Files.readAllBytes(path), null,
        // Files.probeContentType(path)))

        Token token = Token.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(ZonedDateTime.now(ZoneId.of("Z")))
                .expiresAt(ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(15))
                .user(user)
                .build();

        tokenService.saveToken(token);

        String link = "http://localhost:4200/confirm?token=" + token.getToken();

        String email = emailService.buildEmail(user.getUsername(), link);

        emailService.send(user.getEmail(), email);

        userService.addRoleToUser(user.getUsername(), "ROLE_USER");

        user = userService.getUser(user.getUsername());

        UserDto userDto = mapper.map(user, UserDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/re-confirm", method = RequestMethod.POST)
    public ResponseEntity<Object> reConfirmAccount(@RequestBody String emailAddress) {

        User user = userService.getUserByEmail(emailAddress);

        Token token = Token.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(ZonedDateTime.now(ZoneId.of("Z")))
                .expiresAt(ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(15))
                .user(user)
                .build();

        tokenService.saveToken(token);

        String link = "http://localhost:4200/confirm?token=" + token.getToken();

        String email = emailService.buildEmail(user.getUsername(), link);

        emailService.send(user.getEmail(), email);

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public ResponseEntity<Object> activeUser(@RequestParam("token") String token) {

        boolean confirm = tokenService.confirmToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(confirm);
    }

    @RequestMapping(value = "/{username}/exist", method = RequestMethod.GET)
    public ResponseEntity<Object> checkUserExist(@PathVariable String username) {

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<Object> resetPassword(@RequestBody String emailAddress) {

        User user = userService.getUserByEmail(emailAddress);

        Token token = Token.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(ZonedDateTime.now(ZoneId.of("Z")))
                .expiresAt(ZonedDateTime.now(ZoneId.of("Z")).plusMinutes(15))
                .user(user)
                .build();

        tokenService.saveToken(token);

        String link = "http://localhost:4200/reset-password?token=" + token.getToken();

        String email = emailService.buildEmailReset(user.getUsername(), link);

        emailService.send(user.getEmail(), email);

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ResponseEntity<Object> requestMethodName(@RequestParam("token") String token) {
        boolean confirm = tokenService.resetToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(confirm);
    }

    @RequestMapping(value = "/reset/{token}", method = RequestMethod.POST)
    public ResponseEntity<Object> requestMethodName(@PathVariable("token") String token, @RequestBody String password) {
        userService.resetPassByToken(token, password);
        return ResponseEntity.ok("");
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ResponseEntity<Object> changePass(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody ReqChangePass reqChangePass) {
        String username = AuthorizationHeader.getSub(authorizationHeader);

        userService.changePassword(username, reqChangePass.getOldPassword(), reqChangePass.getNewPassword());
        return ResponseEntity.ok("");
    }

    // Add role to user
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public void addRoleToUser(
            @RequestParam("username") String username,
            @RequestParam("roleName") String roleName) throws ItemNotfoundException {

        userService.addRoleToUser(username, roleName);

    }

    // Remove role from user
    @RequestMapping(value = "/role", method = RequestMethod.DELETE)
    public void removeRoleFromUser(
            @RequestParam("username") String username,
            @RequestParam("roleName") String roleName) throws ItemNotfoundException {

        userService.removeRoleFromUser(username, roleName);

    }

    // Refresh token
    @RequestMapping(value = "/token/refresh", method = RequestMethod.GET)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws JsonGenerationException, JsonMappingException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 8 * 60 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",
                                user.getRoles().stream().map(Role::getName)
                                        .collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                // TODO: handle exception
                // log.error("Error loging: {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

}
