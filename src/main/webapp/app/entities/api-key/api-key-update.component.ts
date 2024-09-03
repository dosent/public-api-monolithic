import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ApiKeyService from './api-key.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { ApiKey, type IApiKey } from '@/shared/model/api-key.model';
import { Actual } from '@/shared/model/enumerations/actual.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApiKeyUpdate',
  setup() {
    const apiKeyService = inject('apiKeyService', () => new ApiKeyService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const apiKey: Ref<IApiKey> = ref(new ApiKey());
    const actualValues: Ref<string[]> = ref(Object.keys(Actual));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveApiKey = async apiKeyId => {
      try {
        const res = await apiKeyService().find(apiKeyId);
        apiKey.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.apiKeyId) {
      retrieveApiKey(route.params.apiKeyId);
    }

    const validations = useValidation();
    const validationRules = {
      keyHash: {},
      description: {},
      isActual: {},
    };
    const v$ = useVuelidate(validationRules, apiKey as any);
    v$.value.$validate();

    return {
      apiKeyService,
      alertService,
      apiKey,
      previousState,
      actualValues,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.apiKey.id) {
        this.apiKeyService()
          .update(this.apiKey)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A ApiKey is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.apiKeyService()
          .create(this.apiKey)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A ApiKey is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
