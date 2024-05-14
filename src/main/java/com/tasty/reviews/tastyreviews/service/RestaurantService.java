package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.domain.Restaurant;
import com.tasty.reviews.tastyreviews.dto.RestaurantDTO;
import com.tasty.reviews.tastyreviews.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantDTO addRestaurant(RestaurantDTO restaurantDTO) {
        // DTO를 엔티티로 변환
        Restaurant restaurant = restaurantDTO.toEntity();

        // 레스토랑 등록
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // 등록된 레스토랑 정보를 다시 DTO로 변환하여 반환
        return RestaurantDTO.fromEntity(savedRestaurant);
    }

    public void saveRestaurantsFromApiResponse(String jsonResponse) throws JSONException {
        JSONArray restaurantsArray = new JSONObject(jsonResponse).getJSONArray("documents");
        for (int i = 0; i < restaurantsArray.length(); i++) {
            JSONObject restaurantJson = restaurantsArray.getJSONObject(i);

            String name = restaurantJson.optString("place_name", "Unknown Name"); // "place_name"을 사용하고 기본값 설정
            String address = restaurantJson.optString("address_name", "No Address Provided");

            // 중복 검사
            Optional<Restaurant> existingRestaurant = restaurantRepository.findByPlaceNameAndRoadAddressName(name, address);
            if (existingRestaurant.isEmpty()) { // 중복이 없으면 새로 저장
                Restaurant restaurant = new Restaurant();
                restaurant.setPlaceName(name);
                restaurant.setRoadAddressName(address);
                restaurant.setCategoryName(restaurantJson.getString("category_name"));
                restaurant.setPlaceUrl(restaurantJson.getString("place_url"));
                restaurant.setPhone(restaurantJson.getString("phone"));
                restaurant.setViewCount(0);  // 초기 조회수는 0으로 설정
                restaurant.setX(restaurantJson.optString("x", "0"));    // 위도, 기본값 0
                restaurant.setY(restaurantJson.optString("y", "0"));    // 경도, 기본값 0

                restaurantRepository.save(restaurant);
            }
        }
    }

    public RestaurantDTO findByPlace(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 음식점은 존재하지 않습니다." + id));

        return new RestaurantDTO().fromEntity(restaurant);
    }



/*    private boolean isAdmin(String username) {
        // 여기에 사용자 이름(username)을 기반으로 사용자 역할을 확인하는 로직을 추가하세요.
        // 예를 들어, 사용자 이름으로 데이터베이스에서 해당 사용자를 조회하여 역할을 확인할 수 있습니다.
        // 이 예제에서는 단순히 "admin"이라는 이름을 가진 사용자만이 관리자로 간주합니다.
        return "admin".equals(username);
    }*/
}
