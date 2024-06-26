package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exceptions.IncorrectItemException;
import ru.practicum.shareit.exceptions.IncorrectUserIdException;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid ItemDto ItemDto,
                              @RequestHeader("X-Sharer-User-Id") @NonNull @NotBlank long userId) {

        log.info("ItemController: createItem");
        try {
            return itemService.createItem(ItemDto, userId);

        } catch (IncorrectUserIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Ошибка создания вещи! " + e.getMessage());

        } catch (IncorrectItemException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ошибка создания вещи! " + e.getMessage());
        }
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestBody ItemDto ItemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("ItemController: updateItem");
        try {
            return itemService.updateItem(itemId, ItemDto, userId);

        } catch (IncorrectUserIdException | IncorrectItemException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Ошибка редактирования вещи! " + e.getMessage());
        }
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {

        log.info("ItemController: getItem");
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsUser(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("ItemController: getItemsUser");
        return itemService.getItemsUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {

        log.info("ItemController: searchItems");
        return itemService.searchItems(text);
    }

}
