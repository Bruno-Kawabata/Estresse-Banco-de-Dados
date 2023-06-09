SELECT * FROM PRODUTO;

---------------------------------

-- Remoção das tabelas e sequências
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE endereco CASCADE CONSTRAINTS';
  EXECUTE IMMEDIATE 'DROP TABLE cliente CASCADE CONSTRAINTS';
  EXECUTE IMMEDIATE 'DROP TABLE produto CASCADE CONSTRAINTS';
  EXECUTE IMMEDIATE 'DROP TABLE carrinho CASCADE CONSTRAINTS';
  EXECUTE IMMEDIATE 'DROP SEQUENCE seq_endereco';
  EXECUTE IMMEDIATE 'DROP SEQUENCE seq_cliente';
  EXECUTE IMMEDIATE 'DROP SEQUENCE seq_produto';
  EXECUTE IMMEDIATE 'DROP SEQUENCE seq_carrinho';
  EXECUTE IMMEDIATE 'DROP TRIGGER endereco_trigger'; 
  EXECUTE IMMEDIATE 'DROP TRIGGER cliente_trigger';
  EXECUTE IMMEDIATE 'DROP TRIGGER produto_trigger';
  EXECUTE IMMEDIATE 'DROP TRIGGER carrinho_trigger';
END;

------------------

-- Criação do banco de dados e sequências
DECLARE
  table_exists NUMBER;
BEGIN
	SELECT COUNT(*) INTO table_exists FROM user_tables WHERE table_name = 'ENDERECO';
	IF table_exists > 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE endereco CASCADE CONSTRAINTS';
		EXECUTE IMMEDIATE 'DROP SEQUENCE seq_endereco';
	END IF;
	SELECT COUNT(*) INTO table_exists FROM user_tables WHERE table_name = 'CLIENTE';
	IF table_exists > 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE cliente CASCADE CONSTRAINTS';
		EXECUTE IMMEDIATE 'DROP SEQUENCE seq_cliente';
	END IF;
	SELECT COUNT(*) INTO table_exists FROM user_tables WHERE table_name = 'PRODUTO';
	IF table_exists > 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE produto CASCADE CONSTRAINTS';
		EXECUTE IMMEDIATE 'DROP SEQUENCE seq_produto';
	END IF;
	
	SELECT COUNT(*) INTO table_exists FROM user_tables WHERE table_name = 'CARRINHO';
	IF table_exists > 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE carrinho CASCADE CONSTRAINTS';
		EXECUTE IMMEDIATE 'DROP SEQUENCE seq_carrinho';
	END IF;
	EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_endereco START WITH 1 INCREMENT BY 1';
	EXECUTE IMMEDIATE 'CREATE TABLE endereco (
		id NUMBER,
		endereco VARCHAR2(255),
		CONSTRAINT endereco_pk PRIMARY KEY (id)
		)';
	EXECUTE IMMEDIATE 'CREATE TRIGGER endereco_trigger
		BEFORE INSERT ON endereco
		FOR EACH ROW
		BEGIN
		    :NEW.id := seq_endereco.NEXTVAL;
		END;';
	EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_cliente START WITH 1 INCREMENT BY 1';
	EXECUTE IMMEDIATE 'CREATE TABLE cliente (
		id NUMBER,
		nome VARCHAR2(255),
		idade NUMBER,
		idendereco NUMBER,
		CONSTRAINT cliente_pk PRIMARY KEY (id),
		CONSTRAINT cliente_fk_endereco FOREIGN KEY (idendereco) REFERENCES endereco (id)
		)';
	EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER cliente_trigger
		BEFORE INSERT ON cliente
		FOR EACH ROW
		BEGIN
		    :NEW.id := seq_cliente.NEXTVAL;
		END;';
	EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_produto START WITH 1 INCREMENT BY 1';
	EXECUTE IMMEDIATE 'CREATE TABLE produto (
		id NUMBER,
		nome VARCHAR2(255),
		precoun NUMBER(5,2),
		quantidade NUMBER,
		CONSTRAINT produto_pk PRIMARY KEY (id)
		)';
	EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER produto_trigger
		BEFORE INSERT ON produto
		FOR EACH ROW
		BEGIN
		    :NEW.id := seq_produto.NEXTVAL;
		END;';
	EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_carrinho START WITH 1 INCREMENT BY 1';
	EXECUTE IMMEDIATE 'CREATE TABLE carrinho (
		id NUMBER,
		idcliente NUMBER,
		idproduto NUMBER,
		CONSTRAINT carrinho_pk PRIMARY KEY (id),
		CONSTRAINT carrinho_fk_cliente FOREIGN KEY (idcliente) REFERENCES cliente (id),
		CONSTRAINT carrinho_fk_produto FOREIGN KEY (idproduto) REFERENCES produto (id)
		)';
	EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER carrinho_trigger
		BEFORE INSERT ON carrinho
		FOR EACH ROW
		BEGIN
		    :NEW.id := seq_carrinho.NEXTVAL;
		END;';
END;

------------------------------------------------
--Cria Banco sem Trigger

DECLARE
  table_exists NUMBER;
BEGIN
	EXECUTE IMMEDIATE 'CREATE TABLE endereco (
		id NUMBER,
		endereco VARCHAR2(255),
		CONSTRAINT endereco_pk PRIMARY KEY (id)
		)';
	EXECUTE IMMEDIATE 'CREATE TABLE cliente (
		id NUMBER,
		nome VARCHAR2(255),
		idade NUMBER,
		idendereco NUMBER,
		CONSTRAINT cliente_pk PRIMARY KEY (id),
		CONSTRAINT cliente_fk_endereco FOREIGN KEY (idendereco) REFERENCES endereco (id)
		)';
	EXECUTE IMMEDIATE 'CREATE TABLE produto (
		id NUMBER,
		nome VARCHAR2(255),
		precoun NUMBER(5,2),
		quantidade NUMBER,
		precototal NUMBER(5,2),
		CONSTRAINT produto_pk PRIMARY KEY (id)
		)';
	EXECUTE IMMEDIATE 'CREATE TABLE carrinho (
		id NUMBER,
		idcliente NUMBER,
		idproduto NUMBER,
		CONSTRAINT carrinho_pk PRIMARY KEY (id),
		CONSTRAINT carrinho_fk_cliente FOREIGN KEY (idcliente) REFERENCES cliente (id),
		CONSTRAINT carrinho_fk_produto FOREIGN KEY (idproduto) REFERENCES produto (id)
		)';
END;