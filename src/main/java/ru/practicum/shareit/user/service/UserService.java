package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

public interface UserService {

    User findById(Long id);

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
