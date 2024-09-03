package ru.publicapi.site.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.publicapi.site.domain.ApiKey;
import ru.publicapi.site.repository.ApiKeyRepository;
import ru.publicapi.site.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.publicapi.site.domain.ApiKey}.
 */
@RestController
@RequestMapping("/api/api-keys")
@Transactional
public class ApiKeyResource {

    private static final Logger LOG = LoggerFactory.getLogger(ApiKeyResource.class);

    private static final String ENTITY_NAME = "apiKey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyResource(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * {@code POST  /api-keys} : Create a new apiKey.
     *
     * @param apiKey the apiKey to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiKey, or with status {@code 400 (Bad Request)} if the apiKey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApiKey> createApiKey(@RequestBody ApiKey apiKey) throws URISyntaxException {
        LOG.debug("REST request to save ApiKey : {}", apiKey);
        if (apiKey.getId() != null) {
            throw new BadRequestAlertException("A new apiKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        apiKey = apiKeyRepository.save(apiKey);
        return ResponseEntity.created(new URI("/api/api-keys/" + apiKey.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, apiKey.getId().toString()))
            .body(apiKey);
    }

    /**
     * {@code PUT  /api-keys/:id} : Updates an existing apiKey.
     *
     * @param id the id of the apiKey to save.
     * @param apiKey the apiKey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiKey,
     * or with status {@code 400 (Bad Request)} if the apiKey is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiKey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiKey> updateApiKey(@PathVariable(value = "id", required = false) final Long id, @RequestBody ApiKey apiKey)
        throws URISyntaxException {
        LOG.debug("REST request to update ApiKey : {}, {}", id, apiKey);
        if (apiKey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiKey.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        apiKey = apiKeyRepository.save(apiKey);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apiKey.getId().toString()))
            .body(apiKey);
    }

    /**
     * {@code PATCH  /api-keys/:id} : Partial updates given fields of an existing apiKey, field will ignore if it is null
     *
     * @param id the id of the apiKey to save.
     * @param apiKey the apiKey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiKey,
     * or with status {@code 400 (Bad Request)} if the apiKey is not valid,
     * or with status {@code 404 (Not Found)} if the apiKey is not found,
     * or with status {@code 500 (Internal Server Error)} if the apiKey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApiKey> partialUpdateApiKey(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApiKey apiKey
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ApiKey partially : {}, {}", id, apiKey);
        if (apiKey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apiKey.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apiKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApiKey> result = apiKeyRepository
            .findById(apiKey.getId())
            .map(existingApiKey -> {
                if (apiKey.getKeyHash() != null) {
                    existingApiKey.setKeyHash(apiKey.getKeyHash());
                }
                if (apiKey.getDescription() != null) {
                    existingApiKey.setDescription(apiKey.getDescription());
                }
                if (apiKey.getIsActual() != null) {
                    existingApiKey.setIsActual(apiKey.getIsActual());
                }

                return existingApiKey;
            })
            .map(apiKeyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, apiKey.getId().toString())
        );
    }

    /**
     * {@code GET  /api-keys} : get all the apiKeys.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiKeys in body.
     */
    @GetMapping("")
    public List<ApiKey> getAllApiKeys() {
        LOG.debug("REST request to get all ApiKeys");
        return apiKeyRepository.findAll();
    }

    /**
     * {@code GET  /api-keys/:id} : get the "id" apiKey.
     *
     * @param id the id of the apiKey to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiKey, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiKey> getApiKey(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ApiKey : {}", id);
        Optional<ApiKey> apiKey = apiKeyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apiKey);
    }

    /**
     * {@code DELETE  /api-keys/:id} : delete the "id" apiKey.
     *
     * @param id the id of the apiKey to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApiKey(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ApiKey : {}", id);
        apiKeyRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
