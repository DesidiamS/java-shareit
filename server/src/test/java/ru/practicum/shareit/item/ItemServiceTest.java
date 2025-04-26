package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository mockItemRepository;

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void whenAddItemThenReturnItem() {
        Item item = new Item("test_item", "description", true, new User(), null);
        ItemDto itemDto = new ItemDto("test_item", "description", true, null);


        Mockito
                .when(mockUserRepository.findById(any()))
                .thenReturn(Optional.of(new User()));

        try (MockedStatic<ItemMapper> mockedStatic = mockStatic(ItemMapper.class)) {
            mockedStatic.when(() -> ItemMapper.dtoToItem(any(), any()))
                    .thenReturn(item);
        }

        Mockito
                .when(mockItemRepository.save(any()))
                .thenReturn(item);

        item = itemService.createItem(new User(), itemDto);

        assertThat(item, notNullValue());
        assertThat(item.getName(), equalTo(item.getName()));
    }

    @Test
    public void whenWrongUserThenException() {
        ItemDto itemDto = new ItemDto("test_item", "description", true, null);

        try (MockedStatic<ItemMapper> mockedStatic = mockStatic(ItemMapper.class)) {
            mockedStatic.when(() -> ItemMapper.dtoToItem(any(), any()))
                    .thenReturn(new Item());
        }

        Mockito
                .when(mockUserRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.createItem(new User(), itemDto));
    }

    @Test
    public void whenUpdateItemThenReturnItem() {
        User user = new User(1L, "John", "john@email.ru");
        Item item = new Item("test_item", "description", true, user, null);
        Mockito
                .when(mockItemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        try (MockedStatic<ItemMapper> mockedStatic = mockStatic(ItemMapper.class)) {
            mockedStatic.when(() -> ItemMapper.dtoToItem(any(), any()))
                    .thenReturn(item);
        }

        ItemDto itemDto = new ItemDto("test_item2", "description", true, null);

        Mockito
                .doNothing()
                .when(mockItemRepository)
                .patchItem(any(), any(), any(), any());

        Item expectedItem = itemService.updateItem(user, item.getId(), itemDto);

        assertThat(expectedItem, notNullValue());
        assertThat(expectedItem.getName(), equalTo(itemDto.getName()));
    }

    @Test
    public void whenNoItemThenException() {
        Mockito
                .when(mockItemRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.updateItem(new User(), -1L, null));
    }

    @Test
    public void whenNotOwnerThenException() {
        User user = new User(1L, null, null);
        Item item = new Item("test_item", "description", true, user, null);
        Mockito
                .when(mockItemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> itemService.updateItem(new User(), -1L, null));
    }

    @Test
    public void whenEmptySearchThenEmptyList() {
        Collection<Item> items = itemService.searchItem("");
        assertThat(items, equalTo(Collections.EMPTY_LIST));
    }
}
