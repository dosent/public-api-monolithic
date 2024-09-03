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
import ru.publicapi.site.domain.Directory;
import ru.publicapi.site.repository.DirectoryRepository;
import ru.publicapi.site.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.publicapi.site.domain.Directory}.
 */
@RestController
@RequestMapping("/api/directories")
@Transactional
public class DirectoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(DirectoryResource.class);

    private static final String ENTITY_NAME = "directory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DirectoryRepository directoryRepository;

    public DirectoryResource(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    /**
     * {@code POST  /directories} : Create a new directory.
     *
     * @param directory the directory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new directory, or with status {@code 400 (Bad Request)} if the directory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Directory> createDirectory(@RequestBody Directory directory) throws URISyntaxException {
        LOG.debug("REST request to save Directory : {}", directory);
        if (directory.getId() != null) {
            throw new BadRequestAlertException("A new directory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        directory = directoryRepository.save(directory);
        return ResponseEntity.created(new URI("/api/directories/" + directory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, directory.getId().toString()))
            .body(directory);
    }

    /**
     * {@code PUT  /directories/:id} : Updates an existing directory.
     *
     * @param id the id of the directory to save.
     * @param directory the directory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directory,
     * or with status {@code 400 (Bad Request)} if the directory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the directory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Directory> updateDirectory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Directory directory
    ) throws URISyntaxException {
        LOG.debug("REST request to update Directory : {}, {}", id, directory);
        if (directory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!directoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        directory = directoryRepository.save(directory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, directory.getId().toString()))
            .body(directory);
    }

    /**
     * {@code PATCH  /directories/:id} : Partial updates given fields of an existing directory, field will ignore if it is null
     *
     * @param id the id of the directory to save.
     * @param directory the directory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directory,
     * or with status {@code 400 (Bad Request)} if the directory is not valid,
     * or with status {@code 404 (Not Found)} if the directory is not found,
     * or with status {@code 500 (Internal Server Error)} if the directory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Directory> partialUpdateDirectory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Directory directory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Directory partially : {}, {}", id, directory);
        if (directory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!directoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Directory> result = directoryRepository
            .findById(directory.getId())
            .map(existingDirectory -> {
                if (directory.getDirectoryName() != null) {
                    existingDirectory.setDirectoryName(directory.getDirectoryName());
                }
                if (directory.getUrlAPI() != null) {
                    existingDirectory.setUrlAPI(directory.getUrlAPI());
                }
                if (directory.getDescription() != null) {
                    existingDirectory.setDescription(directory.getDescription());
                }
                if (directory.getIsPublic() != null) {
                    existingDirectory.setIsPublic(directory.getIsPublic());
                }

                return existingDirectory;
            })
            .map(directoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, directory.getId().toString())
        );
    }

    /**
     * {@code GET  /directories} : get all the directories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of directories in body.
     */
    @GetMapping("")
    public List<Directory> getAllDirectories() {
        LOG.debug("REST request to get all Directories");
        return directoryRepository.findAll();
    }

    /**
     * {@code GET  /directories/:id} : get the "id" directory.
     *
     * @param id the id of the directory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the directory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Directory> getDirectory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Directory : {}", id);
        Optional<Directory> directory = directoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(directory);
    }

    /**
     * {@code DELETE  /directories/:id} : delete the "id" directory.
     *
     * @param id the id of the directory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirectory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Directory : {}", id);
        directoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
