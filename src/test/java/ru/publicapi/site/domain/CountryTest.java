package ru.publicapi.site.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.publicapi.site.domain.CountryTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.publicapi.site.web.rest.TestUtil;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }
}
