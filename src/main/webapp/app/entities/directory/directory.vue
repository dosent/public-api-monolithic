<template>
  <div>
    <h2 id="page-heading" data-cy="DirectoryHeading">
      <span id="directory-heading">Directories</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'DirectoryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-directory"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Directory</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && directories && directories.length === 0">
      <span>No Directories found</span>
    </div>
    <div class="table-responsive" v-if="directories && directories.length > 0">
      <table class="table table-striped" aria-describedby="directories">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Directory Name</span></th>
            <th scope="row"><span>Url API</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Is Public</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="directory in directories" :key="directory.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DirectoryView', params: { directoryId: directory.id } }">{{ directory.id }}</router-link>
            </td>
            <td>{{ directory.directoryName }}</td>
            <td>{{ directory.urlAPI }}</td>
            <td>{{ directory.description }}</td>
            <td>{{ directory.isPublic }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'DirectoryView', params: { directoryId: directory.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </router-link>
                <router-link
                  :to="{ name: 'DirectoryEdit', params: { directoryId: directory.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </router-link>
                <b-button
                  @click="prepareRemove(directory)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="apiApplicationApp.directory.delete.question" data-cy="directoryDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-directory-heading">Are you sure you want to delete Directory {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-directory"
            data-cy="entityConfirmDeleteButton"
            @click="removeDirectory()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./directory.component.ts"></script>
