package ru.publicapi.site.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.publicapi.site.domain.CountryAsserts.*;
import static ru.publicapi.site.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.publicapi.site.IntegrationTest;
import ru.publicapi.site.domain.Country;
import ru.publicapi.site.domain.enumeration.Actual;
import ru.publicapi.site.repository.CountryRepository;

/**
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountryResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SHORT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SHORT = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_FULL = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FULL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SHORT_ENG = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SHORT_ENG = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_FULL_ENG = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FULL_ENG = "BBBBBBBBBB";

    private static final String DEFAULT_UNRESTRICTED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_UNRESTRICTED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_CODE_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ALFA_2_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_ALFA_2_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ALFA_3_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_ALFA_3_COUNTRY = "BBBBBBBBBB";

    private static final Actual DEFAULT_IS_ACTUAL = Actual.YES;
    private static final Actual UPDATED_IS_ACTUAL = Actual.NO;

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryMockMvc;

    private Country country;

    private Country insertedCountry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity() {
        return new Country()
            .code(DEFAULT_CODE)
            .value(DEFAULT_VALUE)
            .nameShort(DEFAULT_NAME_SHORT)
            .nameFull(DEFAULT_NAME_FULL)
            .nameShortEng(DEFAULT_NAME_SHORT_ENG)
            .nameFullEng(DEFAULT_NAME_FULL_ENG)
            .unrestrictedValue(DEFAULT_UNRESTRICTED_VALUE)
            .codeCountry(DEFAULT_CODE_COUNTRY)
            .alfa2Country(DEFAULT_ALFA_2_COUNTRY)
            .alfa3Country(DEFAULT_ALFA_3_COUNTRY)
            .isActual(DEFAULT_IS_ACTUAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity() {
        return new Country()
            .code(UPDATED_CODE)
            .value(UPDATED_VALUE)
            .nameShort(UPDATED_NAME_SHORT)
            .nameFull(UPDATED_NAME_FULL)
            .nameShortEng(UPDATED_NAME_SHORT_ENG)
            .nameFullEng(UPDATED_NAME_FULL_ENG)
            .unrestrictedValue(UPDATED_UNRESTRICTED_VALUE)
            .codeCountry(UPDATED_CODE_COUNTRY)
            .alfa2Country(UPDATED_ALFA_2_COUNTRY)
            .alfa3Country(UPDATED_ALFA_3_COUNTRY)
            .isActual(UPDATED_IS_ACTUAL);
    }

    @BeforeEach
    public void initTest() {
        country = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCountry != null) {
            countryRepository.delete(insertedCountry);
            insertedCountry = null;
        }
    }

    @Test
    @Transactional
    void createCountry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Country
        var returnedCountry = om.readValue(
            restCountryMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(country)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Country.class
        );

        // Validate the Country in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCountryUpdatableFieldsEquals(returnedCountry, getPersistedCountry(returnedCountry));

        insertedCountry = returnedCountry;
    }

    @Test
    @Transactional
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(country)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].nameShort").value(hasItem(DEFAULT_NAME_SHORT)))
            .andExpect(jsonPath("$.[*].nameFull").value(hasItem(DEFAULT_NAME_FULL)))
            .andExpect(jsonPath("$.[*].nameShortEng").value(hasItem(DEFAULT_NAME_SHORT_ENG)))
            .andExpect(jsonPath("$.[*].nameFullEng").value(hasItem(DEFAULT_NAME_FULL_ENG)))
            .andExpect(jsonPath("$.[*].unrestrictedValue").value(hasItem(DEFAULT_UNRESTRICTED_VALUE)))
            .andExpect(jsonPath("$.[*].codeCountry").value(hasItem(DEFAULT_CODE_COUNTRY)))
            .andExpect(jsonPath("$.[*].alfa2Country").value(hasItem(DEFAULT_ALFA_2_COUNTRY)))
            .andExpect(jsonPath("$.[*].alfa3Country").value(hasItem(DEFAULT_ALFA_3_COUNTRY)))
            .andExpect(jsonPath("$.[*].isActual").value(hasItem(DEFAULT_IS_ACTUAL.toString())));
    }

    @Test
    @Transactional
    void getCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.nameShort").value(DEFAULT_NAME_SHORT))
            .andExpect(jsonPath("$.nameFull").value(DEFAULT_NAME_FULL))
            .andExpect(jsonPath("$.nameShortEng").value(DEFAULT_NAME_SHORT_ENG))
            .andExpect(jsonPath("$.nameFullEng").value(DEFAULT_NAME_FULL_ENG))
            .andExpect(jsonPath("$.unrestrictedValue").value(DEFAULT_UNRESTRICTED_VALUE))
            .andExpect(jsonPath("$.codeCountry").value(DEFAULT_CODE_COUNTRY))
            .andExpect(jsonPath("$.alfa2Country").value(DEFAULT_ALFA_2_COUNTRY))
            .andExpect(jsonPath("$.alfa3Country").value(DEFAULT_ALFA_3_COUNTRY))
            .andExpect(jsonPath("$.isActual").value(DEFAULT_IS_ACTUAL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry
            .code(UPDATED_CODE)
            .value(UPDATED_VALUE)
            .nameShort(UPDATED_NAME_SHORT)
            .nameFull(UPDATED_NAME_FULL)
            .nameShortEng(UPDATED_NAME_SHORT_ENG)
            .nameFullEng(UPDATED_NAME_FULL_ENG)
            .unrestrictedValue(UPDATED_UNRESTRICTED_VALUE)
            .codeCountry(UPDATED_CODE_COUNTRY)
            .alfa2Country(UPDATED_ALFA_2_COUNTRY)
            .alfa3Country(UPDATED_ALFA_3_COUNTRY)
            .isActual(UPDATED_IS_ACTUAL);

        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCountry.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCountryToMatchAllProperties(updatedCountry);
    }

    @Test
    @Transactional
    void putNonExistingCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, country.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(country))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(country))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(country)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .nameFullEng(UPDATED_NAME_FULL_ENG)
            .unrestrictedValue(UPDATED_UNRESTRICTED_VALUE)
            .alfa3Country(UPDATED_ALFA_3_COUNTRY)
            .isActual(UPDATED_IS_ACTUAL);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCountry, country), getPersistedCountry(country));
    }

    @Test
    @Transactional
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .code(UPDATED_CODE)
            .value(UPDATED_VALUE)
            .nameShort(UPDATED_NAME_SHORT)
            .nameFull(UPDATED_NAME_FULL)
            .nameShortEng(UPDATED_NAME_SHORT_ENG)
            .nameFullEng(UPDATED_NAME_FULL_ENG)
            .unrestrictedValue(UPDATED_UNRESTRICTED_VALUE)
            .codeCountry(UPDATED_CODE_COUNTRY)
            .alfa2Country(UPDATED_ALFA_2_COUNTRY)
            .alfa3Country(UPDATED_ALFA_3_COUNTRY)
            .isActual(UPDATED_IS_ACTUAL);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryUpdatableFieldsEquals(partialUpdatedCountry, getPersistedCountry(partialUpdatedCountry));
    }

    @Test
    @Transactional
    void patchNonExistingCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, country.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(country))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(country))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        country.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(country)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountry() throws Exception {
        // Initialize the database
        insertedCountry = countryRepository.saveAndFlush(country);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the country
        restCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, country.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return countryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Country getPersistedCountry(Country country) {
        return countryRepository.findById(country.getId()).orElseThrow();
    }

    protected void assertPersistedCountryToMatchAllProperties(Country expectedCountry) {
        assertCountryAllPropertiesEquals(expectedCountry, getPersistedCountry(expectedCountry));
    }

    protected void assertPersistedCountryToMatchUpdatableProperties(Country expectedCountry) {
        assertCountryAllUpdatablePropertiesEquals(expectedCountry, getPersistedCountry(expectedCountry));
    }
}
