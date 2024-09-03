import { type Publication } from '@/shared/model/enumerations/publication.model';
export interface IDirectory {
  id?: number;
  directoryName?: string | null;
  urlAPI?: string | null;
  description?: string | null;
  isPublic?: keyof typeof Publication | null;
}

export class Directory implements IDirectory {
  constructor(
    public id?: number,
    public directoryName?: string | null,
    public urlAPI?: string | null,
    public description?: string | null,
    public isPublic?: keyof typeof Publication | null,
  ) {}
}
