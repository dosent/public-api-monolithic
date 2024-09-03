/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DirectoryUpdate from './directory-update.vue';
import DirectoryService from './directory.service';
import AlertService from '@/shared/alert/alert.service';

type DirectoryUpdateComponentType = InstanceType<typeof DirectoryUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const directorySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DirectoryUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Directory Management Update Component', () => {
    let comp: DirectoryUpdateComponentType;
    let directoryServiceStub: SinonStubbedInstance<DirectoryService>;

    beforeEach(() => {
      route = {};
      directoryServiceStub = sinon.createStubInstance<DirectoryService>(DirectoryService);
      directoryServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          directoryService: () => directoryServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(DirectoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.directory = directorySample;
        directoryServiceStub.update.resolves(directorySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(directoryServiceStub.update.calledWith(directorySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        directoryServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DirectoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.directory = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(directoryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        directoryServiceStub.find.resolves(directorySample);
        directoryServiceStub.retrieve.resolves([directorySample]);

        // WHEN
        route = {
          params: {
            directoryId: `${directorySample.id}`,
          },
        };
        const wrapper = shallowMount(DirectoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.directory).toMatchObject(directorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        directoryServiceStub.find.resolves(directorySample);
        const wrapper = shallowMount(DirectoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
