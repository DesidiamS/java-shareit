package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.DuplicateException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void whenGetUserByIdThenReturnUser() {
        Mockito.when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        User user = userService.findById(1L);
        assertThat(user, notNullValue());
    }

    @Test
    public void whenUserNotFoundThenThrowException() {
        Mockito.when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    public void whenCreateUserThenReturnUser() {
        User expectedUser = new User(null, "John", "john@email.ru");
        Mockito
                .when(mockUserRepository.findByEmailIgnoreCase(anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(mockUserRepository.save(expectedUser)).thenReturn(expectedUser);
        User user = userService.createUser(expectedUser);
        assertThat(user, notNullValue());
        assertThat(user, equalTo(expectedUser));
    }

    @Test
    public void whenDoubleUserThenThrowException() {
        User user = new User(null, "John", "john@email.ru");
        Mockito.when(mockUserRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        assertThrows(DuplicateException.class, () -> userService.createUser(user));
    }

    @Test
    public void whenUpdateUserThenReturnUser() {
        Mockito.when(mockUserRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        User expectedUser = new User(null, "John", "john@email.ru");
        Mockito
                .doNothing()
                .when(mockUserRepository)
                .patchUser(anyString(), anyString(), anyLong());

        User user = userService.updateUser(1L, expectedUser);
        assertThat(user, notNullValue());
        assertThat(user.getId(), equalTo(1L));
    }

    @Test
    public void whenUpdateWrongUserThenThrowException() {
        Mockito.when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, new User()));
    }

    @Test
    public void whenDeleteWrongUserThenThrowException() {
        Mockito.when(mockUserRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
    }
}
