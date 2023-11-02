package apis.jsoup;

import Itens.ITLinkReplace;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class APIJsoup {

    public static String cloneWebSiteAndReplaceURLS(String destiny, ObservableList<ITLinkReplace> linkReplaces){
        String siteReplaced = null ;
        try {
            siteReplaced = Jsoup.connect(destiny).header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64;rv:23.0) Gecko/20100101 Firefox/23.0")
                    .maxBodySize(0)
                    .get().getElementsByTag("html").toString();

            for(ITLinkReplace linkReplace : linkReplaces)
                siteReplaced = siteReplaced
                        .replaceAll(linkReplace.getCurrentURL(),linkReplace.getSubstitutueURL())
                        .replace(linkReplace.getCurrentURL(),linkReplace.getSubstitutueURL());
            return siteReplaced ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return siteReplaced;
    }

/*
    public static void main(String[] args) {
        ITLinkReplace rep = new ITLinkReplace("https://olink.me/plano-mensal","https://google.com");
        System.out.println(new APIJsoup().cloneAndReplaceURLS("https://site.olink.me/",rep));
    }
*/

}
