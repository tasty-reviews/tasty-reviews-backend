package com.tasty.reviews.tastyreviews.restaruant.service;

import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.restaruant.dto.RestaurantDTO;
import com.tasty.reviews.tastyreviews.restaruant.repository.RestaurantRepository;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

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
            String placeId = restaurantJson.optString("id", "No id");

            // 중복 검사
            Optional<Restaurant> existingRestaurant = restaurantRepository.findByPlaceNameAndRoadAddressName(name, address);
            if (existingRestaurant.isEmpty()) { // 중복이 없으면 새로 저장
                Restaurant restaurant = new Restaurant();
                restaurant.setPlaceName(name);
                restaurant.setRoadAddressName(address);
                restaurant.setCategoryName(restaurantJson.getString("category_name"));
                restaurant.setPlaceUrl(restaurantJson.getString("place_url"));
                restaurant.setPhone(restaurantJson.getString("phone"));
                restaurant.setPlaceId(placeId);
                restaurant.setViewCount(0);  // 초기 조회수는 0으로 설정
                restaurant.setReviewCount(0);
                restaurant.setX(restaurantJson.optString("x", "0"));    // 위도, 기본값 0
                restaurant.setY(restaurantJson.optString("y", "0"));    // 경도, 기본값 0

                restaurantRepository.save(restaurant);
            }
        }
    }

    public RestaurantDTO findByPlace(String placeId) {
//        Restaurant restaurant = restaurantRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 음식점은 존재하지 않습니다." + id));

        Restaurant restaurant = restaurantRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 음식점이 없습니다"));

        List<Review> findReviews = reviewRepository.findByRestaurantId(restaurant.getId());
        restaurant.setReviews(findReviews);

        restaurant.setViewCount(restaurant.getViewCount() + 1);

        return new RestaurantDTO().fromEntity(restaurant);
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getRankedRestaurantsByViewCount() {
        return restaurantRepository.getRankingByViewCount();
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getRankedRestaurantsByReviewCount() {
        return restaurantRepository.getRankingByReviewCount();
    }

    public Optional<Restaurant> findByPlaceNameAndRoadAddressName(String placeName, String roadAddressName) {
        return restaurantRepository.findByPlaceNameAndRoadAddressName(placeName, roadAddressName);
    }
}
