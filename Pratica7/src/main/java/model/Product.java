package model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Product(
        @JsonAlias("id") int id,
        @JsonAlias("title") String title,
        @JsonAlias("price") int price,
        @JsonAlias("description") String description

) {
}
