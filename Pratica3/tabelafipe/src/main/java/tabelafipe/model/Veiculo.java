package tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Veiculo(
       @JsonAlias("vehicleType") String tipo,
       @JsonAlias("price") String preco,
       @JsonAlias("model") String model,
       @JsonAlias("modelYear") int ano,
       @JsonAlias("fuel") String combustivel
) {
}
