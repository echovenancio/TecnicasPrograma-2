package model;

import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.UUID;
import db.Conexao;
import model.Tarefa;

public class GestorTarefa {

    public static void insertTarefa(String titulo, String descricao, String categoria) {
        try {
            Tarefa tarefa = new Tarefa(UUID.randomUUID().toString(), titulo, descricao, categoria);
            var conn = Conexao.conexao();
            var sql = "INSERT INTO tarefas(id, titulo, descricao, categoria, status) VALUES(?, ?, ?, ?, ?)";
            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tarefa.getId());
            pstmt.setString(2, tarefa.titulo);
            pstmt.setString(3, tarefa.descricao);
            pstmt.setString(4, tarefa.categoria);
            pstmt.setInt(5, tarefa.status);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void deleteTarefa(String id) {
        try {
            var conn = Conexao.conexao();
            var sql = "DELETE FROM tarefas WHERE id = ?";
            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.execute();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
        return;
    }

    public static Tarefa getTarefa(String id) {
        Tarefa tarefa = null;
        try {
            var conn = Conexao.conexao();
            var sql = "SELECT id, titulo, descricao, categoria, status FROM tarefas WHERE id = ?";
            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                tarefa = new Tarefa(
                    rs.getString("id"), 
                    rs.getString("titulo"), 
                    rs.getString("descricao"),
                    rs.getString("categoria"));
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
        return tarefa;   
    }

    public static ArrayList<Tarefa> getAllTarefas() {
        ArrayList<Tarefa> lista = new ArrayList();
        try {
            var conn = Conexao.conexao();
            var sql = "SELECT id, titulo, descricao, categoria, status FROM tarefas";
            var pstmt = conn.createStatement();
            var rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(new Tarefa(
                    rs.getString("id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("categoria"),
                    rs.getInt("status")
                ));
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
        return lista;
    }

    public static boolean updateTarefa(Tarefa tarefa) {
        boolean result = false;
        try {
            var conn = Conexao.conexao();
            var sql = "UPDATE tarefas SET titulo = ?, descricao = ?, categoria = ?, status = ? WHERE id = ?";
            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tarefa.titulo);
            pstmt.setString(2, tarefa.descricao); 
            pstmt.setString(3, tarefa.categoria);
            pstmt.setInt(4, tarefa.status);
            pstmt.setString(5, tarefa.getId());
            result = pstmt.execute();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
        return result;
    }
}
