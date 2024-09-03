package ru.publicapi.site.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.publicapi.site.domain.ApiKeyAsserts.*;
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
import ru.publicapi.site.domain.ApiKey;
import ru.publicapi.site.domain.enumeration.Actual;
import ru.publicapi.site.repository.ApiKeyRepository;

/**
 * Integration tests for the {@link ApiKeyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApiKeyResourceIT {

    private static final String DEFAULT_KEY_HASH = "AAAAAAAAAA";
    private static final String UPDATED_KEY_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Actual DEFAULT_IS_ACTUAL = Actual.YES;
    private static final Actual UPDATED_IS_ACTUAL = Actual.NO;

    private static final String ENTITY_API_URL = "/api/api-keys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApiKeyMockMvc;

    private ApiKey apiKey;

    private ApiKey insertedApiKey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiKey createEntity() {
        return new ApiKey().keyHash(DEFAULT_KEY_HASH).description(DEFAULT_DESCRIPTION).isActual(DEFAULT_IS_ACTUAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiKey createUpdatedEntity() {
        return new ApiKey().keyHash(UPDATED_KEY_HASH).description(UPDATED_DESCRIPTION).isActual(UPDATED_IS_ACTUAL);
    }

    @BeforeEach
    public void initTest() {
        apiKey = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedApiKey != null) {
            apiKeyRepository.delete(insertedApiKey);
            insertedApiKey = null;
        }
    }

    @Test
    @Transactional
    void createApiKey() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ApiKey
        var returnedApiKey = om.readValue(
            restApiKeyMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiKey)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApiKey.class
        );

        // Validate the ApiKey in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertApiKeyUpdatableFieldsEquals(returnedApiKey, getPersistedApiKey(returnedApiKey));

        insertedApiKey = returnedApiKey;
    }

    @Test
    @Transactional
    void createApiKeyWithExistingId() throws Exception {
        // Create the ApiKey with an existing ID
        apiKey.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiKeyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiKey)))
            .andExpect(status().isBadRequest());

        // Validate the ApiKey in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApiKeys() throws Exception {
        // Initialize the database
        insertedApiKey = apiKeyRepository.saveAndFlush(apiKey);

        // Get all the apiKeyList
        restApiKeyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].keyHash").value(hasItem(DEFAULT_KEY_HASH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActual").value(hasItem(DEFAULT_IS_ACTUAL.toString())));
    }

    @Test
    @Transactional
    void getApiKey() throws Exception {
        // Initialize the database
        insertedApiKey = apiKeyRepository.saveAndFlush(apiKey);

        // Get the apiKey
        restApiKeyMockMvc
            .perform(get(ENTITY_API_URL_ID, apiKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apiKey.getId().intValue()))
            .andExpect(jsonPath("$.keyHash").value(DEFAULT_KEY_HASH))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActual").value(DEFAULT_IS_ACTUAL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingApiKey() throws Exception {
        // Get the apiKey
        restApiKeyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApiKey() throws Exception {
        // Initialize the database
        insertedApiKey = apiKeyRepository.saveAndFlush(apiKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiKey
        ApiKey updatedApiKey = apiKeyRepository.findById(apiKey.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApiKey are not directly saved in db
        em.detach(updatedApiKey);
        updatedApiKey.keyHash(UPDATED_KEY_HASH).description(UPDATED_DESCRIPTION).isActual(UPDATED_IS_ACTUAL);

        restApiKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApiKey.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedApiKey))
            )
            .andExpect(status().isOk());

        // Validate the ApiKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApiKeyToMatchAllProperties(updatedApiKey);
    }

    @Test
    @Transactional
    void putNonExistingApiKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiKey.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apiKey.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApiKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiKey.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(apiKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApiKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiKey.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiKeyMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(apiKey)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApiKeyWithPatch() throws Exception {
        // Initialize the database
        insertedApiKey = apiKeyRepository.saveAndFlush(apiKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiKey using partial update
        ApiKey partialUpdatedApiKey = new ApiKey();
        partialUpdatedApiKey.setId(apiKey.getId());

        partialUpdatedApiKey.keyHash(UPDATED_KEY_HASH).isActual(UPDATED_IS_ACTUAL);

        restApiKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiKey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApiKey))
            )
            .andExpect(status().isOk());

        // Validate the ApiKey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApiKeyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedApiKey, apiKey), getPersistedApiKey(apiKey));
    }

    @Test
    @Transactional
    void fullUpdateApiKeyWithPatch() throws Exception {
        // Initialize the database
        insertedApiKey = apiKeyRepository.saveAndFlush(apiKey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the apiKey using partial update
        ApiKey partialUpdatedApiKey = new ApiKey();
        partialUpdatedApiKey.setId(apiKey.getId());

        partialUpdatedApiKey.keyHash(UPDATED_KEY_HASH).description(UPDATED_DESCRIPTION).isActual(UPDATED_IS_ACTUAL);

        restApiKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApiKey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApiKey))
            )
            .andExpect(status().isOk());

        // Validate the ApiKey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApiKeyUpdatableFieldsEquals(partialUpdatedApiKey, getPersistedApiKey(partialUpdatedApiKey));
    }

    @Test
    @Transactional
    void patchNonExistingApiKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiKey.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apiKey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(apiKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApiKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiKey.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(apiKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApiKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApiKey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        apiKey.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApiKeyMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(apiKey)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApiKey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApiKey() throws Exception {
        // Initialize the database
        insertedApiKey = apiKeyRepository.saveAndFlush(apiKey);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the apiKey
        restApiKeyMockMvc
            .perform(delete(ENTITY_API_URL_ID, apiKey.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return apiKeyRepository.count();
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

    protected ApiKey getPersistedApiKey(ApiKey apiKey) {
        return apiKeyRepository.findById(apiKey.getId()).orElseThrow();
    }

    protected void assertPersistedApiKeyToMatchAllProperties(ApiKey expectedApiKey) {
        assertApiKeyAllPropertiesEquals(expectedApiKey, getPersistedApiKey(expectedApiKey));
    }

    protected void assertPersistedApiKeyToMatchUpdatableProperties(ApiKey expectedApiKey) {
        assertApiKeyAllUpdatablePropertiesEquals(expectedApiKey, getPersistedApiKey(expectedApiKey));
    }
}
