/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ApiKey from './api-key.vue';
import ApiKeyService from './api-key.service';
import AlertService from '@/shared/alert/alert.service';

type ApiKeyComponentType = InstanceType<typeof ApiKey>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ApiKey Management Component', () => {
    let apiKeyServiceStub: SinonStubbedInstance<ApiKeyService>;
    let mountOptions: MountingOptions<ApiKeyComponentType>['global'];

    beforeEach(() => {
      apiKeyServiceStub = sinon.createStubInstance<ApiKeyService>(ApiKeyService);
      apiKeyServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          apiKeyService: () => apiKeyServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        apiKeyServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ApiKey, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(apiKeyServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.apiKeys[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ApiKeyComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ApiKey, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        apiKeyServiceStub.retrieve.reset();
        apiKeyServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        apiKeyServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeApiKey();
        await comp.$nextTick(); // clear components

        // THEN
        expect(apiKeyServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(apiKeyServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
