package ru.publicapi.site.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.publicapi.site.domain.enumeration.Publication;

/**
 * A Directory.
 */
@Entity
@Table(name = "directory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Directory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "directory_name")
    private String directoryName;

    @Column(name = "url_api")
    private String urlAPI;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_public")
    private Publication isPublic;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Directory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirectoryName() {
        return this.directoryName;
    }

    public Directory directoryName(String directoryName) {
        this.setDirectoryName(directoryName);
        return this;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getUrlAPI() {
        return this.urlAPI;
    }

    public Directory urlAPI(String urlAPI) {
        this.setUrlAPI(urlAPI);
        return this;
    }

    public void setUrlAPI(String urlAPI) {
        this.urlAPI = urlAPI;
    }

    public String getDescription() {
        return this.description;
    }

    public Directory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Publication getIsPublic() {
        return this.isPublic;
    }

    public Directory isPublic(Publication isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Publication isPublic) {
        this.isPublic = isPublic;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Directory)) {
            return false;
        }
        return getId() != null && getId().equals(((Directory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Directory{" +
            "id=" + getId() +
            ", directoryName='" + getDirectoryName() + "'" +
            ", urlAPI='" + getUrlAPI() + "'" +
            ", description='" + getDescription() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            "}";
    }
}
