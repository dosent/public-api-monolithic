/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApiKeyUpdate from './api-key-update.vue';
import ApiKeyService from './api-key.service';
import AlertService from '@/shared/alert/alert.service';

type ApiKeyUpdateComponentType = InstanceType<typeof ApiKeyUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const apiKeySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ApiKeyUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ApiKey Management Update Component', () => {
    let comp: ApiKeyUpdateComponentType;
    let apiKeyServiceStub: SinonStubbedInstance<ApiKeyService>;

    beforeEach(() => {
      route = {};
      apiKeyServiceStub = sinon.createStubInstance<ApiKeyService>(ApiKeyService);
      apiKeyServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          apiKeyService: () => apiKeyServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ApiKeyUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.apiKey = apiKeySample;
        apiKeyServiceStub.update.resolves(apiKeySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(apiKeyServiceStub.update.calledWith(apiKeySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        apiKeyServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ApiKeyUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.apiKey = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(apiKeyServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        apiKeyServiceStub.find.resolves(apiKeySample);
        apiKeyServiceStub.retrieve.resolves([apiKeySample]);

        // WHEN
        route = {
          params: {
            apiKeyId: `${apiKeySample.id}`,
          },
        };
        const wrapper = shallowMount(ApiKeyUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.apiKey).toMatchObject(apiKeySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        apiKeyServiceStub.find.resolves(apiKeySample);
        const wrapper = shallowMount(ApiKeyUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
