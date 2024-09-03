import { type Actual } from '@/shared/model/enumerations/actual.model';
export interface IApiKey {
  id?: number;
  keyHash?: string | null;
  description?: string | null;
  isActual?: keyof typeof Actual | null;
}

export class ApiKey implements IApiKey {
  constructor(
    public id?: number,
    public keyHash?: string | null,
    public description?: string | null,
    public isActual?: keyof typeof Actual | null,
  ) {}
}
