# Skillify
* Java 21
* Spring Boot 3.4.3

## Spring Dependencies

| DEPENDÊNCIA                  | FUNÇÃO                                                                 |
| ---------------------------- | ---------------------------------------------------------------------- |
| Spring Boot Starter Data JPA | Integração com JPA para persistência dos dados                         |
| Spring Boot Starter Web      | Criação de serviços REST e APIs                                        |
| MariaDB Java Client          | Conector para o banco de dados MariaDB (ambiente de produção/homologação)|
| Lombok                       | Redução de código boilerplate (getters, setters, etc.)                 |
| Spring Boot Starter Test     | Suporte para testes unitários e de integração                          |

**Observação:** Dependências adicionais poderão ser integradas conforme a evolução do projeto.

## Arquitetura

O Skillify adota uma arquitetura monolítica baseada no padrão [MVC](https://pt.wikipedia.org/wiki/MVC), facilitando o desenvolvimento, a manutenção e a escalabilidade da aplicação.

## Práticas de Desenvolvimento

* Estruturação clara das camadas da aplicação:
    * **Entidades:** Representam os dados persistidos no banco.
    * **Request:** Objetos que representam os dados recebidos nas requisições.
    * **Response:** Objetos que estruturam as respostas enviadas pela API.
    * **Serviços:** Contêm a lógica de negócio.
    * **Repositórios:** Responsáveis pelo acesso aos dados utilizando JPA.

## Configuração do Projeto

- **Java:** Versão 21.
- **Spring Boot:** Versão 3.4.3.
- **Gerenciamento de Dependências:** Maven.
- **Banco de Dados:** MariaDB 
- **Plugins de Build:** Maven Compiler Plugin (configurado para utilizar Lombok) e Spring Boot Maven Plugin.

## Instalação e Execução

1. **Clone o repositório:**

    ```bash
    git clone <URL_DO_REPOSITORIO>
    cd skillify
    ```

2. **Construa o projeto:**

    ```bash
    mvn clean install
    ```

3. **Execute a aplicação:**

    ```bash
    mvn spring-boot:run
    ```

## Configuração do Banco de Dados

Defina as configurações de conexão no arquivo `src/main/resources/application.properties` ou `application.yml`. Exemplo:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
