package com.ktl.l2store.service.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.repo.RoleRepo;
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
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("user not found in database");
            throw new UsernameNotFoundException("Not found user in database");
        } else {
            log.info("found user {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    @Override
    public User saveUser(User user) {
        // TODO Auto-generated method stub
        log.info("ok");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        // TODO Auto-generated method stub
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        // TODO Auto-generated method stub
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        // TODO Auto-generated method stub
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        // TODO Auto-generated method stub
        return userRepo.findAll();
    }

}
