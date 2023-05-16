package com.delaiglesia.doctorhouse.service.mapper;

import com.delaiglesia.doctorhouse.domain.Specialty;
import com.delaiglesia.doctorhouse.service.dto.SpecialtyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialty} and its DTO {@link SpecialtyDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {}
