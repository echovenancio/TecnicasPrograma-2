package tabelafipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResGenerico(
        @JsonAlias("code") String id,
        @JsonAlias("name") String name
) {
}
