package com.tcs.fullstack.items.fullstackitems.service;

import java.util.List;

import com.tcs.fullstack.items.fullstackitems.data.ItemEntity;

public interface ItemsService {
    List<ItemEntity> getItems(String userId);
}
