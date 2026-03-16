package com.tcs.fullstack.items.fullstackitems.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tcs.fullstack.items.fullstackitems.data.ItemEntity;

@Service
public class ItemsServiceImpl implements ItemsService {

    @Override
    public List<ItemEntity> getItems(String userId) {
        List<ItemEntity> returnValue = new ArrayList<>();

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setUserId(userId);
        itemEntity.setItemId("item1Id");
        itemEntity.setDescription("item 1 description");
        itemEntity.setId(1L);
        itemEntity.setName("item 1 name");
        
        ItemEntity itemEntity2 = new ItemEntity();
        itemEntity2.setUserId(userId);
        itemEntity2.setItemId("item2Id");
        itemEntity2.setDescription("item 2 description");
        itemEntity2.setId(2L);
        itemEntity2.setName("item 2 name");
        
        returnValue.add(itemEntity);
        returnValue.add(itemEntity2);
        
        return returnValue;
    }

}
