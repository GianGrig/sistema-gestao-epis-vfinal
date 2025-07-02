# Sistema Gestão EPIs - Versão Final

http://localhost:8080/login

Sistema web para gerenciamento de EPIs (Equipamentos de Proteção Individual), empréstimos e devoluções, desenvolvido com **Spring Boot**, **Spring Security** e **Thymeleaf**.

---

## Funcionalidades

- Cadastro, edição, listagem e exclusão de EPIs.
- Gerenciamento de usuários com perfis Administrador e Colaborador.
- Registro e controle de empréstimos e devoluções de EPIs.
- Autenticação e autorização via Spring Security.
- Diferenciação de telas e permissões conforme perfil do usuário.
- Interface web responsiva usando Thymeleaf.

---

## Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.x
- Spring Security
- Thymeleaf
- Maven
- Banco de dados relacional (configurar conforme seu ambiente)
- BCrypt para hash de senhas

---

## Como Rodar Localmente

### Pré-requisitos

- JDK 17 ou superior instalado
- Maven instalado
- Banco de dados configurado e rodando (MySQL, PostgreSQL, H2, etc)
- Configurar o arquivo `application.properties` em `src/main/resources` com as credenciais do banco

### Passos

1. Clone este repositório:

   ```bash
   git clone https://github.com/seuusuario/sistema-gestao-epis.git
   cd sistema-gestao-epis
