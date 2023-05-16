package com.delaiglesia.doctorhouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Specialty.
 */
@Entity
@Table(name = "specialty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Specialty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "especialties")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "places", "especialties" }, allowSetters = true)
    private Set<Doctor> doctors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Specialty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Specialty name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Specialty description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Doctor> getDoctors() {
        return this.doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        if (this.doctors != null) {
            this.doctors.forEach(i -> i.removeEspecialty(this));
        }
        if (doctors != null) {
            doctors.forEach(i -> i.addEspecialty(this));
        }
        this.doctors = doctors;
    }

    public Specialty doctors(Set<Doctor> doctors) {
        this.setDoctors(doctors);
        return this;
    }

    public Specialty addDoctor(Doctor doctor) {
        this.doctors.add(doctor);
        doctor.getEspecialties().add(this);
        return this;
    }

    public Specialty removeDoctor(Doctor doctor) {
        this.doctors.remove(doctor);
        doctor.getEspecialties().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialty)) {
            return false;
        }
        return id != null && id.equals(((Specialty) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
