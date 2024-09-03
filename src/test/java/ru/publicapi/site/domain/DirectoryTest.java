package ru.publicapi.site.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.publicapi.site.domain.DirectoryTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.publicapi.site.web.rest.TestUtil;

class DirectoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Directory.class);
        Directory directory1 = getDirectorySample1();
        Directory directory2 = new Directory();
        assertThat(directory1).isNotEqualTo(directory2);

        directory2.setId(directory1.getId());
        assertThat(directory1).isEqualTo(directory2);

        directory2 = getDirectorySample2();
        assertThat(directory1).isNotEqualTo(directory2);
    }
}
