package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository mockItemRequestRepository;

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    public void whenCreateItemRequestThenReturnItemRequest() {
        Mockito
                .when(mockUserService.findById(Mockito.anyLong()))
                .thenReturn(Mockito.mock(User.class));
        ItemRequest expectedItemRequest = new ItemRequest(null, "test description", new User(),
                Timestamp.valueOf(LocalDateTime.now()));

        Mockito
                .when(mockItemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(expectedItemRequest);

        ItemRequest itemRequest = itemRequestService.create(1L, new ItemRequestDto());

        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest.getDescription(), equalTo("test description"));
    }

    @Test
    public void whenNotFoundByIdThenThrowException() {
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.getById(-1L));
    }
}
