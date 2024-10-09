package service;

import model.Comment;

import java.util.List;

public class FilterService {
    public static List<String> getEmails(List<Comment> comments) {
        return comments.stream().map(Comment::email).toList();
    }
}
