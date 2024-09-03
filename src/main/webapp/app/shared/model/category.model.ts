export interface ICategory {
  id?: number;
  categoryName?: string | null;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public categoryName?: string | null,
  ) {}
}
