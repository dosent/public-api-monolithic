package ru.publicapi.site.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.publicapi.site.domain.ApiKeyTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.publicapi.site.web.rest.TestUtil;

class ApiKeyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApiKey.class);
        ApiKey apiKey1 = getApiKeySample1();
        ApiKey apiKey2 = new ApiKey();
        assertThat(apiKey1).isNotEqualTo(apiKey2);

        apiKey2.setId(apiKey1.getId());
        assertThat(apiKey1).isEqualTo(apiKey2);

        apiKey2 = getApiKeySample2();
        assertThat(apiKey1).isNotEqualTo(apiKey2);
    }
}
