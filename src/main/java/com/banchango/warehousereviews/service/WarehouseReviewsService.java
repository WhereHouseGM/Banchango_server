package com.banchango.warehousereviews.service;

import com.banchango.domain.users.Users;
import com.banchango.domain.users.UsersRepository;
import com.banchango.domain.warehousereviews.WarehouseReviews;
import com.banchango.domain.warehousereviews.WarehouseReviewsRepository;
import com.banchango.domain.warehouses.WarehousesRepository;
import com.banchango.tools.ObjectMaker;
import com.banchango.warehousereviews.dto.WarehouseReviewInsertRequestDto;
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
            Users user = usersRepository.findById(dto.getUserId()).orElseThrow(Exception::new);
            org.json.simple.JSONObject objectOfUser = ObjectMaker.getJSONObjectWithUserInfo(user);
            org.json.simple.JSONObject jTemp = ObjectMaker.getSimpleJSONObject();
            jTemp.putAll(dto.convertMap());
            jTemp.put("writer", objectOfUser);
            jsonArray.add(jTemp);
        }
        jsonObject.put("reviews", jsonArray);
        return jsonObject;
    }

    public org.json.simple.JSONObject register(Integer warehouseId, WarehouseReviewInsertRequestDto dto) throws Exception {
        // TODO : JWT에서 user id 값 가져오기, 코드 작동을 위해 1로 임시 지정함
        int userId = 1;
        WarehouseReviews review = WarehouseReviews.builder()
                .rating(dto.getRating())
                .content(dto.getContent())
                .userId(userId)
                .warehouseId(warehouseId).build();
        org.json.simple.JSONObject jsonObject = ObjectMaker.getSimpleJSONObject();
        jsonObject.put("id", userId);
        jsonObject.put("rating", dto.getRating());
        jsonObject.put("content", dto.getContent());
        jsonObject.put("writer", ObjectMaker.getJSONObjectWithUserInfo(usersRepository.findById(userId).get()));
        return jsonObject;
    }

    public void delete(int reviewId, int warehouseId) throws Exception {
        reviewsRepository.deleteByIdAndWarehouseId(reviewId, warehouseId);
    }
}
