package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
    }

    @Override
    public User createUser(User user) {
        checkUserEmailDuplicate(user);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

        if (user.getEmail() != null) {
            checkUserEmailDuplicate(user);
        }

        user.setId(id);

        userRepository.patchUser(user.getEmail(), user.getName(), id);

        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

        userRepository.deleteById(id);
    }

    private void checkUserEmailDuplicate(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new DuplicateException("Пользователь с таким Email уже существует!");
        }
    }
}
