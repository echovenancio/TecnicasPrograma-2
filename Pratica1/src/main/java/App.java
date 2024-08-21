import java.sql.Connection;
import db.Conexao;
import model.GestorTarefa;
import model.Tarefa;
import cli.Sistema;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        var gestor = new GestorTarefa();
        var sistema = new Sistema(gestor); 
        sistema.pintarMenu();
        while (true) {
            sistema.pegarInput();
            Scanner sc = new Scanner(System.in);
            System.out.print("\n\nAperte <enter> para mostrar as opções");
            var wait = sc.nextLine();
            sistema.pintarMenu();
        }
    }
}
