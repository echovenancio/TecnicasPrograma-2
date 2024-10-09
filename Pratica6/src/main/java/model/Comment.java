package model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Comment(
        @JsonAlias("postId") int postId,
        @JsonAlias("id") int id,
        @JsonAlias("name") String name,
        @JsonAlias("email") String email,
        @JsonAlias("body") String body
) {
}
