import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

//A websocket for getting synonyms using DataMuse service
//https://www.datamuse.com/api/

public class Synonym {
    public static final int SUCCESS_CODE = 200;

    /**
     * Get synonyms of a file.
     * @param file file as a set of words(String)
     * @return lists of synonyms that correspond to words
     * @throws IOException exception
     * @throws URISyntaxException exception
     */
    public HashMap<String, List<String>> getSynonyms(Set<String> file) throws IOException, URISyntaxException {

        HashMap<String, List<String>> wordSynList = new HashMap<>();
        for (String word : file) {
            List<String> synonyms = requestSyn(word);
            wordSynList.put(word, synonyms);
        }
        return wordSynList;
    }

    /**
     * Get synonyms of a word.
     * @param word the word to check
     * @return List of synonyms corresponds to that word
     * @throws URISyntaxException exception
     * @throws IOException exception
     */
    private static List<String> requestSyn(String word) throws URISyntaxException, IOException {

        String baseURL = "https://api.datamuse.com/words?ml=";
        String completeURL = baseURL + word;
        CloseableHttpClient client;
        CloseableHttpResponse response;
        client = HttpClients.createDefault();
        JsonArray responseAsArray = new JsonArray();
        List<String> synonyms = new ArrayList<>();

        //build URI for request
        URIBuilder buildURI = new URIBuilder(completeURL);

        //assemble get request
        HttpGet getRequest = new HttpGet(buildURI.build());
        response = client.execute(getRequest);

        //check & return response
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == SUCCESS_CODE) {
            String str = EntityUtils.toString(response.getEntity(), "UTF-8");
            responseAsArray = (JsonArray) JsonParser.parseString(str);
        }

        //parse json array into list of synonyms
        //similarity in descending order
        for (JsonElement element : responseAsArray) {
            synonyms.add(element.getAsJsonObject().get("word").getAsString());
        }
        return synonyms;
    }
}
