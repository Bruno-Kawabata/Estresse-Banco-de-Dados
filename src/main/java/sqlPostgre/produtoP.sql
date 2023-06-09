-- Remoção das tabelas
DROP TABLE IF EXISTS carrinho;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS endereco;

----------------------

-- Criação da tabela "endereco"
CREATE TABLE endereco (
  id SERIAL PRIMARY KEY,
  endereco VARCHAR(255)
);

-- Criação da tabela "cliente"
CREATE TABLE cliente (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255),
  idade INT,
  idendereco INT,
  FOREIGN KEY (idendereco) REFERENCES endereco (id)
);

-- Adição da tabela "produto"
CREATE TABLE produto (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(255),
  precoUn NUMERIC(5,2),
  quantidade INT
);

-- Adição da tabela "carrinho"
CREATE TABLE carrinho (
  id SERIAL PRIMARY KEY,
  idcliente INT,
  idproduto INT,
  FOREIGN KEY (idcliente) REFERENCES cliente (id),
  FOREIGN KEY (idproduto) REFERENCES produto (id)
);
