package ru.publicapi.site.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.publicapi.site.domain.Directory;

/**
 * Spring Data JPA repository for the Directory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {}
