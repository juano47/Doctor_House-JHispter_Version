package com.delaiglesia.doctorhouse.repository;

import com.delaiglesia.doctorhouse.domain.Doctor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DoctorRepositoryWithBagRelationshipsImpl implements DoctorRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Doctor> fetchBagRelationships(Optional<Doctor> doctor) {
        return doctor.map(this::fetchPlaces).map(this::fetchEspecialties);
    }

    @Override
    public Page<Doctor> fetchBagRelationships(Page<Doctor> doctors) {
        return new PageImpl<>(fetchBagRelationships(doctors.getContent()), doctors.getPageable(), doctors.getTotalElements());
    }

    @Override
    public List<Doctor> fetchBagRelationships(List<Doctor> doctors) {
        return Optional.of(doctors).map(this::fetchPlaces).map(this::fetchEspecialties).orElse(Collections.emptyList());
    }

    Doctor fetchPlaces(Doctor result) {
        return entityManager
            .createQuery("select doctor from Doctor doctor left join fetch doctor.places where doctor is :doctor", Doctor.class)
            .setParameter("doctor", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Doctor> fetchPlaces(List<Doctor> doctors) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, doctors.size()).forEach(index -> order.put(doctors.get(index).getId(), index));
        List<Doctor> result = entityManager
            .createQuery("select distinct doctor from Doctor doctor left join fetch doctor.places where doctor in :doctors", Doctor.class)
            .setParameter("doctors", doctors)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Doctor fetchEspecialties(Doctor result) {
        return entityManager
            .createQuery("select doctor from Doctor doctor left join fetch doctor.especialties where doctor is :doctor", Doctor.class)
            .setParameter("doctor", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Doctor> fetchEspecialties(List<Doctor> doctors) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, doctors.size()).forEach(index -> order.put(doctors.get(index).getId(), index));
        List<Doctor> result = entityManager
            .createQuery(
                "select distinct doctor from Doctor doctor left join fetch doctor.especialties where doctor in :doctors",
                Doctor.class
            )
            .setParameter("doctors", doctors)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
