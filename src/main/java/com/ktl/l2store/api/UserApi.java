package com.ktl.l2store.api;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktl.l2store.common.AuthorizationHeader;
import com.ktl.l2store.common.RegisterForm;
import com.ktl.l2store.dto.req.ReqUserDto;
import com.ktl.l2store.dto.res.UserDto;
import com.ktl.l2store.entity.FileDB;
import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.service.user.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    // Get all info user
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getUsers() {

        List<UserDto> userDtos = userService.getUsers().stream().map(i -> mapper.map(i, UserDto.class)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }

    // Get user by user name
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserByUsername(@PathVariable("username") String username)
            throws ItemNotfoundException {
        UserDto userDto = mapper.map(userService.getUser(username), UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    // Update user
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> requestMethodName(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestPart ReqUserDto reqUserDto,
            @RequestPart(required = false) MultipartFile file) throws IOException {
        String username = AuthorizationHeader.getSub(authorizationHeader);

        User user = User.builder()
                .username(username)
                .displayName(reqUserDto.getDisplayName())
                .gender(reqUserDto.getGender() > 0 ? true : false)
                .address(reqUserDto.getAddress())
                .dob(reqUserDto.getDob())
                .build();
        if (file != null) {
            user.setAvatar(
                    new FileDB(null, null, file.getBytes(), file.getOriginalFilename(), file.getContentType()));
        }

        mapper.getTypeMap(User.class, UserDto.class).addMapping(u -> u.getAvatar().getFileCode(),
                UserDto::setImageUrl);

        return ResponseEntity.status(HttpStatus.OK).body(mapper.map(userService.updateUser(user), UserDto.class));

    }

    // Register user
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody RegisterForm form) {

        User newUser = User.builder()
                .id(null)
                .username(form.getUsername())
                .displayName(form.getUsername())
                .email("")
                .password(form.getPassword())
                .gender(true)
                .avatar(new FileDB(null, Calendar.getInstance().getTimeInMillis(), null, null, null))
                .address("")
                .dob(ZonedDateTime.now(ZoneId.of("Z")))
                .roles(new ArrayList<>())
                .favProducts(new ArrayList<>())
                .bills(new ArrayList<>())
                .comboProducts(new ArrayList<>())
                .updatedAt(ZonedDateTime.now(ZoneId.of("Z"))).build();

        UserDto userDto = mapper.map(userService.saveUser(newUser), UserDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    // Add role to user
    @RequestMapping(value = "/role/add", method = RequestMethod.POST)
    public void addRoleToUser(
            @RequestParam("username") String username,
            @RequestParam("roleName") String roleName) throws ItemNotfoundException {

        userService.addRoleToUser(username, roleName);

    }

    @RequestMapping(value = "/role/remove", method = RequestMethod.POST)
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
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
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
