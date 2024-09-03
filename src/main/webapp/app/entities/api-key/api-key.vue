<template>
  <div>
    <h2 id="page-heading" data-cy="ApiKeyHeading">
      <span id="api-key-heading">Api Keys</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ApiKeyCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-api-key"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Api Key</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && apiKeys && apiKeys.length === 0">
      <span>No Api Keys found</span>
    </div>
    <div class="table-responsive" v-if="apiKeys && apiKeys.length > 0">
      <table class="table table-striped" aria-describedby="apiKeys">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Key Hash</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Is Actual</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="apiKey in apiKeys" :key="apiKey.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ApiKeyView', params: { apiKeyId: apiKey.id } }">{{ apiKey.id }}</router-link>
            </td>
            <td>{{ apiKey.keyHash }}</td>
            <td>{{ apiKey.description }}</td>
            <td>{{ apiKey.isActual }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ApiKeyView', params: { apiKeyId: apiKey.id } }"
                  class="btn btn-info btn-sm details"
                  data-cy="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </router-link>
                <router-link
                  :to="{ name: 'ApiKeyEdit', params: { apiKeyId: apiKey.id } }"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </router-link>
                <b-button
                  @click="prepareRemove(apiKey)"
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
        <span id="apiApplicationApp.apiKey.delete.question" data-cy="apiKeyDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-apiKey-heading">Are you sure you want to delete Api Key {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-apiKey"
            data-cy="entityConfirmDeleteButton"
            @click="removeApiKey()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./api-key.component.ts"></script>
