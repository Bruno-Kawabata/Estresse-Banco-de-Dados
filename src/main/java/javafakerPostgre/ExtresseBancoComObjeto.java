package javafakerPostgre;

import com.github.javafaker.Faker;

import entidade_Dao.produto;
import entidade_Dao.produtoDAO;

import java.sql.*;
import java.util.*;

public class ExtresseBancoComObjeto {

	static Faker faker = new Faker(new Locale("pt-BR"));
	// String nome = faker.name().fullName();
	// String endereco = faker.address().fullAddress();
	// String telefone = faker.phoneNumber().phoneNumber();
	// System.out.println(nome + endereco + telefone);

	public static void main(String[] args) {

		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			public void run() {
				Integer quantidadeInsert = 5;
				long startTime = System.currentTimeMillis();
				
				for (int i = 1; i <= quantidadeInsert; i++) {
					try {
//						insereProdPostgre();
						insereProdOracle();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("prod: " + i + " inserido");
				}
				
				long endTime = System.currentTimeMillis();
				long tempo = endTime - startTime;
				double tempoSegundos = tempo / 1000.0;

				timer.cancel();
				System.out.println("produtos inseridos no banco postgre");
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

	public static void insereProdPostgre() throws SQLException {
		String urlPostgre = "jdbc:postgresql://localhost:8080/JavaFakerPostgre";
		String userPostgre = "postgres";
		String passwordPostgre = "automa";

		try (Connection conn = DriverManager.getConnection(urlPostgre, userPostgre, passwordPostgre)) {
			produtoDAO dao = new produtoDAO(conn);

			// Inserir um produto
			produto produto1 = new produto();
			produto1.setNome(faker.food().fruit());
			produto1.setPreco(Double.valueOf(valorRand(1, 999)));
			produto1.setQuantidade(valorRand(1, 500));
			dao.inserir(produto1);
//			System.out.println("produto foi inserido no banco postgre");

		}
	}

	public static void insereProdOracle() throws SQLException {
		String urlOracle = "jdbc:oracle:thin:@localhost:1521:xe";
		String userOracle = "BRUNO";
		String passwordOracle = "root";

		try (Connection conn = DriverManager.getConnection(urlOracle, userOracle, passwordOracle)) {
			produtoDAO dao = new produtoDAO(conn);

			// Inserir um produto
			produto produto1 = new produto();
			produto1.setNome("Produto 1");
			produto1.setPreco(10.0);
			produto1.setQuantidade(20);
			dao.inserir(produto1);
			System.out.println("produto foi inserido no banco oracle");

		}
	}
}
