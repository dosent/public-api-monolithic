import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import DirectoryService from './directory.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Directory, type IDirectory } from '@/shared/model/directory.model';
import { Publication } from '@/shared/model/enumerations/publication.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DirectoryUpdate',
  setup() {
    const directoryService = inject('directoryService', () => new DirectoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const directory: Ref<IDirectory> = ref(new Directory());
    const publicationValues: Ref<string[]> = ref(Object.keys(Publication));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDirectory = async directoryId => {
      try {
        const res = await directoryService().find(directoryId);
        directory.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.directoryId) {
      retrieveDirectory(route.params.directoryId);
    }

    const validations = useValidation();
    const validationRules = {
      directoryName: {},
      urlAPI: {},
      description: {},
      isPublic: {},
    };
    const v$ = useVuelidate(validationRules, directory as any);
    v$.value.$validate();

    return {
      directoryService,
      alertService,
      directory,
      previousState,
      publicationValues,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.directory.id) {
        this.directoryService()
          .update(this.directory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Directory is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.directoryService()
          .create(this.directory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Directory is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
