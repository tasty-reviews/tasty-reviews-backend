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

/**
 * 음식점 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 음식점을 등록하는 메서드
     *
     * @param restaurantDTO 등록할 음식점 정보 DTO
     * @return 등록된 음식점 정보 DTO
     */
    public RestaurantDTO addRestaurant(RestaurantDTO restaurantDTO) {
        // DTO를 엔티티로 변환
        Restaurant restaurant = restaurantDTO.toEntity();
        // 레스토랑 등록
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        // 등록된 레스토랑 정보를 다시 DTO로 변환하여 반환
        return RestaurantDTO.fromEntity(savedRestaurant);
    }

    /**
     * 외부 API 응답으로부터 받은 음식점 정보를 저장하는 메서드
     *
     * @param jsonResponse 외부 API에서 받은 JSON 형식의 응답
     * @throws JSONException JSON 파싱 예외가 발생할 경우
     */
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

    /**
     * placeId를 이용해 음식점을 조회하는 메서드
     *
     * @param placeId 조회할 음식점의 placeId
     * @return 조회된 음식점 정보 DTO
     * @throws IllegalArgumentException 조회된 음식점이 없을 경우 예외 발생
     */
    public RestaurantDTO findByPlace(String placeId) {
        Restaurant restaurant = restaurantRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 음식점이 없습니다"));

        List<Review> findReviews = reviewRepository.findByRestaurantId(restaurant.getId());
        restaurant.setReviews(findReviews);

        restaurant.setViewCount(restaurant.getViewCount() + 1);

        return new RestaurantDTO().fromEntity(restaurant);
    }

    /**
     * 조회수 순으로 정렬된 음식점 목록을 반환하는 메서드
     *
     * @return 조회수 순으로 정렬된 음식점 목록
     */
    @Transactional(readOnly = true)
    public List<Restaurant> getRankedRestaurantsByViewCount() {
        return restaurantRepository.getRankingByViewCount();
    }

    /**
     * 리뷰 개수 순으로 정렬된 음식점 목록을 반환하는 메서드
     *
     * @return 리뷰 개수 순으로 정렬된 음식점 목록
     */
    @Transactional(readOnly = true)
    public List<Restaurant> getRankedRestaurantsByReviewCount() {
        return restaurantRepository.getRankingByReviewCount();
    }

    /**
     * 이름과 주소를 이용해 음식점을 조회하는 메서드
     *
     * @param placeName     음식점 이름
     * @param roadAddressName 음식점 도로명 주소
     * @return 조회된 음식점 엔티티
     */
    @Transactional(readOnly = true)
    public Restaurant findByPlaceNameAndRoadAddressName(String placeName, String roadAddressName) {
        return restaurantRepository.findByPlaceNameAndRoadAddressName(placeName, roadAddressName).orElse(null);
    }
}
