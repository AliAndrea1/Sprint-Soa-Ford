# AutoInsight API — Arquitetura Orientada a Serviços

> Projeto desenvolvido para o Challenge Ford FIAP 2026 — Inteligência Competitiva Automotiva

---

## Índice

- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Banco de Dados (MySQL Workbench)](#configuração-do-banco-de-dados)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Como Rodar](#como-rodar)
- [Endpoints da API](#endpoints-da-api)
- [Documentação Swagger](#documentação-swagger)

---

## Visão Geral

A **AutoInsight API** é uma API RESTful desenvolvida em **Spring Boot** que centraliza o processamento de dados de inteligência competitiva automotiva. Ela recebe requisições do app mobile, autentica usuários via JWT, consulta especificações técnicas de veículos e armazena o histórico de buscas de forma criptografada.

---

## Arquitetura

O projeto segue o padrão **SOA (Arquitetura Orientada a Serviços e Web Services)**, com separação clara entre camadas independentes e reutilizáveis.

<img width="406" height="1531" alt="Image" src="https://github.com/user-attachments/assets/514b6dc9-a429-4570-a278-96c1fbc29bba" />

> **Nota:** A camada de **Security** foi desenvolvida em conjunto com os requisitos da disciplina de **Cybersecurity**, incluindo autenticação JWT, controle de acesso por papéis (RBAC), rate limiting, criptografia AES/GCM e trilha de auditoria.

---

## Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.5.14 | Framework da API |
| Spring Security | - | Gerenciado pelo Spring Boot |
| Spring Data JPA | - | Gerenciado pelo Spring Boot |
| MySQL Connector | - | Gerenciado pelo Spring Boot |
| MySQL | 8.0 | Banco de dados relacional |
| Flyway | - | Gerenciado pelo Spring Boot |
| JWT (JJWT) | 0.12.6 | Tokens de autenticação |
| Bucket4j | 7.6.0 | Rate limiting |
| Springdoc OpenAPI | 2.8.8 | Documentação Swagger |
| Lombok | - | Gerenciado pelo Spring Boot |

---

## Estrutura do Projeto

```
src/main/java/com/autoinsight/autoinsight_api/
├── config/
│   ├── SecurityConfig.java       # Configuração do Spring Security
│   └── SwaggerConfig.java        # Configuração do Swagger e CORS
├── controller/
│   ├── AuthController.java       # Login e geração de token JWT
│   ├── VehicleController.java    # CRUD de veículos e busca
│   ├── SearchHistoryController.java  # Histórico de buscas
│   └── AuditLogController.java   # Logs de auditoria (admin only)
├── dto/
│   ├── ApiResponseDTO.java       # Resposta padrão da API
│   ├── LoginRequestDTO.java      # Dados de login
│   ├── LoginResponseDTO.java     # Token e dados do usuário
│   ├── VehicleRequestDTO.java    # Criação/edição de veículo
│   ├── VehicleResponseDTO.java   # Retorno de veículo
│   ├── SpecificationRequestDTO.java
│   ├── SpecificationResponseDTO.java
│   └── SearchHistoryResponseDTO.java
├── exception/
│   └── GlobalExceptionHandler.java  # Tratamento centralizado de erros
├── model/
│   ├── Vehicle.java              # Entidade veículo
│   ├── Specification.java        # Entidade especificação técnica
│   ├── SearchHistory.java        # Entidade histórico de buscas
│   └── AuditLog.java             # Entidade log de auditoria
├── repository/
│   ├── VehicleRepository.java
│   ├── SpecificationRepository.java
│   ├── SearchHistoryRepository.java
│   └── AuditLogRepository.java
├── security/
│   ├── JwtUtil.java              # Geração e validação de tokens JWT
│   ├── JwtFilter.java            # Filtro de autenticação por token
│   ├── RateLimitFilter.java      # Limite de requisições por IP
│   ├── AuditLogFilter.java       # Registro de todos os requests
│   └── CryptoUtils.java          # Criptografia AES/GCM
└── service/
    ├── VehicleService.java       # Lógica de negócio de veículos
    └── SearchHistoryService.java # Lógica de histórico com criptografia

src/main/resources/
├── application.properties        # Configurações da aplicação
└── db/migration/
    ├── V1__create_tables.sql     # Criação das tabelas principais
    └── V2__create_audit_logs.sql # Criação da tabela de auditoria
```

---

## Pré-requisitos

- Java 17+
- Maven 3.8+
- MySQL Workbench (recomendado)
  
---

## Configuração do Banco de Dados

### 1. Instalar o MySQL

Baixe e instale o MySQL Community Server em: https://dev.mysql.com/downloads/mysql/

Durante a instalação, defina uma senha para o usuário `root`. Guarde essa senha.

### 2. Instalar o MySQL Workbench

Baixe em: https://dev.mysql.com/downloads/workbench/

### 3. Conectar ao banco

1. Abra o MySQL Workbench
2. Clique em **+** ao lado de "MySQL Connections"
3. Preencha:
   - Connection Name: `autoinsight`
   - Hostname: `127.0.0.1`
   - Port: `3306`
   - Username: `root`
4. Clique em **Test Connection** e insira sua senha
5. Clique em **OK**

### 4. Criar o banco de dados

No MySQL Workbench, abra uma nova query e execute:

```sql
CREATE DATABASE autoinsight_db;
```

> O Flyway criará as tabelas automaticamente ao subir a aplicação.

---

## Variáveis de Ambiente

A aplicação usa variáveis de ambiente para proteger credenciais sensíveis. Configure as seguintes antes de rodar:

| Variável | Descrição | Padrão (dev) |
|---|---|---|
| `DB_URL` | URL do banco de dados | `jdbc:mysql://localhost:3306/autoinsight_db` |
| `DB_USERNAME` | Usuário do MySQL | `root` |
| `DB_PASSWORD` | Senha do MySQL | `root123` |
| `JWT_SECRET` | Chave secreta do JWT | definida no properties |
| `CRYPTO_KEY` | Chave AES de criptografia | definida no properties |
| `CORS_ALLOWED_ORIGINS` | Origens permitidas no CORS | `http://localhost:3000,http://localhost:8080` |

> Em desenvolvimento local, os valores padrão do `application.properties` são usados automaticamente.

---

## Como Rodar

### 1. Clonar o repositório

```bash
git clone https://github.com/AliAndrea1/Sprint-Soa-Ford.git
cd Sprint-Soa-Ford
```

### 2. Configurar o banco

Certifique-se de que o MySQL está rodando e o banco `autoinsight_db` foi criado.

Se necessário, edite as credenciais em `src/main/resources/application.properties`:

```properties
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:SUA_SENHA}
```

### 3. Rodar a aplicação

```bash
mvn spring-boot:run
```

## Endpoints da API

### Autenticação

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/api/auth/login` | Realiza login e retorna token JWT | Público |

**Exemplo de login:**
```json
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

**Usuários disponíveis:**
| Usuário | Senha | Papel |
|---|---|---|
| admin | admin123 | ADMIN |
| analyst | analyst123 | ANALYST |

---

### Veículos

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| GET | `/api/vehicles` | Lista todos os veículos | ADMIN, ANALYST |
| GET | `/api/vehicles/{id}` | Busca veículo por ID | ADMIN, ANALYST |
| GET | `/api/vehicles/search` | Busca por marca/modelo/versão | ADMIN, ANALYST |
| GET | `/api/vehicles/brand/{brand}` | Lista por marca | ADMIN, ANALYST |
| POST | `/api/vehicles` | Cadastra novo veículo | ADMIN |
| PUT | `/api/vehicles/{id}` | Atualiza veículo | ADMIN |
| DELETE | `/api/vehicles/{id}` | Remove veículo | ADMIN |

**Exemplo de busca:**
```
GET /api/vehicles/search?brand=Ford&model=Ranger&version=Raptor
```

---

### Histórico de Buscas

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| GET | `/api/history` | Lista histórico do usuário | ADMIN, ANALYST |
| DELETE | `/api/history/{id}` | Remove registro do histórico | ADMIN |

---

### Auditoria

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| GET | `/api/audit` | Lista todos os logs | ADMIN |
| GET | `/api/audit/user/{username}` | Logs por usuário | ADMIN |

---

## Documentação Swagger

Com a aplicação rodando, acesse a documentação interativa:

```
http://localhost:8080/swagger-ui.html
```

Para testar endpoints protegidos no Swagger:
1. Faça login em `POST /api/auth/login`
2. Copie o token retornado
3. Clique em **Authorize** (🔒) no topo da página
4. Cole o token e clique em **Authorize**
