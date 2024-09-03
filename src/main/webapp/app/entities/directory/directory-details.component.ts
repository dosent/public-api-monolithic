import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import DirectoryService from './directory.service';
import { type IDirectory } from '@/shared/model/directory.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DirectoryDetails',
  setup() {
    const directoryService = inject('directoryService', () => new DirectoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const directory: Ref<IDirectory> = ref({});

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

    return {
      alertService,
      directory,

      previousState,
    };
  },
});
