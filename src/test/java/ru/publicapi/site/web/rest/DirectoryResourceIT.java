package ru.publicapi.site.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.publicapi.site.domain.DirectoryAsserts.*;
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
import ru.publicapi.site.domain.Directory;
import ru.publicapi.site.domain.enumeration.Publication;
import ru.publicapi.site.repository.DirectoryRepository;

/**
 * Integration tests for the {@link DirectoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DirectoryResourceIT {

    private static final String DEFAULT_DIRECTORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL_API = "AAAAAAAAAA";
    private static final String UPDATED_URL_API = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Publication DEFAULT_IS_PUBLIC = Publication.PROD;
    private static final Publication UPDATED_IS_PUBLIC = Publication.DEBUG;

    private static final String ENTITY_API_URL = "/api/directories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirectoryMockMvc;

    private Directory directory;

    private Directory insertedDirectory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directory createEntity() {
        return new Directory()
            .directoryName(DEFAULT_DIRECTORY_NAME)
            .urlAPI(DEFAULT_URL_API)
            .description(DEFAULT_DESCRIPTION)
            .isPublic(DEFAULT_IS_PUBLIC);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directory createUpdatedEntity() {
        return new Directory()
            .directoryName(UPDATED_DIRECTORY_NAME)
            .urlAPI(UPDATED_URL_API)
            .description(UPDATED_DESCRIPTION)
            .isPublic(UPDATED_IS_PUBLIC);
    }

    @BeforeEach
    public void initTest() {
        directory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDirectory != null) {
            directoryRepository.delete(insertedDirectory);
            insertedDirectory = null;
        }
    }

    @Test
    @Transactional
    void createDirectory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Directory
        var returnedDirectory = om.readValue(
            restDirectoryMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Directory.class
        );

        // Validate the Directory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDirectoryUpdatableFieldsEquals(returnedDirectory, getPersistedDirectory(returnedDirectory));

        insertedDirectory = returnedDirectory;
    }

    @Test
    @Transactional
    void createDirectoryWithExistingId() throws Exception {
        // Create the Directory with an existing ID
        directory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectoryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directory)))
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDirectories() throws Exception {
        // Initialize the database
        insertedDirectory = directoryRepository.saveAndFlush(directory);

        // Get all the directoryList
        restDirectoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directory.getId().intValue())))
            .andExpect(jsonPath("$.[*].directoryName").value(hasItem(DEFAULT_DIRECTORY_NAME)))
            .andExpect(jsonPath("$.[*].urlAPI").value(hasItem(DEFAULT_URL_API)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.toString())));
    }

    @Test
    @Transactional
    void getDirectory() throws Exception {
        // Initialize the database
        insertedDirectory = directoryRepository.saveAndFlush(directory);

        // Get the directory
        restDirectoryMockMvc
            .perform(get(ENTITY_API_URL_ID, directory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(directory.getId().intValue()))
            .andExpect(jsonPath("$.directoryName").value(DEFAULT_DIRECTORY_NAME))
            .andExpect(jsonPath("$.urlAPI").value(DEFAULT_URL_API))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDirectory() throws Exception {
        // Get the directory
        restDirectoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDirectory() throws Exception {
        // Initialize the database
        insertedDirectory = directoryRepository.saveAndFlush(directory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directory
        Directory updatedDirectory = directoryRepository.findById(directory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDirectory are not directly saved in db
        em.detach(updatedDirectory);
        updatedDirectory
            .directoryName(UPDATED_DIRECTORY_NAME)
            .urlAPI(UPDATED_URL_API)
            .description(UPDATED_DESCRIPTION)
            .isPublic(UPDATED_IS_PUBLIC);

        restDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDirectory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDirectory))
            )
            .andExpect(status().isOk());

        // Validate the Directory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDirectoryToMatchAllProperties(updatedDirectory);
    }

    @Test
    @Transactional
    void putNonExistingDirectory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(directory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirectory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(directory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirectory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(directory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDirectoryWithPatch() throws Exception {
        // Initialize the database
        insertedDirectory = directoryRepository.saveAndFlush(directory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directory using partial update
        Directory partialUpdatedDirectory = new Directory();
        partialUpdatedDirectory.setId(directory.getId());

        partialUpdatedDirectory.directoryName(UPDATED_DIRECTORY_NAME).description(UPDATED_DESCRIPTION).isPublic(UPDATED_IS_PUBLIC);

        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDirectory))
            )
            .andExpect(status().isOk());

        // Validate the Directory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDirectoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDirectory, directory),
            getPersistedDirectory(directory)
        );
    }

    @Test
    @Transactional
    void fullUpdateDirectoryWithPatch() throws Exception {
        // Initialize the database
        insertedDirectory = directoryRepository.saveAndFlush(directory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the directory using partial update
        Directory partialUpdatedDirectory = new Directory();
        partialUpdatedDirectory.setId(directory.getId());

        partialUpdatedDirectory
            .directoryName(UPDATED_DIRECTORY_NAME)
            .urlAPI(UPDATED_URL_API)
            .description(UPDATED_DESCRIPTION)
            .isPublic(UPDATED_IS_PUBLIC);

        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDirectory))
            )
            .andExpect(status().isOk());

        // Validate the Directory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDirectoryUpdatableFieldsEquals(partialUpdatedDirectory, getPersistedDirectory(partialUpdatedDirectory));
    }

    @Test
    @Transactional
    void patchNonExistingDirectory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, directory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(directory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirectory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(directory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirectory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        directory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(directory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDirectory() throws Exception {
        // Initialize the database
        insertedDirectory = directoryRepository.saveAndFlush(directory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the directory
        restDirectoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, directory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return directoryRepository.count();
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

    protected Directory getPersistedDirectory(Directory directory) {
        return directoryRepository.findById(directory.getId()).orElseThrow();
    }

    protected void assertPersistedDirectoryToMatchAllProperties(Directory expectedDirectory) {
        assertDirectoryAllPropertiesEquals(expectedDirectory, getPersistedDirectory(expectedDirectory));
    }

    protected void assertPersistedDirectoryToMatchUpdatableProperties(Directory expectedDirectory) {
        assertDirectoryAllUpdatablePropertiesEquals(expectedDirectory, getPersistedDirectory(expectedDirectory));
    }
}
