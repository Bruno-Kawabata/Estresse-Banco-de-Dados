package javafakerPostgre;

import com.github.javafaker.Faker;

import java.sql.*;
import java.util.*;

public class ExtressaBanco {
	static Faker faker = new Faker(new Locale("pt-BR"));
	static String urlOracle = "jdbc:oracle:thin:@localhost:1521:xe";
	static String userOracle = "BRUNO";
	static String passwordOracle = "root";
	static String urlPostgre = "jdbc:postgresql://localhost:5432/";
	static String userPostgre = "postgres";
	static String passwordPostgre = "1234";

	public static void main(String[] args) throws SQLException {
//		CriaBancoPostgre();
		CriaBancoOracle();

		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			public void run() {
				Integer quantidadeInsert = 100;
				long startTime = System.currentTimeMillis();

				try {
//					updatePostgre(quantidadeInsert);
//					updateOracle(quantidadeInsert);
					
//					insertPostgre(quantidadeInsert);
					for (int i = 0; i < 500; i++) {
						System.out.println("ciclo " + i);
						insertOracle(quantidadeInsert);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				long endTime = System.currentTimeMillis();
				long tempo = endTime - startTime;
				double tempoSegundos = tempo / 1000.0;
				timer.cancel();
				System.out.println("Duração: " + String.format("%.2f", tempoSegundos) + " segundos");

			}
		};
		timer.schedule(task, 0, 1000);
	}

	public static Integer valorRand(Integer min, Integer max) {
		Random rand = new Random();
		Integer randomNum = min + rand.nextInt((max - min) + 1);
		return randomNum;
	}

	public static void insertPostgre(Integer qtde) throws SQLException {
		System.out.println("Iniciando inserção PostgreSQL...");
		try (Connection conn = DriverManager.getConnection(urlPostgre, userPostgre, passwordPostgre)) {
			String script = montaInsertPostgre(qtde);
			PreparedStatement ps = conn.prepareStatement(script);
			ps.executeUpdate();
			ps.close();
		}
		System.out.println("-----------------");
		System.out.println(qtde + " produtos foram inseridos no banco PostgreSQL");
		System.out.println("-----------------");
	}

	public static void insertOracle(Integer qtde) throws SQLException {
		System.out.println("Iniciando inserção Oracle...");
		try (Connection conn = DriverManager.getConnection(urlOracle, userOracle, passwordOracle)) {
			String script = montaInsertOracle(qtde);
			PreparedStatement ps = conn.prepareStatement(script);
			ps.executeUpdate();
			ps.close();
		}
		System.out.println("-----------------");
		System.out.println(qtde + " produtos foram inseridos no banco Oracle");
		System.out.println("-----------------");
	}

	public static void updatePostgre(Integer qtde) throws SQLException {
		System.out.println("Iniciando Update no PostgreSQL...");
		try (Connection conn = DriverManager.getConnection(urlPostgre, userPostgre, passwordPostgre)) {
			String script = montaUpdatePostgre(qtde);
			PreparedStatement ps = conn.prepareStatement(script);
			ps.executeUpdate();
			ps.close();
		}
		System.out.println("-----------------");
		System.out.println(qtde + " produtos foram atualizados no banco PostgreSQL");
		System.out.println("-----------------");
	}

	public static void updateOracle(Integer qtde) throws SQLException {
		System.out.println("Iniciando Update no Oracle...");
		try (Connection conn = DriverManager.getConnection(urlOracle, userOracle, passwordOracle)) {
			String script = montaUpdateOracle(qtde);
			PreparedStatement ps = conn.prepareStatement(script);
			ps.executeUpdate();
			ps.close();
		}
		System.out.println("-----------------");
		System.out.println(qtde + " produtos foram atualizados no banco Oracle");
		System.out.println("-----------------");
	}

	public static String montaUpdatePostgre (Integer qtde) {
		StringBuilder script = new StringBuilder();

		for (int i = 1; i <= qtde; i++) {
			script.append("UPDATE produto SET nome = '" + faker.food().ingredient() + "', precoun = " + 
					Double.valueOf(valorRand(1, 15) + Double.valueOf(valorRand(1, 99))/100).toString() + ", quantidade = " + 
					Double.valueOf(valorRand(1, 50)) + " WHERE id = " + i +";");
			
			script.append("UPDATE endereco SET endereco ='" + faker.address().streetAddress() + "' WHERE id = " + i +";");
			
			script.append("UPDATE cliente SET nome = '" + faker.name().fullName() + "', idade = " + valorRand(18,70) + " WHERE id = " + i +";");
		}

		return script.toString();
	}

	public static String montaUpdateOracle (Integer qtde) {
		StringBuilder script = new StringBuilder();
		
		script.append("BEGIN ");
		for (int i = 1; i <= qtde; i++) {
			script.append("UPDATE PRODUTO SET nome = '" + faker.food().ingredient() + "', precoun = " + 
				Double.valueOf(valorRand(1, 15) + Double.valueOf(valorRand(1, 99))/100).toString() + ", quantidade = " + 
				Double.valueOf(valorRand(1, 50)) + " WHERE id = " + i +"; ");
			
			script.append("UPDATE ENDERECO SET endereco ='" + faker.address().streetAddress() + "' WHERE id = " + i +"; ");
			
			script.append("UPDATE CLIENTE SET nome = '" + faker.name().fullName() + "', idade = " + valorRand(18,70) + " WHERE id = " + i +"; ");
		}
		script.append("COMMIT; END;");
		
		return script.toString();
	}

	public static String montaInsertPostgre(Integer qtde) {
		StringBuilder script = new StringBuilder();

		//insere valores na tabela produto
		script.append("INSERT INTO produto (nome, precoun, quantidade) VALUES ");
		for (int i = 1; i <= qtde; i++) {
			Double precoun = Double.valueOf(valorRand(1, 15) + Double.valueOf(valorRand(1, 99))/100);
			Double qtdeProd = Double.valueOf(valorRand(1, 50));

			script.append("('" + faker.food().fruit() + "', " + precoun.toString() + ", " + qtdeProd.toString() + ")");
			if (i < qtde) {
				script.append(", ");
			}
		}
		script.append("; ");

		//insere valores na tabela produto
		script.append("INSERT INTO endereco (endereco) VALUES ");
		for (int i = 1; i <= qtde; i++) {
			script.append("('" + faker.address().streetAddress() + "')");
			if (i < qtde) {
				script.append(", ");
			}
		}
		script.append("; ");

		script.append("INSERT INTO cliente (nome, idade, idendereco) VALUES ");
		for (int i = 1; i <= qtde; i++) {
			script.append("('" + faker.name().fullName() + "', " + valorRand(18, 70) + ", " + valorRand(1, qtde) + ")");
			if (i < qtde) {
				script.append(", ");
			}
		}
		script.append("; ");

		script.append("INSERT INTO carrinho (idcliente, idproduto) VALUES ");
		for (int i = 1; i <= qtde; i++) {
			script.append("(" + valorRand(1,qtde) + ", " + valorRand(1,qtde) + ")");
			if (i < qtde) {
				script.append(", ");
			}
		}
		script.append("; ");

		return script.toString();
	}

	public static String montaInsertOracle(Integer qtde) {
		StringBuilder script = new StringBuilder();
		script.append("INSERT ALL ");

		for (int i = 1; i <= qtde; i++) {
			Double precoun = Double.valueOf(valorRand(1, 15) + Double.valueOf(valorRand(1, 99))/100);
			Double qtdeProd = Double.valueOf(valorRand(1, 50));
			script.append("INTO PRODUTO (nome, precoun, quantidade) VALUES ('" + faker.food().fruit() + "', " + 
				precoun.toString() + ", " + qtdeProd.toString() + ") ");
		}

		for (int i = 1; i <= qtde; i++) {
			script.append("INTO ENDERECO (endereco) VALUES ('" + faker.address().streetAddress() + "') ");
		}

		for (int i = 1; i <= qtde; i++) {
			script.append("INTO CLIENTE (nome, idade, idendereco) VALUES ('" + faker.name().fullName() + "', " + 
				valorRand(18, 70).toString() + ", " + valorRand(1, qtde).toString() + ") ");
		}

		for (int i = 1; i <= qtde; i++) {
			script.append("INTO CARRINHO (idcliente, idproduto) VALUES (" + valorRand(1, qtde).toString() + ", " + 
				valorRand(1, qtde).toString() + ") ");
		}

		script.append("SELECT * FROM DUAL");
		return script.toString();
	}

	public static void CriaBancoOracle() throws SQLException {
		try (Connection conn = DriverManager.getConnection(urlOracle, userOracle, passwordOracle)) {
			StringBuilder script = new StringBuilder();
			script.append("DECLARE"
					+ "  table_exists NUMBER;"
					+ "BEGIN"
					+ "	SELECT COUNT(*) INTO table_exists FROM user_tables WHERE table_name = 'ENDERECO';"
					+ "	IF table_exists > 0 THEN"
					+ "		EXECUTE IMMEDIATE 'DROP TABLE endereco CASCADE CONSTRAINTS';"
					+ "		EXECUTE IMMEDIATE 'DROP SEQUENCE seq_endereco';"
					+ "	END IF;"
					+ "	SELECT COUNT(*) INTO table_exists FROM user_tables WHERE table_name = 'CLIENTE';"
					+ "	IF table_exists > 0 THEN"
					+ "		EXECUTE IMMEDIATE 'DROP TABLE cliente CASCADE CONSTRAINTS';"
					+ "		EXECUTE IMMEDIATE 'DROP SEQUENCE seq_cliente';"
					+ "	END IF;"
					+ "	SELECT COUNT(*) INTO table_exists FROM user_tables WHERE table_name = 'PRODUTO';"
					+ "	IF table_exists > 0 THEN"
					+ "		EXECUTE IMMEDIATE 'DROP TABLE produto CASCADE CONSTRAINTS';"
					+ "		EXECUTE IMMEDIATE 'DROP SEQUENCE seq_produto';"
					+ "	END IF;"
					+ "	"
					+ "	SELECT COUNT(*) INTO table_exists FROM user_tables WHERE table_name = 'CARRINHO';"
					+ "	IF table_exists > 0 THEN"
					+ "		EXECUTE IMMEDIATE 'DROP TABLE carrinho CASCADE CONSTRAINTS';"
					+ "		EXECUTE IMMEDIATE 'DROP SEQUENCE seq_carrinho';"
					+ "	END IF;"
					+ "	EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_endereco START WITH 1 INCREMENT BY 1';"
					+ "	EXECUTE IMMEDIATE 'CREATE TABLE endereco ("
					+ "		id NUMBER,"
					+ "		endereco VARCHAR2(255),"
					+ "		CONSTRAINT endereco_pk PRIMARY KEY (id)"
					+ "		)';"
					+ "	EXECUTE IMMEDIATE 'CREATE TRIGGER endereco_trigger"
					+ "		BEFORE INSERT ON endereco"
					+ "		FOR EACH ROW"
					+ "		BEGIN"
					+ "		    :NEW.id := seq_endereco.NEXTVAL;"
					+ "		END;';"
					+ "	EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_cliente START WITH 1 INCREMENT BY 1';"
					+ "	EXECUTE IMMEDIATE 'CREATE TABLE cliente ("
					+ "		id NUMBER,"
					+ "		nome VARCHAR2(255),"
					+ "		idade NUMBER,"
					+ "		idendereco NUMBER,"
					+ "		CONSTRAINT cliente_pk PRIMARY KEY (id),"
					+ "		CONSTRAINT cliente_fk_endereco FOREIGN KEY (idendereco) REFERENCES endereco (id)"
					+ "		)';"
					+ "	EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER cliente_trigger"
					+ "		BEFORE INSERT ON cliente"
					+ "		FOR EACH ROW"
					+ "		BEGIN"
					+ "		    :NEW.id := seq_cliente.NEXTVAL;"
					+ "		END;';"
					+ "	EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_produto START WITH 1 INCREMENT BY 1';"
					+ "	EXECUTE IMMEDIATE 'CREATE TABLE produto ("
					+ "		id NUMBER,"
					+ "		nome VARCHAR2(255),"
					+ "		precoun NUMBER(5,2),"
					+ "		quantidade NUMBER,"
					+ "		CONSTRAINT produto_pk PRIMARY KEY (id)"
					+ "		)';"
					+ "	EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER produto_trigger"
					+ "		BEFORE INSERT ON produto"
					+ "		FOR EACH ROW"
					+ "		BEGIN"
					+ "		    :NEW.id := seq_produto.NEXTVAL;"
					+ "		END;';"
					+ "	EXECUTE IMMEDIATE 'CREATE SEQUENCE seq_carrinho START WITH 1 INCREMENT BY 1';"
					+ "	EXECUTE IMMEDIATE 'CREATE TABLE carrinho ("
					+ "		id NUMBER,"
					+ "		idcliente NUMBER,"
					+ "		idproduto NUMBER,"
					+ "		CONSTRAINT carrinho_pk PRIMARY KEY (id),"
					+ "		CONSTRAINT carrinho_fk_cliente FOREIGN KEY (idcliente) REFERENCES cliente (id),"
					+ "		CONSTRAINT carrinho_fk_produto FOREIGN KEY (idproduto) REFERENCES produto (id)"
					+ "		)';"
					+ "	EXECUTE IMMEDIATE 'CREATE OR REPLACE TRIGGER carrinho_trigger"
					+ "		BEFORE INSERT ON carrinho"
					+ "		FOR EACH ROW"
					+ "		BEGIN"
					+ "		    :NEW.id := seq_carrinho.NEXTVAL;"
					+ "		END;';"
					+ "END;");
			PreparedStatement ps = conn.prepareStatement(script.toString());
			System.out.println("Criando Banco de Dados Oracle");
			ps.executeUpdate();
			ps.close();
			System.out.println("Termino da Criação do Banco Oracle");
		}
	}

	public static void CriaBancoPostgre() throws SQLException {
		try (Connection conn = DriverManager.getConnection(urlPostgre, userPostgre, passwordPostgre)) {
			StringBuilder script = new StringBuilder();
			script.append("DROP TABLE IF EXISTS carrinho;"
					+ "DROP TABLE IF EXISTS produto;"
					+ "DROP TABLE IF EXISTS cliente;"
					+ "DROP TABLE IF EXISTS endereco;"
					+ "CREATE TABLE endereco ("
					+ "  id SERIAL PRIMARY KEY,"
					+ "  endereco VARCHAR(255)"
					+ ");"
					+ "CREATE TABLE cliente ("
					+ "  id SERIAL PRIMARY KEY,"
					+ "  nome VARCHAR(255),"
					+ "  idade INT,"
					+ "  idendereco INT,"
					+ "  FOREIGN KEY (idendereco) REFERENCES endereco (id)"
					+ ");"
					+ "CREATE TABLE produto ("
					+ "  id SERIAL PRIMARY KEY,"
					+ "  nome VARCHAR(255),"
					+ "  precoUn NUMERIC(5,2),"
					+ "  quantidade INT"
					+ ");"
					+ "CREATE TABLE carrinho ("
					+ "  id SERIAL PRIMARY KEY,"
					+ "  idcliente INT,"
					+ "  idproduto INT,"
					+ "  FOREIGN KEY (idcliente) REFERENCES cliente (id),"
					+ "  FOREIGN KEY (idproduto) REFERENCES produto (id)"
					+ ");");
			PreparedStatement ps = conn.prepareStatement(script.toString());
			System.out.println("Criando Banco de Dados PostgreSQL");
			ps.executeUpdate();
			ps.close();
			System.out.println("Termino da Criação do Banco PostgreSQL");
		}
	}
}
