<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="apiKey">
        <h2 class="jh-entity-heading" data-cy="apiKeyDetailsHeading"><span>Api Key</span> {{ apiKey.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Key Hash</span>
          </dt>
          <dd>
            <span>{{ apiKey.keyHash }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ apiKey.description }}</span>
          </dd>
          <dt>
            <span>Is Actual</span>
          </dt>
          <dd>
            <span>{{ apiKey.isActual }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link v-if="apiKey.id" :to="{ name: 'ApiKeyEdit', params: { apiKeyId: apiKey.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./api-key-details.component.ts"></script>
