package com.example.belolipetsckiy.belolipetsckiy_boot.service;

import com.example.belolipetsckiy.belolipetsckiy_boot.models.User;

import java.util.List;

public interface UserService {
    public List<User> index();
    public User show(int id);
    public void save(User user);
    public void update(User user);
    public void delete(int id);
    User getUserByName(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
