import dayjs from 'dayjs';
import { IPlace } from 'app/shared/model/place.model';
import { ISpecialty } from 'app/shared/model/specialty.model';

export interface IDoctor {
  id?: number;
  name?: string;
  email?: string | null;
  phoneNumber?: string | null;
  hireDate?: string | null;
  license?: number | null;
  imageContentType?: string | null;
  image?: string | null;
  places?: IPlace[] | null;
  especialties?: ISpecialty[] | null;
}

export const defaultValue: Readonly<IDoctor> = {};
