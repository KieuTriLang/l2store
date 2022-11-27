package com.ktl.l2store.service.user;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.Token;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemExistException;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.RoleRepo;
import com.ktl.l2store.repo.TokenRepo;
import com.ktl.l2store.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private TokenRepo tokenRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("");
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ItemNotfoundException("Not found user"));
        boolean enable = user.isEnable();
        boolean isUser = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_USER"));
        if (!enable && isUser) {
            throw new ItemNotfoundException("Your account is not actived!");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    @Override
    public User saveUser(User user) {

        if (userRepo.existsUserByUsername(user.getUsername()))
            throw new ItemExistException("Username is exist");
        if (userRepo.existsUserByEmail(user.getEmail()))
            throw new ItemExistException("Email is exist");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {

        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) throws ItemNotfoundException {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ItemNotfoundException("Not found user: " + username));
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ItemNotfoundException("Not found role: " + roleName));
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
        }
    }

    @Override
    public User getUser(String username) {

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ItemNotfoundException("Not found user: " + username));
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {

        return userRepo.findAll(pageable);
    }

    @Override
    public List<Role> getRoles() {

        return roleRepo.findAll();
    }

    @Override
    public void removeRoleFromUser(String username, String roleName) throws ItemNotfoundException {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ItemNotfoundException("Not found user: " + username));
        user.getRoles().removeIf(item -> item.getName().toLowerCase().equals(roleName));
    }

    @Override
    public User updateUser(User user) throws ItemNotfoundException {

        User record = userRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new ItemNotfoundException("Not found user: " + user.getUsername()));
        record.setFirstName(user.getFirstName());
        record.setLastName(user.getLastName());
        record.setGender(user.isGender());

        record.setAddress(user.getAddress());
        record.setDob(user.getDob());
        record.setUpdatedAt(ZonedDateTime.now(ZoneId.of("Z")));
        if (user.getAvatar() != null) {
            record.getAvatar().setData(user.getAvatar().getData());
            record.getAvatar().setName(user.getAvatar().getName());
            record.getAvatar().setType(user.getAvatar().getType());
        }

        return userRepo.save(record);
    }

    @Override
    public boolean checkUserNameExist(String username) {
        // TODO Auto-generated method stub

        return userRepo.existsUserByUsername(username);
    }

    @Override
    public boolean checkEmailExist(String username) {
        // TODO Auto-generated method stub

        return userRepo.existsUserByEmail(username);
    }

    @Override
    public User getUserByEmail(String emailAddress) throws ItemNotfoundException {
        // TODO Auto-generated method stub
        return userRepo.findByEmail(emailAddress).orElseThrow(() -> new ItemNotfoundException("Email not exist"));
    }

    @Override
    public void resetPassByToken(String token, String pass) {
        // TODO Auto-generated method stub
        Token tokenRecord = tokenRepo.findByToken(token)
                .orElseThrow(() -> new ItemNotfoundException("not found token"));
        User user = tokenRecord.getUser();

        user.setPassword(passwordEncoder.encode(pass));

        userRepo.save(user);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ItemNotfoundException("Current password incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepo.save(user);
    }

    @Override
    public Page<User> getUsersByRole(String roleName, Pageable pageable) {
        // TODO Auto-generated method stub
        return userRepo.findByRolesName(roleName, pageable);
    }

    @Override
    public void saveMultiUser(List<User> users) {
        // TODO Auto-generated method stub
        List<User> records = userRepo.saveAll(users);
        for (User user : records) {
            user.getRoles().add(roleRepo.findByName("ROLE_USER").orElse(null));
        }
    }

    @Override
    public List<User> getAllUser() {
        // TODO Auto-generated method stub
        return userRepo.findByRolesName("ROLE_USER");
    }

}
