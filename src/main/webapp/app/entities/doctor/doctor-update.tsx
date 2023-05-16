import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPlace } from 'app/shared/model/place.model';
import { getEntities as getPlaces } from 'app/entities/place/place.reducer';
import { ISpecialty } from 'app/shared/model/specialty.model';
import { getEntities as getSpecialties } from 'app/entities/specialty/specialty.reducer';
import { IDoctor } from 'app/shared/model/doctor.model';
import { getEntity, updateEntity, createEntity, reset } from './doctor.reducer';

export const DoctorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const places = useAppSelector(state => state.place.entities);
  const specialties = useAppSelector(state => state.specialty.entities);
  const doctorEntity = useAppSelector(state => state.doctor.entity);
  const loading = useAppSelector(state => state.doctor.loading);
  const updating = useAppSelector(state => state.doctor.updating);
  const updateSuccess = useAppSelector(state => state.doctor.updateSuccess);

  const handleClose = () => {
    navigate('/doctor');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getPlaces({}));
    dispatch(getSpecialties({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.hireDate = convertDateTimeToServer(values.hireDate);

    const entity = {
      ...doctorEntity,
      ...values,
      places: mapIdList(values.places),
      especialties: mapIdList(values.especialties),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          hireDate: displayDefaultDateTime(),
        }
      : {
          ...doctorEntity,
          hireDate: convertDateTimeFromServer(doctorEntity.hireDate),
          places: doctorEntity?.places?.map(e => e.id.toString()),
          especialties: doctorEntity?.especialties?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="doctorHouseJHipsterVersionApp.doctor.home.createOrEditLabel" data-cy="DoctorCreateUpdateHeading">
            <Translate contentKey="doctorHouseJHipsterVersionApp.doctor.home.createOrEditLabel">Create or edit a Doctor</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="doctor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('doctorHouseJHipsterVersionApp.doctor.name')}
                id="doctor-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('doctorHouseJHipsterVersionApp.doctor.email')}
                id="doctor-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('doctorHouseJHipsterVersionApp.doctor.phoneNumber')}
                id="doctor-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('doctorHouseJHipsterVersionApp.doctor.hireDate')}
                id="doctor-hireDate"
                name="hireDate"
                data-cy="hireDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('doctorHouseJHipsterVersionApp.doctor.license')}
                id="doctor-license"
                name="license"
                data-cy="license"
                type="text"
              />
              <ValidatedBlobField
                label={translate('doctorHouseJHipsterVersionApp.doctor.image')}
                id="doctor-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('doctorHouseJHipsterVersionApp.doctor.place')}
                id="doctor-place"
                data-cy="place"
                type="select"
                multiple
                name="places"
              >
                <option value="" key="0" />
                {places
                  ? places.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('doctorHouseJHipsterVersionApp.doctor.especialty')}
                id="doctor-especialty"
                data-cy="especialty"
                type="select"
                multiple
                name="especialties"
              >
                <option value="" key="0" />
                {specialties
                  ? specialties.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/doctor" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DoctorUpdate;
