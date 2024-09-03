package ru.publicapi.site.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DirectoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Directory getDirectorySample1() {
        return new Directory().id(1L).directoryName("directoryName1").urlAPI("urlAPI1").description("description1");
    }

    public static Directory getDirectorySample2() {
        return new Directory().id(2L).directoryName("directoryName2").urlAPI("urlAPI2").description("description2");
    }

    public static Directory getDirectoryRandomSampleGenerator() {
        return new Directory()
            .id(longCount.incrementAndGet())
            .directoryName(UUID.randomUUID().toString())
            .urlAPI(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
