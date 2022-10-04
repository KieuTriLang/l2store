package com.ktl.l2store.service.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemNotfoundException;

public interface UserService {

    User saveUser(User user);

    Role saveRole(Role role);

    User getUser(String username) throws ItemNotfoundException;

    User updateUser(User user) throws ItemNotfoundException;

    List<Role> getRoles();

    Page<User> getUsers(Pageable pageable);

    void addRoleToUser(String username, String roleName) throws ItemNotfoundException;

    void removeRoleFromUser(String username, String roleName) throws ItemNotfoundException;

}
