# Projeto Foursales

Este projeto é uma aplicação Spring Boot que utiliza diversas tecnologias para fornecer funcionalidades robustas e escaláveis. Abaixo, você encontrará uma visão geral da estrutura do projeto, as tecnologias utilizadas e orientações para executar o projeto.

## Estrutura do Projeto

O projeto está organizado em diferentes camadas, incluindo:

- **Camada de Web**: Controladores REST para gerenciar requisições HTTP.
- **Camada de Serviço**: Lógica de negócios e manipulação de dados.
- **Camada de Repositório**: Interação com o banco de dados usando Spring Data JPA.


## Arquitetura Hexagonal

O projeto segue a arquitetura hexagonal, também conhecida como Ports and Adapters, que promove a separação de preocupações e facilita a manutenção e evolução do sistema. Abaixo está a estrutura de pacotes e suas descrições:

- **br.com.foursales.app**: Pacote raiz da aplicação.
  - **application**: Contém os casos de uso e orquestração de fluxos de aplicação.
  - **domain**: Contém as entidades de domínio e interfaces de repositórios.
  - **infrastructure**: Implementações de interfaces de repositórios, configurações e integrações com tecnologias externas.
    - **web**: Controladores REST e configuração de segurança.
    - **persistence**: Implementações de repositórios usando Spring Data JPA.
    - **messaging**: Configurações e implementações para integração com Kafka.
    - **elasticsearch**: Configurações e implementações para integração com Elasticsearch.
  - **config**: Configurações gerais da aplicação, como beans e propriedades.


## Tecnologias Utilizadas

- **Java 17**: A aplicação é construída usando Java 17. Certifique-se de ter o JDK 17 instalado.
- **Spring Boot 3.4.3**: Framework principal para criar a aplicação.
- **Spring Data JPA**: Para interagir com o banco de dados MySQL.
- **Spring Security**: Para autenticação e autorização.
- **Liquibase**: Para gerenciamento de migrações de banco de dados.
- **Spring Data Elasticsearch**: Para integração com Elasticsearch.
- **Spring for Apache Kafka**: Para mensageria com Kafka.
- **Spring Data Redis**: Para caching e mensageria com Redis.
- **Docker**: Utilizado para containerização dos serviços.

## Configuração do Ambiente

### Pré-requisitos

- **JDK 17**: Certifique-se de que o JDK 17 está instalado e configurado no seu PATH.
- **Docker**: Necessário para executar os serviços de suporte como Kafka, Elasticsearch e Redis.
- **Gradle**: Utilizado para construir o projeto. Acesse a [documentação oficial do Gradle](https://docs.gradle.org) para mais informações.

### Executando o Projeto

1. **Clone o repositório**: 
   ```bash
   git clone <url-do-repositorio>
   cd <nome-do-repositorio>
   ```

2. **Construir o projeto**:
   Execute o seguinte comando para construir o projeto usando Gradle:
   ```bash
   ./gradlew build
   ```

3. **Executar o Docker Compose**:
   Inicie os serviços necessários com Docker Compose:
   ```bash
   docker-compose up
   ```

4. **Iniciar a aplicação**:
   Execute a aplicação Spring Boot:
   ```bash
   ./gradlew bootRun
   ```

5. **Acessar a aplicação**:
   A aplicação estará disponível em `http://localhost:8080/api/foursales`.

### Configurações Adicionais

- **Configurações de Banco de Dados**: As configurações de banco de dados estão definidas no arquivo `application.yaml`.
- **Perfis de Aplicação**: O projeto suporta diferentes perfis de execução (`dev`, `test`, `prod`) configurados no `application.yaml`.

Para mais informações sobre como utilizar as funcionalidades do projeto, consulte a documentação oficial das tecnologias listadas acima.
