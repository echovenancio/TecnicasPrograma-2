package model;
import java.util.UUID;

public class Tarefa {
    private String id;
    public String titulo;
    public String descricao;
    public String categoria;
    public int status;

    public Tarefa(String id, String titulo, String descricao, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public Tarefa(String id, String titulo, String descricao, String categoria, int status) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.status = status;
    }

    public void concluir() {
        this.status = 1;
    }

    public String getId() {
        return this.id.toString();
    }
}
