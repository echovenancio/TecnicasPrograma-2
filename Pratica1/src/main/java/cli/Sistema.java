package cli;

import java.util.Scanner;
import java.util.ArrayList;
import model.Tarefa;
import model.GestorTarefa;

public class Sistema {
    private GestorTarefa gestor;

    public Sistema(GestorTarefa gestor) {
        this.gestor = gestor;
    }

    private void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void pintarMenu() {
        this.limparTela();
        System.out.print("""
┌──────────────────────────────────────┐
│                                      │
│ Sistema de gerenciamento de tarefas  │
│                                      │
└──────────────────────────────────────┘
    - Opções:
        n               -> Nova tarefa;
        l <flag?> <q?>  -> Listar todas as tarefas;
        e <id>          -> Edita tarefa com o id especificado; 
        d <id>          -> Exclui tarefa com o id especificao;
        h               -> Mostra esse menu que você esta vendo;
        q               -> Sair da aplicação;
    - OBS: 
        <>  -> obrigatorio;
        <?> -> opcional;

            """);
        System.out.flush();
    }

    private void novaTarefa() {
        this.limparTela();
        Scanner sc = new Scanner(System.in);
        System.out.print("->> Titulo: ");
        var titulo = sc.nextLine();
        System.out.print("->> Descrição: ");
        var descricao = sc.nextLine();
        System.out.print("->> Categoria: ");
        var categoria = sc.nextLine();
        this.gestor.insertTarefa(titulo, descricao, categoria);
        System.out.print("Tarefa inserida.");
    }

    private void listarTarefas() {
        this.limparTela();
        ArrayList<Tarefa> listaTarefa = this.gestor.getAllTarefas();
        for (Tarefa tarefa: listaTarefa) {
            System.out.println("+-");
            System.out.println("|Id: " + tarefa.getId());
            System.out.println("|Titulo: " + tarefa.titulo);
            System.out.println("|Descrição: " + tarefa.descricao);
            System.out.println("|Categoria: " + tarefa.categoria);
            String status = "pendente";
            if (tarefa.status != 0) {
                status = "concluido";
            }
            System.out.println("|Status: " + status);
        }
        System.out.println("+-");
    }

    private void listarTarefasComQ(String flag, String q) {
        this.limparTela();
        if (flag.equals("categoria") || flag.equals("status")) {
            if (flag.equals("status")) {
                if (q.equals("concluido")) {
                    q = "1";
                } else if (q.equals("pendente")) {
                    q = "0";
                } else {
                    System.err.println("[ERR] Status inválido.");
                    return;
                }
            }
            ArrayList<Tarefa> listaTarefa = this.gestor.getAllTarefasWithQ(flag, q);
            for (Tarefa tarefa: listaTarefa) {
                System.out.println("+-");
                System.out.println("|Id: " + tarefa.getId());
                System.out.println("|Titulo: " + tarefa.titulo);
                System.out.println("|Descrição: " + tarefa.descricao);
                System.out.println("|Categoria: " + tarefa.categoria);
                String status = "pendente";
                if (tarefa.status != 0) {
                    status = "concluido";
                }
                System.out.println("|Status: " + status);
            }
            System.out.println("+-");
            return;
        }
        System.err.println("[ERR] categoria inválida");
        return;
    }

    private void editarTarefa(String id) {
        this.limparTela();
        Tarefa tarefa = this.gestor.getTarefa(id);
        if (tarefa == null) {
            System.err.println("[ERR] Nenhuma tarefa com o id indicado.");
            return;
        }
        System.out.println("+-Tarefa:");
        System.out.println("|Id: " + tarefa.getId());
        System.out.println("|Titulo: " + tarefa.titulo);
        System.out.println("|Descrição: " + tarefa.descricao);
        System.out.println("|Categoria: " + tarefa.categoria);
        String status = "pendente";
        if (tarefa.status == 1) {
            status = "concluido";
        }
        System.out.println("|Status: " + status);
        System.out.println("+-");
        Scanner sc = new Scanner(System.in);
        System.out.print("->> Titulo: ");
        tarefa.titulo = sc.nextLine();
        System.out.print("->> Descrição: ");
        tarefa.descricao = sc.nextLine();
        System.out.print("->> Categoria: ");
        tarefa.categoria = sc.nextLine();
        System.out.print("->> Status: ");
        status = sc.nextLine();
        if (status.equals("concluido")) {
            tarefa.concluir();
        } else if (status.equals("pendente")) {
            tarefa.status = 0;
        } else {
            System.err.print("[ERR] Status inválido.");
            return;           
        }
        this.gestor.updateTarefa(tarefa);
        System.out.print("Tarefa atualizada.");
    }

    private void excluirTarefa(String id) {
        this.limparTela();
        Tarefa tarefa = this.gestor.getTarefa(id);
        if (tarefa == null) {
            System.err.print("[ERR] nenhuma tarefa com o id indicado.");
            return;
        }
        this.gestor.deleteTarefa(id);
        System.out.print("Tarefa Excluida.");
    }

    public void pegarInput() {
        Scanner sc = new Scanner(System.in);
        System.out.print("->> ");
        var op = sc.nextLine();
        String[] input = op.split(" ");
        switch (input[0]) {
            case "n":
                this.novaTarefa();
                break;
            case "l":
                if (input.length == 3) {
                    this.listarTarefasComQ(input[1], input[2]);
                    break;
                }
                this.listarTarefas();
                break;
            case "e":
                if (input.length != 2) {
                    System.err.println("[ERR] esperava comando e id da tarefa");
                    return;
                }
                this.editarTarefa(input[1]);
                break;
            case "d":
                if (input.length != 2) {
                    System.err.println("[ERR] esperava comando e id da tarefa");
                    return;               
                }
                this.excluirTarefa(input[1]);
                break;
            case "h":
                this.pintarMenu();
                break;
            case "q":
                System.exit(0);
                break;
            default:
                System.err.println("[ERR] comando não foi reconhecido.");
                break;
        }
    }
}
