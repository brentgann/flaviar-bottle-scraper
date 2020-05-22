package me.gann.flaviarbottlescraper.runner;

import me.gann.flaviarbottlescraper.model.LiquorBottle;
import me.gann.flaviarbottlescraper.scraper.WebScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class StartUpRunner {

    @Autowired
    WebScraper webScraper;

    static String FILENAME = "flaviar-bottles.csv";

    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {
        List<LiquorBottle> bottleList = webScraper.getAllLiquors();
        try {
            exportCSV(bottleList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportCSV(List<LiquorBottle> bottleList) throws IOException {
        String exportString = "name,edition,type,price,rating\n";

        for(LiquorBottle lb : bottleList){
            exportString = exportString.concat(lb.getName() + "," + lb.getEdition() + "," + lb.getType() + "," + lb.getPrice() + "," + lb.getRating() + "\n");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME));
        writer.write(exportString);

        writer.close();
    }
}
