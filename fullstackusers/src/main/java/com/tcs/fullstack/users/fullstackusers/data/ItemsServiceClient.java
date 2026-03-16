package com.tcs.fullstack.users.fullstackusers.data;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tcs.fullstack.users.fullstackusers.ui.model.ItemResponseModel;

@FeignClient(name="items-ws")
public interface ItemsServiceClient {

    @GetMapping("/users/{id}/items")
    public List<ItemResponseModel> getItems(@PathVariable String id);

}
