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

import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemExistException;
import com.ktl.l2store.exception.ItemNotfoundException;
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
        log.info("");
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ItemNotfoundException("Not found user: " + username));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    @Override
    public User saveUser(User user) {
        // TODO Auto-generated method stub
        if (userRepo.existsUserByUsername(user.getUsername()))
            throw new ItemExistException("Username is exist");
        Role role = roleRepo.findByName("ROLE_USER").orElseThrow();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(role);
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        // TODO Auto-generated method stub
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) throws ItemNotfoundException {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ItemNotfoundException("Not found user: " + username));
    }

    @Override
    public List<User> getUsers() {
        // TODO Auto-generated method stub
        return userRepo.findAll();
    }

    @Override
    public List<Role> getRoles() {
        // TODO Auto-generated method stub
        return roleRepo.findAll();
    }

    @Override
    public void removeRoleFromUser(String username, String roleName) throws ItemNotfoundException {
        // TODO Auto-generated method stub
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ItemNotfoundException("Not found user: " + username));
        user.getRoles().removeIf(item -> item.getName().toLowerCase().equals(roleName));
    }

    @Override
    public User updateUser(User user) throws ItemNotfoundException {
        // TODO Auto-generated method stub
        User record = userRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new ItemNotfoundException("Not found user: " + user.getUsername()));
        record.setDisplayName(user.getDisplayName());
        record.setEmail(user.getEmail());
        record.setGender(user.isGender());
        record.getAvatar().setData(user.getAvatar().getData());
        record.getAvatar().setName(user.getAvatar().getName());
        record.getAvatar().setType(user.getAvatar().getType());
        record.setAddress(user.getAddress());
        record.setDob(user.getDob());
        record.setUpdatedAt(ZonedDateTime.now(ZoneId.of("Z")));
        return userRepo.save(record);
    }

    @Override
    public List<Product> getFavProducts(String username) {
        // TODO Auto-generated method stub
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));
        return user.getFavProducts().stream().toList();
    }

    @Override
    public List<ComboProduct> getCbProducts(String username) {
        // TODO Auto-generated method stub
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));
        return user.getComboProducts().stream().toList();
    }

    @Override
    public Page<User> getUserByUsernameContaining(String username, Pageable pageable) {
        // TODO Auto-generated method stub
        return userRepo.findByUsernameContaining(username, pageable);
    }

}
