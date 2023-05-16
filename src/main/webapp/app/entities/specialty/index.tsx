import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Specialty from './specialty';
import SpecialtyDetail from './specialty-detail';
import SpecialtyUpdate from './specialty-update';
import SpecialtyDeleteDialog from './specialty-delete-dialog';

const SpecialtyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Specialty />} />
    <Route path="new" element={<SpecialtyUpdate />} />
    <Route path=":id">
      <Route index element={<SpecialtyDetail />} />
      <Route path="edit" element={<SpecialtyUpdate />} />
      <Route path="delete" element={<SpecialtyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpecialtyRoutes;
