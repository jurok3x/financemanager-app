package com.financemanager.demo.site.mapper;

import com.financemanager.demo.site.dto.ItemDTO;
import com.financemanager.demo.site.entity.Item;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ItemMapper {
    
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    
    public ItemDTO toItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setDate(item.getDate());
        itemDTO.setUser(userMapper.toUserDTO(item.getUser()));
        itemDTO.setCategory(categoryMapper.toCategoryDTO(item.getCategory()));
        return itemDTO;
    }
    
    public Item toItem(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setDate(itemDTO.getDate());
        item.setUser(userMapper.toUser(itemDTO.getUser()));
        item.setCategory(categoryMapper.toCategory(itemDTO.getCategory()));
        return item;
    }

}
