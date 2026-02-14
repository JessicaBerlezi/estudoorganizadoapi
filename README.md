# estudoorganizadoapi

Instruções para criação do ambiente

Passo 1 - Criação da base de dados
Executar os comandos abaixos, incluindo nome de usuario e senha

docker pull mysql:8.0
ocker run --name estudoorganizado-mysql-container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=estudoorganizado -e MYSQL_USER=user -e MYSQL_PASSWORD=senha -p 3306:3306 -v mysql_data:/var/lib/mysql mysql:8.0

Atualizar o application.properties com o usuário e senha da base 

Passo 2 - Criação das views

As views devem ser criadas manualmente antes da execução do código, pois o JPA acabará a criando em formato de tabela, quebrando as consultas;
Estão no caminho: br.pucrs.estudoorganizado.entity.view



Execução 
Antes de rodar o projeto spring, será necessário subir o conteiner do mysql e executar com o comando:
docker exec -it estudoorganizado-mysql-container mysql -u user -p

Outros comandos do mysql:
SHOW DATABASES;
USE estudoorganizado;
SHOW TABLES;


SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE study_cycle_item;
TRUNCATE TABLE study_cycle;
TRUNCATE TABLE study_record;
TRUNCATE TABLE topic;
TRUNCATE TABLE subject;
TRUNCATE TABLE review_control;
SET FOREIGN_KEY_CHECKS = 1;
