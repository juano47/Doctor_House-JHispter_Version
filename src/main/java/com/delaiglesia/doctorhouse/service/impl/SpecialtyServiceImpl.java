package com.delaiglesia.doctorhouse.service.impl;

import com.delaiglesia.doctorhouse.domain.Specialty;
import com.delaiglesia.doctorhouse.repository.SpecialtyRepository;
import com.delaiglesia.doctorhouse.service.SpecialtyService;
import com.delaiglesia.doctorhouse.service.dto.SpecialtyDTO;
import com.delaiglesia.doctorhouse.service.mapper.SpecialtyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Specialty}.
 */
@Service
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService {

    private final Logger log = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

    private final SpecialtyRepository specialtyRepository;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
    }

    @Override
    public SpecialtyDTO save(SpecialtyDTO specialtyDTO) {
        log.debug("Request to save Specialty : {}", specialtyDTO);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);
        specialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(specialty);
    }

    @Override
    public SpecialtyDTO update(SpecialtyDTO specialtyDTO) {
        log.debug("Request to update Specialty : {}", specialtyDTO);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);
        specialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(specialty);
    }

    @Override
    public Optional<SpecialtyDTO> partialUpdate(SpecialtyDTO specialtyDTO) {
        log.debug("Request to partially update Specialty : {}", specialtyDTO);

        return specialtyRepository
            .findById(specialtyDTO.getId())
            .map(existingSpecialty -> {
                specialtyMapper.partialUpdate(existingSpecialty, specialtyDTO);

                return existingSpecialty;
            })
            .map(specialtyRepository::save)
            .map(specialtyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialtyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Specialties");
        return specialtyRepository.findAll(pageable).map(specialtyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialtyDTO> findOne(Long id) {
        log.debug("Request to get Specialty : {}", id);
        return specialtyRepository.findById(id).map(specialtyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
    }
}
