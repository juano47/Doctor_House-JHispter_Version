package com.delaiglesia.doctorhouse.service.mapper;

import com.delaiglesia.doctorhouse.domain.Place;
import com.delaiglesia.doctorhouse.service.dto.PlaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Place} and its DTO {@link PlaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaceMapper extends EntityMapper<PlaceDTO, Place> {}
