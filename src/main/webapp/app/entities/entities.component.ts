import { defineComponent, provide } from 'vue';

import CategoryService from './category/category.service';
import CountryService from './country/country.service';
import DirectoryService from './directory/directory.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('categoryService', () => new CategoryService());
    provide('countryService', () => new CountryService());
    provide('directoryService', () => new DirectoryService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
