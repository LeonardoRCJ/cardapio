# Cardapio API

## Visão Geral

Esta é uma API para um cardápio digital, desenvolvida com Java e Spring Boot. A API permite o gerenciamento de pratos (foods) e autenticação de usuários, com rotas específicas para administradores e usuários comuns.

## Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.4.3**
* **Spring Web:** Para a criação de APIs REST.
* **Spring Data JPA:** Para o acesso e persistência de dados.
* **Spring Security:** Para a autenticação e autorização.
* **PostgreSQL:** Como banco de dados.
* **Lombok:** Para reduzir o código boilerplate.
* **JWT (Java JWT):** Para a geração e validação de tokens de autenticação.
* **Maven:** Para o gerenciamento de dependências e do projeto.

## Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-repositorio>
    ```

2.  **Configure o banco de dados:**
    * No arquivo `src/main/resources/application.properties`, configure a URL do seu banco de dados PostgreSQL, o usuário e a senha.
        ```properties
        spring.datasource.url={url}
        spring.datasource.username={username}
        spring.datasource.password={password}
        ```

3.  **Configure o segredo do token:**
    * Ainda no arquivo `src/main/resources/application.properties`, defina um segredo para a geração de tokens JWT.
        ```properties
        api.security.token.secret={secret}
        ```

4.  **Execute a aplicação:**
    * Utilize o Maven para compilar e executar o projeto:
        ```bash
        ./mvnw spring-boot:run
        ```

## Endpoints da API

### Autenticação

* **`POST /auth/login`**
    * Realiza o login de um usuário.
    * **Request Body:**
        ```json
        {
            "email": "user@example.com",
            "password": "password"
        }
        ```
    * **Response:**
        ```json
        {
            "token": "seu-token-jwt"
        }
        ```

* **`POST /auth/register`**
    * Registra um novo usuário.
    * **Request Body:**
        ```json
        {
            "fullname": "Nome Completo",
            "cpf": "123.456.789-00",
            "phone": "11999999999",
            "password": "password",
            "birthdate": "2000-01-01",
            "email": "user@example.com",
            "role": "USER"
        }
        ```

### Foods (Pratos)

* **`GET /foods`**
    * Retorna a lista de todos os pratos. Acessível a todos.

* **`GET /foods/{id}`**
    * Retorna um prato específico pelo seu ID. Acessível a todos.

### Admin/Foods (Gerenciamento de Pratos)

* **`POST /admin/foods`**
    * Cria um novo prato. Requer autenticação de administrador.
    * **Request Body:**
        ```json
        {
            "title": "Novo Prato",
            "image": "url-da-imagem",
            "description": "Descrição do prato",
            "price": 25.50
        }
        ```

* **`PUT /admin/foods/{id}`**
    * Atualiza um prato existente. Requer autenticação de administrador.
    * **Request Body:**
        ```json
        {
            "title": "Prato Atualizado",
            "image": "url-da-imagem-atualizada",
            "description": "Descrição atualizada",
            "price": 30.00
        }
        ```

* **`DELETE /admin/foods/{id}`**
    * Deleta um prato pelo seu ID. Requer autenticação de administrador.

## Melhorias a serem implementadas

* Usuário poderá fazer um pedido com uma lista de itens.
* Conectar com uma API de pagamentos.
* Frontend para interação.
