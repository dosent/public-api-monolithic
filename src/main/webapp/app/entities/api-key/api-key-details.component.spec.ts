/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApiKeyDetails from './api-key-details.vue';
import ApiKeyService from './api-key.service';
import AlertService from '@/shared/alert/alert.service';

type ApiKeyDetailsComponentType = InstanceType<typeof ApiKeyDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const apiKeySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ApiKey Management Detail Component', () => {
    let apiKeyServiceStub: SinonStubbedInstance<ApiKeyService>;
    let mountOptions: MountingOptions<ApiKeyDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      apiKeyServiceStub = sinon.createStubInstance<ApiKeyService>(ApiKeyService);

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
          apiKeyService: () => apiKeyServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        apiKeyServiceStub.find.resolves(apiKeySample);
        route = {
          params: {
            apiKeyId: `${123}`,
          },
        };
        const wrapper = shallowMount(ApiKeyDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.apiKey).toMatchObject(apiKeySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        apiKeyServiceStub.find.resolves(apiKeySample);
        const wrapper = shallowMount(ApiKeyDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
