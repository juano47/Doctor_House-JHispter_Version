import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './specialty.reducer';

export const SpecialtyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specialtyEntity = useAppSelector(state => state.specialty.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specialtyDetailsHeading">
          <Translate contentKey="doctorHouseJHipsterVersionApp.specialty.detail.title">Specialty</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{specialtyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="doctorHouseJHipsterVersionApp.specialty.name">Name</Translate>
            </span>
          </dt>
          <dd>{specialtyEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="doctorHouseJHipsterVersionApp.specialty.description">Description</Translate>
            </span>
          </dt>
          <dd>{specialtyEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/specialty" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/specialty/${specialtyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecialtyDetail;
