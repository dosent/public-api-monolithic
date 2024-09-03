import { type Actual } from '@/shared/model/enumerations/actual.model';
export interface ICountry {
  id?: number;
  code?: number | null;
  value?: string | null;
  nameShort?: string | null;
  nameFull?: string | null;
  unrestrictedValue?: string | null;
  codeCountry?: string | null;
  alfa2Country?: string | null;
  alfa3Country?: string | null;
  isActual?: keyof typeof Actual | null;
}

export class Country implements ICountry {
  constructor(
    public id?: number,
    public code?: number | null,
    public value?: string | null,
    public nameShort?: string | null,
    public nameFull?: string | null,
    public unrestrictedValue?: string | null,
    public codeCountry?: string | null,
    public alfa2Country?: string | null,
    public alfa3Country?: string | null,
    public isActual?: keyof typeof Actual | null,
  ) {}
}
