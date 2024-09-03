<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="apiApplicationApp.directory.home.createOrEditLabel" data-cy="DirectoryCreateUpdateHeading">Create or edit a Directory</h2>
        <div>
          <div class="form-group" v-if="directory.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="directory.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="directory-directoryName">Directory Name</label>
            <input
              type="text"
              class="form-control"
              name="directoryName"
              id="directory-directoryName"
              data-cy="directoryName"
              :class="{ valid: !v$.directoryName.$invalid, invalid: v$.directoryName.$invalid }"
              v-model="v$.directoryName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="directory-urlAPI">Url API</label>
            <input
              type="text"
              class="form-control"
              name="urlAPI"
              id="directory-urlAPI"
              data-cy="urlAPI"
              :class="{ valid: !v$.urlAPI.$invalid, invalid: v$.urlAPI.$invalid }"
              v-model="v$.urlAPI.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="directory-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="directory-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="directory-isPublic">Is Public</label>
            <select
              class="form-control"
              name="isPublic"
              :class="{ valid: !v$.isPublic.$invalid, invalid: v$.isPublic.$invalid }"
              v-model="v$.isPublic.$model"
              id="directory-isPublic"
              data-cy="isPublic"
            >
              <option v-for="publication in publicationValues" :key="publication" :value="publication">{{ publication }}</option>
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
<script lang="ts" src="./directory-update.component.ts"></script>
