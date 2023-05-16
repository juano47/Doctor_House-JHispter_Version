import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/place">
        <Translate contentKey="global.menu.entities.place" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/specialty">
        <Translate contentKey="global.menu.entities.specialty" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/doctor">
        <Translate contentKey="global.menu.entities.doctor" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
