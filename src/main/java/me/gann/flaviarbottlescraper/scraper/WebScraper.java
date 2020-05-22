package me.gann.flaviarbottlescraper.scraper;

import me.gann.flaviarbottlescraper.model.LiquorBottle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class WebScraper {

    RestTemplate restTemplate;

    static String FLAVIAR_BOTTLES_URL = "https://flaviar.com/bottles";

    @PostConstruct
    public void before() {
        restTemplate = new RestTemplate();
    }

    public List<LiquorBottle> getAllLiquors(){
        String resourceUrl = FLAVIAR_BOTTLES_URL;

        List<LiquorBottle> liquors = new ArrayList<>();

        try {
            ResponseEntity<String> response
                    = restTemplate.getForEntity(resourceUrl, String.class);
            String body = response.getBody();

            Document document = Jsoup.parse(body);

            Element table = document.getElementById("previousboxes");

            Element row = table.getElementsByClass("row").first();
            List<Element> bottles = row.getElementsByClass("col-lg-3");

            for(Element b : bottles){
                LiquorBottle bottle = new LiquorBottle();

                bottle.setName(b.getElementsByClass("name").first().text());
                String edition = b.getElementsByClass("edition").first().text();
                if(edition.contains(","))
                    edition = edition.replace(",", "");
                bottle.setEdition(edition);
                bottle.setType(b.getElementsByClass("type").first().text());

                if(b.getElementsByClass("valueRating").size() != 0)
                    bottle.setRating(Double.parseDouble(b.getElementsByClass("valueRating").first().text()));
                else
                    bottle.setRating(0.0);
                bottle.setPrice(Double.parseDouble(b.getElementsByClass("price").first().text().substring(1).replace(",","")));

                liquors.add(bottle);
            }
        } catch(Exception e){
            System.out.print(e.toString());
        }

        return liquors;
    }
}
