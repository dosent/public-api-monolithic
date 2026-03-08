# CI/CD Pipeline для Cloud.ru

## Обзор

Этот проект настроен для автоматического развертывания на платформе Cloud.ru через GitHub Actions.

## Структура файлов

- `Dockerfile` - Многоступенчатый Dockerfile для сборки приложения
- `docker-compose.yml` - Конфигурация для локального запуска с PostgreSQL
- `.github/workflows/ci-cd.yml` - Pipeline для сборки, тестирования и деплоя
- `.env.example` - Шаблон переменных окружения

## Настройка секретов в GitHub

Добавьте следующие секреты в настройки вашего репозитория (Settings → Secrets and variables → Actions):

### Обязательные секреты:

| Secret | Описание |
|--------|----------|
| `CLOUD_REGISTRY_USERNAME` | Логин для доступа к registry.cloud.ru |
| `CLOUD_REGISTRY_PASSWORD` | Пароль/токен для доступа к registry.cloud.ru |
| `DB_HOST` | Хост базы данных PostgreSQL в Cloud.ru |
| `DB_NAME` | Имя базы данных |
| `DB_USERNAME` | Пользователь базы данных |
| `DB_PASSWORD` | Пароль пользователя базы данных |
| `JHIPSTER_SECURITY_KEY` | Уникальный ключ безопасности JHipster |

### Опциональные секреты:

| Secret | Описание |
|--------|----------|
| `CLOUD_API_KEY` | API ключ для управления ресурсами Cloud.ru |
| `CLOUD_FOLDER_ID` | ID папки в Cloud.ru |

## Локальная разработка

1. Скопируйте файл переменных окружения:
   ```bash
   cp .env.example .env
   ```

2. Отредактируйте `.env`, указав ваши параметры БД

3. Запустите приложение:
   ```bash
   docker-compose up -d
   ```

4. Приложение будет доступно по адресу: http://localhost:8587

## Процесс CI/CD

### 1. Build and Test (при каждом push/PR)
- Установка JDK 17 и Node.js
- Сборка frontend (npm)
- Сборка backend (Maven)
- Запуск тестов с PostgreSQL

### 2. Build and Push (только при push в main/master)
- Логин в registry.cloud.ru
- Сборка Docker образа
- Пуш образа с тегами:
  - `latest`
  - `<commit-sha>`
  - `<branch-name>`

### 3. Deploy (только при push в main/master)
- Деплой на Cloud.ru
- Health check приложения

## Ручной деплой

Для ручного деплоя выполните:

```bash
# Логин в registry
docker login registry.cloud.ru

# Сборка образа
docker build -t registry.cloud.ru/api-application:latest .

# Пуш образа
docker push registry.cloud.ru/api-application:latest

# Деплой (пример для Kubernetes)
kubectl set image deployment/api-application api-application=registry.cloud.ru/api-application:latest
```

## Доменное имя

Приложение будет доступно по адресу: **http://public-api.ru**

Убедитесь, что домен настроен на соответствующий ресурс Cloud.ru (Load Balancer, Cloud Run, или Kubernetes Ingress).

## Мониторинг

Health endpoint: `http://public-api.ru/management/health`

Metrics endpoint: `http://public-api.ru/management/prometheus` (если включено)

## Troubleshooting

### Ошибки подключения к БД
- Проверьте правильность credentials в секретах
- Убедитесь, что БД доступна из сети Cloud.ru
- Проверьте security groups

### Ошибки сборки Docker
- Проверьте наличие `Dockerfile` в корне проекта
- Убедитесь, что все зависимости доступны

### Ошибки деплоя
- Проверьте логи GitHub Actions
- Убедитесь, что у сервисного аккаунта есть необходимые права
