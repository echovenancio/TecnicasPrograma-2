import service.FilterService;
import service.RequestService;

public class App {
    public static void main(String[] args) {
        var reqSevice = new RequestService();
        var comments = reqSevice.getComments();
        var emails = FilterService.getEmails(comments);
        System.out.println(emails);
    }
}
