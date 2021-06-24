import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.*;

public class ArticleController {

    public void getTopArticles(int limit) {
        try {
            URL url = new URL("https://jsonmock.hackerrank.com/api/articles?page=" + limit);
            URLConnection connection = url.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String decodedString;
            StringBuffer stringBuffer = new StringBuffer();

            while ((decodedString = in.readLine()) != null) {
                stringBuffer.append(decodedString);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Article article = objectMapper.readValue(stringBuffer.toString(), Article.class);
            List<User> users = article.getData().stream().filter(user -> user.getNum_comments() != 0).collect(Collectors.toList());
//            users.sort(Comparator.comparing(user -> user.getNum_comments()));
//            Collections.reverse(users);
            Comparator<User> comparator = new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return Integer.valueOf(o2.getNum_comments()).compareTo(Integer.valueOf(o1.getNum_comments()));
                }
            };
            Collections.sort(users, comparator);
            users.forEach(user -> System.out.println(user.getTitle() + " " + user.getNum_comments()));
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        ArticleController controller = new ArticleController();
        controller.getTopArticles(2);
    }

}
