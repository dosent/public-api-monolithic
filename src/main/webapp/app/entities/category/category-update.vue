<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="apiApplicationApp.category.home.createOrEditLabel" data-cy="CategoryCreateUpdateHeading">Create or edit a Category</h2>
        <div>
          <div class="form-group" v-if="category.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="category.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="category-categoryName">Category Name</label>
            <input
              type="text"
              class="form-control"
              name="categoryName"
              id="category-categoryName"
              data-cy="categoryName"
              :class="{ valid: !v$.categoryName.$invalid, invalid: v$.categoryName.$invalid }"
              v-model="v$.categoryName.$model"
            />
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
<script lang="ts" src="./category-update.component.ts"></script>
