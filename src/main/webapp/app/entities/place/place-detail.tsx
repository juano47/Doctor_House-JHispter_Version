import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './place.reducer';

export const PlaceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const placeEntity = useAppSelector(state => state.place.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="placeDetailsHeading">
          <Translate contentKey="doctorHouseJHipsterVersionApp.place.detail.title">Place</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{placeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="doctorHouseJHipsterVersionApp.place.name">Name</Translate>
            </span>
          </dt>
          <dd>{placeEntity.name}</dd>
          <dt>
            <span id="streetAddress">
              <Translate contentKey="doctorHouseJHipsterVersionApp.place.streetAddress">Street Address</Translate>
            </span>
          </dt>
          <dd>{placeEntity.streetAddress}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="doctorHouseJHipsterVersionApp.place.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{placeEntity.postalCode}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="doctorHouseJHipsterVersionApp.place.city">City</Translate>
            </span>
          </dt>
          <dd>{placeEntity.city}</dd>
          <dt>
            <span id="stateProvince">
              <Translate contentKey="doctorHouseJHipsterVersionApp.place.stateProvince">State Province</Translate>
            </span>
          </dt>
          <dd>{placeEntity.stateProvince}</dd>
        </dl>
        <Button tag={Link} to="/place" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/place/${placeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlaceDetail;
