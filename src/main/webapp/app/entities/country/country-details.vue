<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="country">
        <h2 class="jh-entity-heading" data-cy="countryDetailsHeading"><span>Country</span> {{ country.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Code</span>
          </dt>
          <dd>
            <span>{{ country.code }}</span>
          </dd>
          <dt>
            <span>Value</span>
          </dt>
          <dd>
            <span>{{ country.value }}</span>
          </dd>
          <dt>
            <span>Name Short</span>
          </dt>
          <dd>
            <span>{{ country.nameShort }}</span>
          </dd>
          <dt>
            <span>Name Full</span>
          </dt>
          <dd>
            <span>{{ country.nameFull }}</span>
          </dd>
          <dt>
            <span>Name Short Eng</span>
          </dt>
          <dd>
            <span>{{ country.nameShortEng }}</span>
          </dd>
          <dt>
            <span>Name Full Eng</span>
          </dt>
          <dd>
            <span>{{ country.nameFullEng }}</span>
          </dd>
          <dt>
            <span>Unrestricted Value</span>
          </dt>
          <dd>
            <span>{{ country.unrestrictedValue }}</span>
          </dd>
          <dt>
            <span>Code Country</span>
          </dt>
          <dd>
            <span>{{ country.codeCountry }}</span>
          </dd>
          <dt>
            <span>Alfa 2 Country</span>
          </dt>
          <dd>
            <span>{{ country.alfa2Country }}</span>
          </dd>
          <dt>
            <span>Alfa 3 Country</span>
          </dt>
          <dd>
            <span>{{ country.alfa3Country }}</span>
          </dd>
          <dt>
            <span>Is Actual</span>
          </dt>
          <dd>
            <span>{{ country.isActual }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link v-if="country.id" :to="{ name: 'CountryEdit', params: { countryId: country.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./country-details.component.ts"></script>
