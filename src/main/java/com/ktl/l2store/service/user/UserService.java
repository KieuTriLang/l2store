package com.ktl.l2store.service.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ktl.l2store.entity.Role;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemNotfoundException;

public interface UserService {

    List<User> getAllUser();

    User saveUser(User user);

    Role saveRole(Role role);

    boolean checkUserNameExist(String username);

    boolean checkEmailExist(String username);

    User getUser(String username) throws ItemNotfoundException;

    User getUserByEmail(String emailAddress) throws ItemNotfoundException;

    User updateUser(User user) throws ItemNotfoundException;

    void resetPassByToken(String token, String pass);

    void changePassword(String username, String oldPassword, String newPassword);

    List<Role> getRoles();

    Page<User> getUsers(Pageable pageable);

    Page<User> getUsersByRole(String roleName, Pageable pageable);

    void addRoleToUser(String username, String roleName) throws ItemNotfoundException;

    void removeRoleFromUser(String username, String roleName) throws ItemNotfoundException;

    void saveMultiUser(List<User> users);

}
