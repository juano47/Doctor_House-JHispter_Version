package com.delaiglesia.doctorhouse.repository;

import com.delaiglesia.doctorhouse.domain.Doctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DoctorRepositoryWithBagRelationships {
    Optional<Doctor> fetchBagRelationships(Optional<Doctor> doctor);

    List<Doctor> fetchBagRelationships(List<Doctor> doctors);

    Page<Doctor> fetchBagRelationships(Page<Doctor> doctors);
}
