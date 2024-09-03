package ru.publicapi.site.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApiKeyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ApiKey getApiKeySample1() {
        return new ApiKey().id(1L).keyHash("keyHash1").description("description1");
    }

    public static ApiKey getApiKeySample2() {
        return new ApiKey().id(2L).keyHash("keyHash2").description("description2");
    }

    public static ApiKey getApiKeyRandomSampleGenerator() {
        return new ApiKey().id(longCount.incrementAndGet()).keyHash(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
