import { IDoctor } from 'app/shared/model/doctor.model';

export interface IPlace {
  id?: number;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  doctors?: IDoctor[] | null;
}

export const defaultValue: Readonly<IPlace> = {};
