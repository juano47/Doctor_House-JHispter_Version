package com.delaiglesia.doctorhouse.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.delaiglesia.doctorhouse.domain.Doctor;
import com.delaiglesia.doctorhouse.repository.DoctorRepository;
import com.delaiglesia.doctorhouse.service.dto.DoctorDTO;
import com.delaiglesia.doctorhouse.service.mapper.DoctorMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        // Mock data
        DoctorDTO doctorDTO = new DoctorDTO();
        Doctor doctor = new Doctor();

        // Mock behavior
        when(doctorMapper.toEntity(doctorDTO)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDTO);

        // Invoke method
        DoctorDTO result = doctorService.save(doctorDTO);

        // Verify
        assertNotNull(result);
        assertSame(doctorDTO, result);

        verify(doctorMapper).toEntity(doctorDTO);
        verify(doctorRepository).save(doctor);
        verify(doctorMapper).toDto(doctor);
    }

    @Test
    void update() {
        // Mock data
        DoctorDTO doctorDTO = new DoctorDTO();
        Doctor doctor = new Doctor();

        // Mock behavior
        when(doctorMapper.toEntity(doctorDTO)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDTO);

        // Invoke method
        DoctorDTO result = doctorService.update(doctorDTO);

        // Verify
        assertNotNull(result);
        assertSame(doctorDTO, result);

        verify(doctorMapper).toEntity(doctorDTO);
        verify(doctorRepository).save(doctor);
        verify(doctorMapper).toDto(doctor);
    }

    @Test
    void partialUpdate() {
        // Mock data
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        Doctor existingDoctor = new Doctor();

        // Mock behavior
        when(doctorRepository.findById(doctorDTO.getId())).thenReturn(Optional.of(existingDoctor));
        when(doctorRepository.save(existingDoctor)).thenReturn(existingDoctor);
        when(doctorMapper.toDto(existingDoctor)).thenReturn(doctorDTO);

        // Invoke method
        Optional<DoctorDTO> result = doctorService.partialUpdate(doctorDTO);

        // Verify
        assertTrue(result.isPresent());
        assertSame(doctorDTO, result.get());

        verify(doctorRepository).findById(doctorDTO.getId());
        verify(doctorMapper).partialUpdate(existingDoctor, doctorDTO);
        verify(doctorRepository).save(existingDoctor);
        verify(doctorMapper).toDto(existingDoctor);
    }

    @Test
    void findAll() {
        // Mock data
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> doctorsPage = new PageImpl<>(Collections.emptyList());
        when(doctorRepository.findAll(pageable)).thenReturn(doctorsPage);

        // Invoke method
        Page<DoctorDTO> result = doctorService.findAll(pageable);

        // Verify
        assertNotNull(result);
        assertEquals(doctorsPage, result);

        verify(doctorRepository).findAll(pageable);
        verify(doctorMapper).toDto(any(Doctor.class));
    }

    @Test
    void findAllWithEagerRelationships() {
        // Mock data
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> doctorsPage = new PageImpl<>(Collections.emptyList());
        when(doctorRepository.findAllWithEagerRelationships(pageable)).thenReturn(doctorsPage);
        when(doctorMapper.toDto(any(Doctor.class))).thenReturn(new DoctorDTO());

        // Invoke method
        Page<DoctorDTO> result = doctorService.findAllWithEagerRelationships(pageable);

        // Verify
        assertNotNull(result);
        assertEquals(doctorsPage, result);
        verify(doctorRepository).findAllWithEagerRelationships(pageable);
        verify(doctorMapper).toDto(any(Doctor.class)); // Corrección aquí
    }

    @Test
    void findOne() {
        // Mock data
        Long id = 1L;
        Doctor doctor = new Doctor();
        when(doctorRepository.findOneWithEagerRelationships(id)).thenReturn(Optional.of(doctor));
        DoctorDTO doctorDTO = new DoctorDTO();
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDTO);

        // Invoke method
        Optional<DoctorDTO> result = doctorService.findOne(id);

        // Verify
        assertTrue(result.isPresent());
        assertSame(doctorDTO, result.get());

        verify(doctorRepository).findOneWithEagerRelationships(id);
        verify(doctorMapper).toDto(doctor);
    }

    @Test
    void delete() {
        // Mock data
        Long id = 1L;

        // Invoke method
        doctorService.delete(id);

        // Verify
        verify(doctorRepository).deleteById(id);
    }
}
