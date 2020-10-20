package com.banchango.warehousereviews.service;

import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehousereviews.WarehouseReviews;
import com.banchango.domain.warehousereviews.WarehouseReviewsRepository;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.tools.ObjectMaker;
import com.banchango.warehousereviews.dto.WarehouseReviewResponseDto;
import com.banchango.warehouses.exception.WarehouseIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WarehouseReviewsService {

    private final WarehouseReviewsRepository reviewsRepository;
    private final WarehousesRepository warehousesRepository;
    private final UsersRepository usersRepository;

    public org.json.simple.JSONObject getWarehouseReviewsById(Integer warehouseId, Integer limit, Integer offset) throws Exception {
        if(!warehousesRepository.findById(warehouseId).isPresent()) throw new WarehouseIdNotFoundException();
        PageRequest request = PageRequest.of(offset, limit);
        List<WarehouseReviewResponseDto> list = reviewsRepository.findByWarehouseId(warehouseId, request).stream().map(WarehouseReviewResponseDto::new).collect(Collectors.toList());
        org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
        org.json.simple.JSONArray jsonArray = ObjectMaker.getSimpleJSONArray();
        for(WarehouseReviewResponseDto dto : list) {
            Users user = usersRepository.findById(dto.getWriterId()).orElseThrow(Exception::new);
            org.json.simple.JSONObject objectOfUser = ObjectMaker.getJSONObjectWithUserInfo(user);
            org.json.simple.JSONObject jTemp = ObjectMaker.getSimpleJSONObject();
            jTemp.putAll(dto.convertMap());
            jTemp.put("writer", objectOfUser);
            jsonArray.add(jTemp);
        }
        jsonObject.put("reviews", jsonArray);
        return jsonObject;
    }
}
