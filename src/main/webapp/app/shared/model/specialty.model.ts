import { IDoctor } from 'app/shared/model/doctor.model';

export interface ISpecialty {
  id?: number;
  name?: string;
  description?: string | null;
  doctors?: IDoctor[] | null;
}

export const defaultValue: Readonly<ISpecialty> = {};
