package task;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry.comparingByKey;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class PurchasedProducts {

    public static List<Map<String, Object>> evaluate(
            List<String> productIds,
            Map<String, Map<String, Object>> productDetails
    ) {
        requireNonNull(productIds, "productIds");
        requireNonNull(productDetails, "productDetails");

        final var productsToQuantities = productIds.stream().collect(groupingBy(identity(), counting()));

        return productsToQuantities.entrySet().stream()
                .sorted(comparingByKey())
                .map(e -> attachEntry(require(e.getKey(), productDetails), "quantity", e.getValue()))
                .toList();
    }

    private static <K, V> V require(K productId, Map<K, V> details) {
        V value = details.get(productId);
        if (value == null || (value instanceof Map m && m.isEmpty())) {
            throw new IllegalArgumentException("Missed details for product: " + productId);
        }
        return value;
    }

    private static <K, V> Map<K, V> attachEntry(Map<K, V> original, K key, V value) {
        var patched = new HashMap<>(original);
        patched.put(key, value);
        return Collections.unmodifiableMap(patched);
    }
}
