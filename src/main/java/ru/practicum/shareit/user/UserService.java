package ru.practicum.shareit.user;

public interface UserService {

    User findById(Long id);

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
