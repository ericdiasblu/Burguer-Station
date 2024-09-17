package gui;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbc.DatabaseConnection;

import java.sql.Statement;

public class SQLCommands {
	
	//Inserir dados a tabela
    public void inserirDados(int mesa, String itens, int quantidade, String hora, String status) {
        String sql = "INSERT INTO infopedidos (mesa, itens, quantidade, hora, status) VALUES"+ "(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mesa);
            stmt.setString(2, itens);
            stmt.setInt(3, quantidade);
            stmt.setString(4, hora);
            stmt.setString(5, status);

            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Relatório dos pedidos
    public ObservableList<InfoPedidos> gerarRelatorio() {
        ObservableList<InfoPedidos> listaDados = FXCollections.observableArrayList();

        String query = "SELECT * FROM infopedidos";  
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int mesa = rs.getInt("mesa");
                String itens = rs.getString("itens");
                int quantidade = rs.getInt("quantidade");
                String horarioDoPedido = rs.getString("hora");
                String statusPedido = rs.getString("status");


                InfoPedidos dados = new InfoPedidos(id,mesa, itens, quantidade, horarioDoPedido, statusPedido);
                listaDados.add(dados);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaDados;
    }
    
    // Busca do pedido por ID
    
    public InfoPedidos buscarPedido(int id) {
        String sql = "SELECT * FROM infopedidos WHERE id = ?";
        InfoPedidos pedido = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int mesa = rs.getInt("mesa");
                String itens = rs.getString("itens");
                int quantidade = rs.getInt("quantidade");
                String hora = rs.getString("hora");
                String status = rs.getString("status");

                pedido = new InfoPedidos(id, mesa, itens, quantidade, hora, status);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return pedido;
    }
    
    // UPDATE
    
    public void atualizarDados(int id, int mesa, String itens, int quantidade, String hora, String status) {
        String sql = "UPDATE infopedidos SET mesa = ?, itens = ?, quantidade = ?, hora = ?, status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mesa);
            stmt.setString(2, itens);
            stmt.setInt(3, quantidade);
            stmt.setString(4, hora);
            stmt.setString(5, status);
            stmt.setInt(6, id);

            stmt.executeUpdate();
            System.out.println("Dados atualizados com sucesso!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void deletarPedido(int id) {
        String sql = "DELETE FROM infopedidos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Pedido deletado com sucesso!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}
