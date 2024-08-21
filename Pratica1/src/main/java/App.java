import java.sql.Connection;
import db.Conexao;
import model.GestorTarefa;
import model.Tarefa;

public class App {
    public static void main(String[] args) {
        var gestor = new GestorTarefa();
        gestor.insertTarefa("teste", "testedesc", "testecategoria");
        var tarefas = gestor.getAllTarefas();
        for(Tarefa tarefa : tarefas) {
            System.out.println(tarefa.getId());
            System.out.println(tarefa.titulo);
            System.out.println(tarefa.descricao);
            System.out.println(tarefa.categoria);
            System.out.println(tarefa.status);
        }
        var tarefa01 = tarefas.get(0);
        tarefa01.titulo = "novo titulo";
        gestor.updateTarefa(tarefa01);
        tarefas = gestor.getAllTarefas();
        for(Tarefa tarefa : tarefas) {
            System.out.println(tarefa.getId());
            System.out.println(tarefa.titulo);
            System.out.println(tarefa.descricao);
            System.out.println(tarefa.categoria);
            System.out.println(tarefa.status);
        }
        gestor.deleteTarefa(tarefa01.getId());
        tarefas = gestor.getAllTarefas();
        for(Tarefa tarefa : tarefas) {
            System.out.println(tarefa.getId());
            System.out.println(tarefa.titulo);
            System.out.println(tarefa.descricao);
            System.out.println(tarefa.categoria);
            System.out.println(tarefa.status);
        }
    }
}
