package ru.publicapi.site.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryAllPropertiesEquals(Country expected, Country actual) {
        assertCountryAutoGeneratedPropertiesEquals(expected, actual);
        assertCountryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryAllUpdatablePropertiesEquals(Country expected, Country actual) {
        assertCountryUpdatableFieldsEquals(expected, actual);
        assertCountryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryAutoGeneratedPropertiesEquals(Country expected, Country actual) {
        assertThat(expected)
            .as("Verify Country auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryUpdatableFieldsEquals(Country expected, Country actual) {
        assertThat(expected)
            .as("Verify Country relevant properties")
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()))
            .satisfies(e -> assertThat(e.getNameShort()).as("check nameShort").isEqualTo(actual.getNameShort()))
            .satisfies(e -> assertThat(e.getNameFull()).as("check nameFull").isEqualTo(actual.getNameFull()))
            .satisfies(e -> assertThat(e.getNameShortEng()).as("check nameShortEng").isEqualTo(actual.getNameShortEng()))
            .satisfies(e -> assertThat(e.getNameFullEng()).as("check nameFullEng").isEqualTo(actual.getNameFullEng()))
            .satisfies(e -> assertThat(e.getUnrestrictedValue()).as("check unrestrictedValue").isEqualTo(actual.getUnrestrictedValue()))
            .satisfies(e -> assertThat(e.getCodeCountry()).as("check codeCountry").isEqualTo(actual.getCodeCountry()))
            .satisfies(e -> assertThat(e.getAlfa2Country()).as("check alfa2Country").isEqualTo(actual.getAlfa2Country()))
            .satisfies(e -> assertThat(e.getAlfa3Country()).as("check alfa3Country").isEqualTo(actual.getAlfa3Country()))
            .satisfies(e -> assertThat(e.getIsActual()).as("check isActual").isEqualTo(actual.getIsActual()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryUpdatableRelationshipsEquals(Country expected, Country actual) {
        // empty method
    }
}
