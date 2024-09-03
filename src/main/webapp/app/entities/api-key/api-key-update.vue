<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="apiApplicationApp.apiKey.home.createOrEditLabel" data-cy="ApiKeyCreateUpdateHeading">Create or edit a Api Key</h2>
        <div>
          <div class="form-group" v-if="apiKey.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="apiKey.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="api-key-keyHash">Key Hash</label>
            <input
              type="text"
              class="form-control"
              name="keyHash"
              id="api-key-keyHash"
              data-cy="keyHash"
              :class="{ valid: !v$.keyHash.$invalid, invalid: v$.keyHash.$invalid }"
              v-model="v$.keyHash.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="api-key-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="api-key-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="api-key-isActual">Is Actual</label>
            <select
              class="form-control"
              name="isActual"
              :class="{ valid: !v$.isActual.$invalid, invalid: v$.isActual.$invalid }"
              v-model="v$.isActual.$model"
              id="api-key-isActual"
              data-cy="isActual"
            >
              <option v-for="actual in actualValues" :key="actual" :value="actual">{{ actual }}</option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./api-key-update.component.ts"></script>
