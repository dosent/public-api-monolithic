import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';

import ApiKeyService from './api-key.service';
import { type IApiKey } from '@/shared/model/api-key.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApiKey',
  setup() {
    const apiKeyService = inject('apiKeyService', () => new ApiKeyService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const apiKeys: Ref<IApiKey[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveApiKeys = async () => {
      isFetching.value = true;
      try {
        const res = await apiKeyService().retrieve();
        apiKeys.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveApiKeys();
    };

    onMounted(async () => {
      await retrieveApiKeys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IApiKey) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeApiKey = async () => {
      try {
        await apiKeyService().delete(removeId.value);
        const message = `A ApiKey is deleted with identifier ${removeId.value}`;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveApiKeys();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      apiKeys,
      handleSyncList,
      isFetching,
      retrieveApiKeys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeApiKey,
    };
  },
});
