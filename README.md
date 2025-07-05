# Sistema de Controle de EPIs (Equipamentos de Proteção Individual)

Projeto acadêmico para a Situação de Aprendizagem (SA) da Unidade Curricular de **Desenvolvimento de Sistemas**, do curso Técnico em Desenvolvimento de Sistemas.

## Visão Geral do Projeto

Este repositório contém o código-fonte de um sistema web para gerenciamento de EPIs, desenvolvido para atender à necessidade de uma empresa de construção civil de aprimorar o controle sobre a distribuição, uso e devolução dos equipamentos de proteção dos seus colaboradores.

O sistema foi construído utilizando **Java com Spring Boot** no backend e **Thymeleaf** para a renderização das telas no frontend, com autenticação e autorização gerenciadas pelo **Spring Security**.

**Link para acesso (versão base):** `http://localhost:8080/login`

---

## Contexto Acadêmico

Este projeto foi desenvolvido como requisito avaliativo do terceiro semestre do curso.

* **Instituição:** SENAI
* **Curso:** Técnico em Desenvolvimento de Sistemas
* **Equipe de Desenvolvimento:**
   * Gianluca Grignani
   * Henrique Loos
   * Sanatiel Murceski
* **Professor Orientador:** Jorge Antonio Golle

---

## O Desafio: Situação de Aprendizagem (SA)

A proposta do projeto partiu do seguinte cenário:

> Uma empresa de construção civil identificou a necessidade de aprimorar a gestão dos Equipamentos de Proteção Individual (EPIs) utilizados pelos colaboradores. Muitos funcionários frequentemente esquecem de utilizar os EPIs por deixá-los em casa ou por não retirá-los no setor de Saúde e Segurança do Trabalho.
>
> O desafio proposto foi aplicar os conhecimentos adquiridos para desenvolver um software que permita o controle eficiente do ciclo de vida dos EPIs na empresa, desde o cadastro até a geração de relatórios, garantindo um ambiente de trabalho mais seguro e em conformidade com as normas regulamentadoras.

---

## Funcionalidades e Critérios de Avaliação Atendidos

O sistema foi desenvolvido para cumprir os seguintes requisitos e critérios de avaliação da SA:

-   [x] **Cadastro de EPIs:** Implementação completa do CRUD (Create, Read, Update, Delete) de equipamentos.
-   [x] **Cadastro de Usuários e Login:** Sistema de autenticação funcional com perfis distintos (Administrador e Colaborador) utilizando Spring Security.
-   [x] **Cadastro de Colaboradores:** Gerenciamento dos dados dos funcionários da empresa.
-   [x] **Controle de Estoque:** Gestão da quantidade de EPIs disponíveis e em uso.
-   [x] **Controle de Empréstimos:** Sistema funcional para registrar a entrega de EPIs aos colaboradores, seguindo boas práticas de usabilidade.
-   [x] **Geração de Relatórios:** O sistema possui capacidade de gerar relatórios (Ex: equipamentos emprestados, histórico por colaborador, etc.).
-   [x] **Versionamento de Código:** Todo o desenvolvimento do projeto foi versionado utilizando **Git**, com o repositório hospedado no GitHub.
-   [x] **Disponibilização e Execução:** O código-fonte está organizado e pronto para ser executado em um ambiente local, conforme as instruções abaixo.

---

## Tecnologias Utilizadas

-   **Backend:** Java 17, Spring Boot 3.2.2
-   **Segurança:** Spring Security
-   **Frontend:** Thymeleaf
-   **Banco de Dados:** MySQL (JDBC)
-   **Build Tool:** Maven
-   **Criptografia:** BCrypt para hash de senhas

---

## Como Rodar Localmente

### 1. Pré-requisitos

-   JDK 17 ou superior
-   Maven 3.6+
-   Uma instância de MySQL rodando localmente ou em um container.

### 2. Configuração do Banco de Dados

Antes de iniciar a aplicação, você precisa criar o banco de dados e as tabelas. Execute o script SQL abaixo no seu cliente MySQL.

```sql
-- ###############################################################
-- # SCRIPT SQL PARA CRIAÇÃO DO BANCO DE DADOS E TABELAS #
-- ###############################################################

-- 1. Crie o Banco de Dados (se ainda não existir)
CREATE DATABASE IF NOT EXISTS episdb;

-- 2. Use o Banco de Dados
USE episdb;

-- 3. Criação da Tabela 'usuarios'
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,       -- Tamanho para senhas criptografadas (hash)
    perfil ENUM('ADMINISTRADOR', 'COLABORADOR') NOT NULL DEFAULT 'COLABORADOR'
);

-- 4. Criação da Tabela 'epis'
CREATE TABLE IF NOT EXISTS epis (
    id_epi INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade INT(11) NOT NULL
);

-- 5. Criação da Tabela 'emprestimo'
CREATE TABLE IF NOT EXISTS emprestimo (
    id_emprestimo INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT(11) NOT NULL,
    id_epi INT(11) NOT NULL,
    data_retirada DATETIME NOT NULL,
    data_prevista_devolucao DATETIME NOT NULL,
    confirmacao_retirada TINYINT(1) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    FOREIGN KEY (id_epi) REFERENCES epis(id_epi)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- 6. Criação da Tabela 'devolucao'
CREATE TABLE IF NOT EXISTS devolucao (
    id_devolucao INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_emprestimo INT(11) NOT NULL,
    data_devolucao DATETIME NOT NULL,
    FOREIGN KEY (id_emprestimo) REFERENCES emprestimo(id_emprestimo)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- ###############################################################
-- # INSERÇÃO DE DADOS INICIAIS                                #
-- ###############################################################

-- 7. Inserir um Usuário Administrador Padrão
-- A senha 'Senha@123' foi previamente hashed usando bcrypt.
-- O hash resultante é: $2a$10$O6lcawYgBk/.l7VDAf59/ePGj/eiF/OEgQBUsP2DuTGCmKo7AqAoO
-- Esta senha deve ser usada para o login do usuário 'admin.padrao@epis.com'.
INSERT INTO usuarios (nome, email, senha, perfil) VALUES
('Admin Padrao do Sistema', 'admin.padrao@epis.com', '$2a$10$O6lcawYgBk/.l7VDAf59/ePGj/eiF/OEgQBUsP2DuTGCmKo7AqAoO', 'ADMINISTRADOR');

-- ###############################################################
-- # FIM DO SCRIPT                                             #
-- ###############################################################
```

### 3. Configuração da Aplicação

Abra o arquivo `src/main/resources/application.properties`.
Altere as propriedades `spring.datasource.url`, `spring.datasource.username` e `spring.datasource.password` com as credenciais do seu banco de dados MySQL.

**Exemplo:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/episdb
spring.datasource.username=seu_usuario_mysql
spring.datasource.password=sua_senha_mysql
```
### 4. Execução

1. Clone este repositório:
```best
git clone https://github.com/GianGrig/sistema-gestao-epis-vfinal.git
cd sistema-gestao-epis-vfinal
```

2. Execute a aplicação usando o Maven:
```best
mvn spring-boot:run
```

3. Acesse http://localhost:8080 no seu navegador.

