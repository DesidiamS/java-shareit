package ru.practicum.shareit.item.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findAllByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i WHERE i.available = TRUE AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchString, '%')))")
    Collection<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchString);

    @Modifying
    @Transactional
    @Query("UPDATE Item i SET i.description = COALESCE(:description, i.description), i.name = COALESCE(:name, i.name), " +
            "i.available = COALESCE(:available, i.available) WHERE i.id = :id ")
    void patchItem(@Param("description") String description,
                   @Param("name") String name,
                   @Param("available") Boolean available,
                   @Param("id") Long id);
}
