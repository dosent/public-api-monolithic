import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';

import DirectoryService from './directory.service';
import { type IDirectory } from '@/shared/model/directory.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Directory',
  setup() {
    const directoryService = inject('directoryService', () => new DirectoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const directories: Ref<IDirectory[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveDirectorys = async () => {
      isFetching.value = true;
      try {
        const res = await directoryService().retrieve();
        directories.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveDirectorys();
    };

    onMounted(async () => {
      await retrieveDirectorys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IDirectory) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeDirectory = async () => {
      try {
        await directoryService().delete(removeId.value);
        const message = `A Directory is deleted with identifier ${removeId.value}`;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveDirectorys();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      directories,
      handleSyncList,
      isFetching,
      retrieveDirectorys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeDirectory,
    };
  },
});
