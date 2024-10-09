import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);
        List<String> palavras = Arrays.asList("Aaaaaa", "Bbbbbbbbb", "Ccc", "Dddd", "Eeeee");
        numeros.stream()
                .map(n -> n * 2)
                .forEach(System.out::println);
        var palavrasFiltradas = palavras.stream()
                .filter(palavra -> palavra.length() > 5)
                .toList();
        System.out.println(palavrasFiltradas.toString());
        //palavrasFiltradas.forEach(System.out::println);
        List<Integer> numerosAle = Arrays.asList(1, 2, 3, 4, 5, 6);
        int somaDosPares = numerosAle.stream().filter(n -> n % 2 == 0).map(n -> n * 2).reduce(0, Integer::sum);
        System.out.println(somaDosPares);
    }
}
