package com.delaiglesia.doctorhouse.service.mapper;

import com.delaiglesia.doctorhouse.domain.Doctor;
import com.delaiglesia.doctorhouse.domain.Place;
import com.delaiglesia.doctorhouse.domain.Specialty;
import com.delaiglesia.doctorhouse.service.dto.DoctorDTO;
import com.delaiglesia.doctorhouse.service.dto.PlaceDTO;
import com.delaiglesia.doctorhouse.service.dto.SpecialtyDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Doctor} and its DTO {@link DoctorDTO}.
 */
@Mapper(componentModel = "spring")
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {
    @Mapping(target = "places", source = "places", qualifiedByName = "placeNameSet")
    @Mapping(target = "especialties", source = "especialties", qualifiedByName = "specialtyNameSet")
    DoctorDTO toDto(Doctor s);

    @Mapping(target = "removePlace", ignore = true)
    @Mapping(target = "removeEspecialty", ignore = true)
    Doctor toEntity(DoctorDTO doctorDTO);

    @Named("placeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PlaceDTO toDtoPlaceName(Place place);

    @Named("placeNameSet")
    default Set<PlaceDTO> toDtoPlaceNameSet(Set<Place> place) {
        return place.stream().map(this::toDtoPlaceName).collect(Collectors.toSet());
    }

    @Named("specialtyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SpecialtyDTO toDtoSpecialtyName(Specialty specialty);

    @Named("specialtyNameSet")
    default Set<SpecialtyDTO> toDtoSpecialtyNameSet(Set<Specialty> specialty) {
        return specialty.stream().map(this::toDtoSpecialtyName).collect(Collectors.toSet());
    }
}
