package com.delaiglesia.doctorhouse.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaceMapperTest {

    private PlaceMapper placeMapper;

    @BeforeEach
    public void setUp() {
        placeMapper = new PlaceMapperImpl();
    }
}
