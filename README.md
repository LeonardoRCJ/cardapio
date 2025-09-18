# Pastelaria Campo Grande - Aplicação Full-Stack de Pedidos

## Visão Geral

A Pastelaria Campo Grande é uma aplicação web full-stack para um cardápio digital, que permite aos clientes visualizar pratos, fazer pedidos e pagar online. A plataforma inclui uma área de gestão completa para administradores e uma interface de cliente moderna e reativa.

Este projeto foi desenvolvido com um backend robusto em **Java com Spring Boot** e um frontend interativo em **React com TypeScript e Vite**.

## Funcionalidades Principais

* **Para Clientes:**
    * Landing Page de apresentação.
    * Visualização do cardápio com filtros por categoria (entregáveis vs. não entregáveis).
    * Registo e Login de utilizadores com autenticação via JWT.
    * Carrinho de compras para adicionar e gerir produtos.
    * Checkout transparente com integração de pagamentos via Mercado Pago.
    * Página de sucesso do pedido e histórico de pedidos ("Meus Pedidos").

* **Para Administradores:**
    * Painel de administração protegido por `role` (perfil).
    * CRUD completo (Criar, Ler, Atualizar, Apagar) para os pratos do cardápio.
    * Modais de confirmação para ações destrutivas, melhorando a usabilidade.

## Tecnologias Utilizadas

### Backend

* **Java 17**
* **Spring Boot 3.4.3**
* **Spring Web, Data JPA, Spring Security**
* **PostgreSQL** como base de dados.
* **Lombok** para redução de boilerplate.
* **Java JWT** para geração e validação de tokens de autenticação.
* **SDK do Mercado Pago** для интеграции платежей.
* **Maven** para gestão de dependências.

### Frontend

* **React com TypeScript**
* **Vite** como ferramenta de build e servidor de desenvolvimento.
* **React Router DOM** para gestão de rotas.
* **Axios** para chamadas à API.
* **React Hot Toast** для уведомлений.
* **SDK React do Mercado Pago** для Прозрачного Checkout.
* **jwt-decode** para extrair informações do token no cliente.

## Como Executar o Projeto

Para executar a aplicação completa, precisa de iniciar o backend e o frontend separadamente.

### 1. Backend (API)

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-repositorio>
    ```

2.  **Navegue para a pasta do backend:**
    ```bash
    cd cardapio # ou o nome da sua pasta do backend
    ```

3.  **Configure as variáveis de ambiente:**
    No ficheiro `src/main/resources/application.properties`, preencha as seguintes variáveis:
    ```properties
    # Base de Dados (Supabase/PostgreSQL)
    spring.datasource.url={url_do_seu_banco_de_dados_com_pooler}
    spring.datasource.username={username}
    spring.datasource.password={password}

    # Segredo para o Token JWT
    api.security.token.secret={seu_segredo_para_jwt}

    # Credenciais do Mercado Pago (Backend)
    mercadopago.access_token={seu_access_token_de_teste_do_mp}
    ```

4.  **Execute a aplicação:**
    ```bash
    ./mvnw spring-boot:run
    ```
    O servidor backend estará a correr em `http://localhost:8080`.

### 2. Frontend

1.  **Navegue para a pasta do frontend:**
    ```bash
    # A partir da raiz do projeto
    cd cardapio-frontend # ou o nome da sua pasta do frontend
    ```

2.  **Instale as dependências:**
    ```bash
    npm install
    ```

3.  **Configure as variáveis de ambiente:**
    Crie um ficheiro `.env.local` na raiz da pasta do frontend e adicione a sua chave pública do Mercado Pago:
    ```bash
    # Ficheiro: .env.local
    VITE_MERCADOPAGO_PUBLIC_KEY="sua_public_key_de_teste_do_mp"
    ```

4.  **Execute a aplicação:**
    ```bash
    npm run dev
    ```
    O servidor frontend estará a correr em `http://localhost:5173`.

## Endpoints da API

* `POST /api/auth/login`: Autentica um utilizador e retorna um token JWT.
* `POST /api/auth/register`: Regista um novo utilizador.

* `GET /api/foods`: Retorna a lista de todos os pratos.
* `GET /api/foods/{id}`: Retorna um prato específico pelo seu ID.

* `POST /api/orders`: Cria um novo pedido (requer autenticação).
* `GET /api/orders/my-orders`: Retorna o histórico de pedidos do utilizador autenticado.

* `POST /api/payments/webhook`: Endpoint público para receber notificações do Mercado Pago.

* `POST /api/admin/foods`: Cria um novo prato (requer `role` ADMIN).
* `PUT /api/admin/foods/{id}`: Atualiza um prato existente (requer `role` ADMIN).
* `DELETE /api/admin/foods/{id}`: Apaga um prato (requer `role` ADMIN).

## Próximos Passos e Melhorias

* **Detalhes do Pedido:** Criar uma página que mostre os detalhes de um pedido específico do histórico.
* **Gestão de Estado (Backend):** Implementar uma lógica mais robusta no webhook para verificar o estado real do pagamento na API do Mercado Pago antes de atualizar o pedido.
* **Testes:** Adicionar testes unitários e de integração para garantir a estabilidade do código.
* **Deployment:** Preparar a aplicação para ser implementada num ambiente de produção (ex: Vercel para o frontend e Railway/Heroku para o backend).
