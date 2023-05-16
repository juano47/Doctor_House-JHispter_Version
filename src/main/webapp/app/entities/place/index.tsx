import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Place from './place';
import PlaceDetail from './place-detail';
import PlaceUpdate from './place-update';
import PlaceDeleteDialog from './place-delete-dialog';

const PlaceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Place />} />
    <Route path="new" element={<PlaceUpdate />} />
    <Route path=":id">
      <Route index element={<PlaceDetail />} />
      <Route path="edit" element={<PlaceUpdate />} />
      <Route path="delete" element={<PlaceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlaceRoutes;
