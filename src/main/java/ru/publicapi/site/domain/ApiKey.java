package ru.publicapi.site.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.publicapi.site.domain.enumeration.Actual;

/**
 * A ApiKey.
 */
@Entity
@Table(name = "api_key")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApiKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "key_hash")
    private String keyHash;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_actual")
    private Actual isActual;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApiKey id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyHash() {
        return this.keyHash;
    }

    public ApiKey keyHash(String keyHash) {
        this.setKeyHash(keyHash);
        return this;
    }

    public void setKeyHash(String keyHash) {
        this.keyHash = keyHash;
    }

    public String getDescription() {
        return this.description;
    }

    public ApiKey description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Actual getIsActual() {
        return this.isActual;
    }

    public ApiKey isActual(Actual isActual) {
        this.setIsActual(isActual);
        return this;
    }

    public void setIsActual(Actual isActual) {
        this.isActual = isActual;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiKey)) {
            return false;
        }
        return getId() != null && getId().equals(((ApiKey) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiKey{" +
            "id=" + getId() +
            ", keyHash='" + getKeyHash() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActual='" + getIsActual() + "'" +
            "}";
    }
}
