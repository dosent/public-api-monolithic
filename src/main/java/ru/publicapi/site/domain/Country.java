package ru.publicapi.site.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.publicapi.site.domain.enumeration.Actual;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private Integer code;

    @Column(name = "value")
    private String value;

    @Column(name = "name_short")
    private String nameShort;

    @Column(name = "name_full")
    private String nameFull;

    @Column(name = "unrestricted_value")
    private String unrestrictedValue;

    @Column(name = "code_country")
    private String codeCountry;

    @Column(name = "alfa_2_country")
    private String alfa2Country;

    @Column(name = "alfa_3_country")
    private String alfa3Country;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_actual")
    private Actual isActual;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return this.code;
    }

    public Country code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return this.value;
    }

    public Country value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNameShort() {
        return this.nameShort;
    }

    public Country nameShort(String nameShort) {
        this.setNameShort(nameShort);
        return this;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNameFull() {
        return this.nameFull;
    }

    public Country nameFull(String nameFull) {
        this.setNameFull(nameFull);
        return this;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

    public String getUnrestrictedValue() {
        return this.unrestrictedValue;
    }

    public Country unrestrictedValue(String unrestrictedValue) {
        this.setUnrestrictedValue(unrestrictedValue);
        return this;
    }

    public void setUnrestrictedValue(String unrestrictedValue) {
        this.unrestrictedValue = unrestrictedValue;
    }

    public String getCodeCountry() {
        return this.codeCountry;
    }

    public Country codeCountry(String codeCountry) {
        this.setCodeCountry(codeCountry);
        return this;
    }

    public void setCodeCountry(String codeCountry) {
        this.codeCountry = codeCountry;
    }

    public String getAlfa2Country() {
        return this.alfa2Country;
    }

    public Country alfa2Country(String alfa2Country) {
        this.setAlfa2Country(alfa2Country);
        return this;
    }

    public void setAlfa2Country(String alfa2Country) {
        this.alfa2Country = alfa2Country;
    }

    public String getAlfa3Country() {
        return this.alfa3Country;
    }

    public Country alfa3Country(String alfa3Country) {
        this.setAlfa3Country(alfa3Country);
        return this;
    }

    public void setAlfa3Country(String alfa3Country) {
        this.alfa3Country = alfa3Country;
    }

    public Actual getIsActual() {
        return this.isActual;
    }

    public Country isActual(Actual isActual) {
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
        if (!(o instanceof Country)) {
            return false;
        }
        return getId() != null && getId().equals(((Country) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", value='" + getValue() + "'" +
            ", nameShort='" + getNameShort() + "'" +
            ", nameFull='" + getNameFull() + "'" +
            ", unrestrictedValue='" + getUnrestrictedValue() + "'" +
            ", codeCountry='" + getCodeCountry() + "'" +
            ", alfa2Country='" + getAlfa2Country() + "'" +
            ", alfa3Country='" + getAlfa3Country() + "'" +
            ", isActual='" + getIsActual() + "'" +
            "}";
    }
}
