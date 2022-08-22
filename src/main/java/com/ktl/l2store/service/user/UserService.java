package com.ktl.l2store.service.user;

import java.util.List;

import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);

    List<User> getUsers();
}
