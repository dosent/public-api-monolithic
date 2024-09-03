package ru.publicapi.site.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CountryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Country getCountrySample1() {
        return new Country()
            .id(1L)
            .code(1)
            .value("value1")
            .nameShort("nameShort1")
            .nameFull("nameFull1")
            .unrestrictedValue("unrestrictedValue1")
            .codeCountry("codeCountry1")
            .alfa2Country("alfa2Country1")
            .alfa3Country("alfa3Country1");
    }

    public static Country getCountrySample2() {
        return new Country()
            .id(2L)
            .code(2)
            .value("value2")
            .nameShort("nameShort2")
            .nameFull("nameFull2")
            .unrestrictedValue("unrestrictedValue2")
            .codeCountry("codeCountry2")
            .alfa2Country("alfa2Country2")
            .alfa3Country("alfa3Country2");
    }

    public static Country getCountryRandomSampleGenerator() {
        return new Country()
            .id(longCount.incrementAndGet())
            .code(intCount.incrementAndGet())
            .value(UUID.randomUUID().toString())
            .nameShort(UUID.randomUUID().toString())
            .nameFull(UUID.randomUUID().toString())
            .unrestrictedValue(UUID.randomUUID().toString())
            .codeCountry(UUID.randomUUID().toString())
            .alfa2Country(UUID.randomUUID().toString())
            .alfa3Country(UUID.randomUUID().toString());
    }
}
