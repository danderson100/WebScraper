import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<Item> allItems = new ArrayList<>();

    public static void main(String[] args) {

        String searchQuery = "bicycle";
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            String searchUrl = "https://tucson.craigslist.org/search/bia?query="
                    + URLEncoder.encode(searchQuery, "UTF-8");
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlElement> items = page.getByXPath("//li[@class='result-row']");
            if (items.isEmpty()) {
                System.out.println("No items found.");
            } else {
                for (HtmlElement item : items) {
                    HtmlAnchor itemAnchor = item.getFirstByXPath(".//h3[@class='result-heading']/a");
                    HtmlElement spanPrice = item.getFirstByXPath(".//a/span[@class='result-price']");
                    HtmlElement spanHood = item.getFirstByXPath(".//span[@class='result-hood']");
                    String itemName = itemAnchor.asText();
                    String itemUrl = itemAnchor.getHrefAttribute();

                    String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
                    String itemHood = spanHood == null ? "Not found" : spanHood.asText();
                    Item webItem = new Item();
                    webItem.setPrice(itemPrice);
                    //webItem.setPrice(new BigDecimal(itemPrice.replace("$", "")));
                    webItem.setTitle(itemName);
                    webItem.setUrl(itemUrl);
                    webItem.setNeighborhood(itemHood);

                    allItems.add(webItem);



                    System.out.printf("Name: %s Url: %s Price: %s  Hood: %s%n", itemName, itemUrl, itemPrice, itemHood);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

//        System.out.println("NOW PRINTING FROM ARRAYLIST\n");
//        System.out.println("---------------------------");
//        for (Item item : allItems) {
//            System.out.println(item.toString());
//        }


    }
}
