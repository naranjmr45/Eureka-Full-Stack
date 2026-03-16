package com.tcs.fullstack.items.fullstackitems.io.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.fullstack.items.fullstackitems.data.ItemEntity;
import com.tcs.fullstack.items.fullstackitems.models.ItemResponseModel;
import com.tcs.fullstack.items.fullstackitems.service.ItemsService;

@RestController
@RequestMapping("/users/{id}/items")
public class ItemsController {

    @Autowired
    ItemsService itemsService;

    @GetMapping
    public List<ItemResponseModel> userItems(@PathVariable String id) {

        List<ItemResponseModel> returnValue = new ArrayList<>();
        
        List<ItemEntity> itemEntities = itemsService.getItems(id);
        
        if(itemEntities == null || itemEntities.isEmpty())
        {
            return returnValue;
        }
        
        Type listType = new TypeToken<List<ItemResponseModel>>(){}.getType();
 
        returnValue = new ModelMapper().map(itemEntities, listType);
        return returnValue;
    }
}
