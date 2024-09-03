/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DirectoryDetails from './directory-details.vue';
import DirectoryService from './directory.service';
import AlertService from '@/shared/alert/alert.service';

type DirectoryDetailsComponentType = InstanceType<typeof DirectoryDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const directorySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Directory Management Detail Component', () => {
    let directoryServiceStub: SinonStubbedInstance<DirectoryService>;
    let mountOptions: MountingOptions<DirectoryDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      directoryServiceStub = sinon.createStubInstance<DirectoryService>(DirectoryService);

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          directoryService: () => directoryServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        directoryServiceStub.find.resolves(directorySample);
        route = {
          params: {
            directoryId: `${123}`,
          },
        };
        const wrapper = shallowMount(DirectoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.directory).toMatchObject(directorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        directoryServiceStub.find.resolves(directorySample);
        const wrapper = shallowMount(DirectoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
