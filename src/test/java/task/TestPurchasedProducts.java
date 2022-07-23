package task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestPurchasedProducts {

    @Test
    @DisplayName("should empty list when product ids list is empty")
    void test1() {
        List<String> ids = List.of();
        Map<String, Map<String, Object>> details = Map.of(
                "RQEY", Map.of("version", 1, "edition", "X")
        );

        var result = PurchasedProducts.evaluate(ids, details);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return list of product descriptions when all details are present")
    void test2() {
        List<String> ids = List.of("CVCD", "SDFD", "DDDF", "SDFD");
        Map<String, Map<String, Object>> details = Map.of(
                "CVCD", Map.of("version", 1, "edition", "X"),
                "SDFD", Map.of("version", 2, "edition", "Z"),
                "DDDF", Map.of("version", 1)
        );

        var result = PurchasedProducts.evaluate(ids, details);

        assertThat(result)
                .containsExactly(
                        Map.of("version", 1, "edition", "X", "quantity", 1L),
                        Map.of("version", 1, "quantity", 1L),
                        Map.of("version", 2, "edition", "Z", "quantity", 2L)
                );
    }

    @Test
    @DisplayName("should throw illegal argument when product details missed")
    void test3() {
        List<String> ids = List.of("A", "B", "C");
        Map<String, Map<String, Object>> details = Map.of(
                "B", Map.of("version", 1, "edition", "X"),
                "C", Map.of("version", 2, "edition", "Z"),
                "D", Map.of("version", 1)
        );

        assertThatThrownBy(() -> PurchasedProducts.evaluate(ids, details))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missed details for product: A");
    }

    @Test
    @DisplayName("should throw illegal argument when product details is empty map")
    void test4() {
        List<String> ids = List.of("YEUW", "UWWE", "YUUIRES", "ERTYE");
        Map<String, Map<String, Object>> details = Map.of(
                "YEUW", Map.of("version", 5, "edition", "W"),
                "UWWE", Map.of("version", 6, "edition", "M"),
                "ERTYE", Map.of("version", 45),
                "YUUIRES", Map.of()
        );


        assertThatThrownBy(() -> PurchasedProducts.evaluate(ids, details))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missed details for product: YUUIRES");
    }

    @Test
    @DisplayName("should throw NPE when product ids is null")
    void test5() {
        List<String> ids = null;
        Map<String, Map<String, Object>> details = Map.of(
        );


        assertThatThrownBy(() -> PurchasedProducts.evaluate(ids, details))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("productIds");
    }


    @Test
    @DisplayName("should throw NPE when details is null")
    void test6() {
        List<String> ids = List.of();
        Map<String, Map<String, Object>> details = null;


        assertThatThrownBy(() -> PurchasedProducts.evaluate(ids, details))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("productDetails");
    }
}