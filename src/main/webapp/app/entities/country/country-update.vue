<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="apiApplicationApp.country.home.createOrEditLabel" data-cy="CountryCreateUpdateHeading">Create or edit a Country</h2>
        <div>
          <div class="form-group" v-if="country.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="country.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-code">Code</label>
            <input
              type="number"
              class="form-control"
              name="code"
              id="country-code"
              data-cy="code"
              :class="{ valid: !v$.code.$invalid, invalid: v$.code.$invalid }"
              v-model.number="v$.code.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-value">Value</label>
            <input
              type="text"
              class="form-control"
              name="value"
              id="country-value"
              data-cy="value"
              :class="{ valid: !v$.value.$invalid, invalid: v$.value.$invalid }"
              v-model="v$.value.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-nameShort">Name Short</label>
            <input
              type="text"
              class="form-control"
              name="nameShort"
              id="country-nameShort"
              data-cy="nameShort"
              :class="{ valid: !v$.nameShort.$invalid, invalid: v$.nameShort.$invalid }"
              v-model="v$.nameShort.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-nameFull">Name Full</label>
            <input
              type="text"
              class="form-control"
              name="nameFull"
              id="country-nameFull"
              data-cy="nameFull"
              :class="{ valid: !v$.nameFull.$invalid, invalid: v$.nameFull.$invalid }"
              v-model="v$.nameFull.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-nameShortEng">Name Short Eng</label>
            <input
              type="text"
              class="form-control"
              name="nameShortEng"
              id="country-nameShortEng"
              data-cy="nameShortEng"
              :class="{ valid: !v$.nameShortEng.$invalid, invalid: v$.nameShortEng.$invalid }"
              v-model="v$.nameShortEng.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-nameFullEng">Name Full Eng</label>
            <input
              type="text"
              class="form-control"
              name="nameFullEng"
              id="country-nameFullEng"
              data-cy="nameFullEng"
              :class="{ valid: !v$.nameFullEng.$invalid, invalid: v$.nameFullEng.$invalid }"
              v-model="v$.nameFullEng.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-unrestrictedValue">Unrestricted Value</label>
            <input
              type="text"
              class="form-control"
              name="unrestrictedValue"
              id="country-unrestrictedValue"
              data-cy="unrestrictedValue"
              :class="{ valid: !v$.unrestrictedValue.$invalid, invalid: v$.unrestrictedValue.$invalid }"
              v-model="v$.unrestrictedValue.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-codeCountry">Code Country</label>
            <input
              type="text"
              class="form-control"
              name="codeCountry"
              id="country-codeCountry"
              data-cy="codeCountry"
              :class="{ valid: !v$.codeCountry.$invalid, invalid: v$.codeCountry.$invalid }"
              v-model="v$.codeCountry.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-alfa2Country">Alfa 2 Country</label>
            <input
              type="text"
              class="form-control"
              name="alfa2Country"
              id="country-alfa2Country"
              data-cy="alfa2Country"
              :class="{ valid: !v$.alfa2Country.$invalid, invalid: v$.alfa2Country.$invalid }"
              v-model="v$.alfa2Country.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-alfa3Country">Alfa 3 Country</label>
            <input
              type="text"
              class="form-control"
              name="alfa3Country"
              id="country-alfa3Country"
              data-cy="alfa3Country"
              :class="{ valid: !v$.alfa3Country.$invalid, invalid: v$.alfa3Country.$invalid }"
              v-model="v$.alfa3Country.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="country-isActual">Is Actual</label>
            <select
              class="form-control"
              name="isActual"
              :class="{ valid: !v$.isActual.$invalid, invalid: v$.isActual.$invalid }"
              v-model="v$.isActual.$model"
              id="country-isActual"
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
<script lang="ts" src="./country-update.component.ts"></script>
