import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ApiKeyService from './api-key.service';
import { type IApiKey } from '@/shared/model/api-key.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApiKeyDetails',
  setup() {
    const apiKeyService = inject('apiKeyService', () => new ApiKeyService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const apiKey: Ref<IApiKey> = ref({});

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

    return {
      alertService,
      apiKey,

      previousState,
    };
  },
});
