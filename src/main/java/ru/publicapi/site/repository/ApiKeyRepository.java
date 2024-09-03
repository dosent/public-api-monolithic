package ru.publicapi.site.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.publicapi.site.domain.ApiKey;

/**
 * Spring Data JPA repository for the ApiKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {}
