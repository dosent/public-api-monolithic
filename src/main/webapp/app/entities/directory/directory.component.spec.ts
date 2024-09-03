/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Directory from './directory.vue';
import DirectoryService from './directory.service';
import AlertService from '@/shared/alert/alert.service';

type DirectoryComponentType = InstanceType<typeof Directory>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Directory Management Component', () => {
    let directoryServiceStub: SinonStubbedInstance<DirectoryService>;
    let mountOptions: MountingOptions<DirectoryComponentType>['global'];

    beforeEach(() => {
      directoryServiceStub = sinon.createStubInstance<DirectoryService>(DirectoryService);
      directoryServiceStub.retrieve.resolves({ headers: {} });

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
          directoryService: () => directoryServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        directoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Directory, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(directoryServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.directories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: DirectoryComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Directory, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        directoryServiceStub.retrieve.reset();
        directoryServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        directoryServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeDirectory();
        await comp.$nextTick(); // clear components

        // THEN
        expect(directoryServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(directoryServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
