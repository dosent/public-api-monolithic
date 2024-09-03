<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="directory">
        <h2 class="jh-entity-heading" data-cy="directoryDetailsHeading"><span>Directory</span> {{ directory.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Directory Name</span>
          </dt>
          <dd>
            <span>{{ directory.directoryName }}</span>
          </dd>
          <dt>
            <span>Url API</span>
          </dt>
          <dd>
            <span>{{ directory.urlAPI }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ directory.description }}</span>
          </dd>
          <dt>
            <span>Is Public</span>
          </dt>
          <dd>
            <span>{{ directory.isPublic }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="directory.id"
          :to="{ name: 'DirectoryEdit', params: { directoryId: directory.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./directory-details.component.ts"></script>
