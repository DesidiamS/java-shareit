package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    Map<Long, User> userList = new LinkedHashMap<>();
    private Long sequence = 0L;

    @Override
    public User findById(Long id) {
        User user = userList.get(id);

        if (user == null) {
            throw new NotFoundException("Пользователь не найден!");
        }

        return user;
    }

    @Override
    public User createUser(User user) {
        checkUserEmailDuplicate(user);

        sequence += 1L;

        user.setId(sequence);

        userList.put(sequence, user);
        return findById(sequence);
    }

    @Override
    public User updateUser(Long id, User user) {
        findById(id);

        if (user.getEmail() != null) {
            checkUserEmailDuplicate(user);
        }

        user.setId(id);

        userList.replace(id, user);

        return findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        findById(id);

        userList.remove(id);
    }

    private void checkUserEmailDuplicate(User user) {
        for (User existedUser : userList.values()) {
            if (existedUser.getEmail() != null && existedUser.getEmail().equals(user.getEmail())) {
                throw new DuplicateException("Пользователь с таким Email уже существует!");
            }
        }
    }
}
