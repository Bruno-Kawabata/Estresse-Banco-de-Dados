package entidade_Dao;

import java.sql.*;
import java.util.*;

public class produtoDAO {
	private Connection conn;

	public produtoDAO(Connection conn) {
		this.conn = conn;
	}

	public void inserir(produto produto) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO produto (nome, preco, quantidade) VALUES (?, ?, ?)");
		ps.setString(1, produto.getNome());
		ps.setDouble(2, produto.getPreco());
		ps.setInt(3, produto.getQuantidade());
		ps.executeUpdate();
		ps.close();
	}
	
	public static Integer valorRand(Integer min, Integer max) {
		Random rand = new Random();
		Integer randomNum = min + rand.nextInt((max - min) + 1);
		return randomNum;
	}

	public List<produto> buscarTodos() throws SQLException {
		List<produto> produtos = new ArrayList<>();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM produto");
		while (rs.next()) {
			produto produto = new produto();
			produto.setId(rs.getInt("id"));
			produto.setNome(rs.getString("nome"));
			produto.setPreco(rs.getDouble("preco"));
			produto.setQuantidade(rs.getInt("quantidade"));
			produtos.add(produto);
		}
		rs.close();
		stmt.close();
		return produtos;
	}

	public void atualizar(produto produto) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("UPDATE produto SET nome = ?, preco = ?, quantidade = ? WHERE id = ?");
		ps.setString(1, produto.getNome());
		ps.setDouble(2, produto.getPreco());
		ps.setInt(3, produto.getQuantidade());
		ps.setInt(4, produto.getId());
		ps.executeUpdate();
		ps.close();
	}

	public void excluir(int id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM produtos WHERE id = ?");
		ps.setInt(1, id);
		ps.executeUpdate();
		ps.close();
	}
}
