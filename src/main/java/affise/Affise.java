package affise;

/**
 * Created by wer2 on 08.04.17.
 */

import com.mkyong.controller.BaseController;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import java.io.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by wer-work on 31.03.17.
 */
public class Affise {
    private String apiKey;
    private double percent;
    private static final Affise instance = new Affise();
    List<Offer> offers=new ArrayList<>();
    private LocalDateTime updateOffers;
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);
    String mobvistaId;


    public static Affise getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Affise affise=getInstance();

    }

    public List<Offer> getNewOffersList()
    {
        offers.clear();
        return getOffers();
    }
    public String getOffersListData()
    {
        return updateOffers.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public List<Offer> getOffers()
    {
        if (offers.size()==0) {
            String result = getPageContent("http://api.adsheads.affise.com/2.1/partner/offers?limit=10000");
            JSONParser pars = new JSONParser();
            JSONArray json_array = null;
            try {
                json_array = (JSONArray) ((JSONObject) pars.parse(result)).get("offers");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (Object obj : json_array) {
                JSONObject json_obj = (JSONObject) obj;
                //TODO сделать корректыным офер с несколькими странами
                JSONObject paymentJSON = (JSONObject) ((JSONArray) json_obj.get("payments")).get(0);
                String countries = null;
                if (paymentJSON.get("countries") != null)
                    countries = paymentJSON.get("countries").toString();
                String payment = paymentJSON.get("revenue").toString();
                // id, name,  payout,  category,  regions,  capDaily,  capTotal,  description,  trackingLink,  previewLink, ArrayList<Path> creatives) {
                try {


                    offers.add(new Offer(json_obj.get("id").toString(), json_obj.get("title").toString(), payment, json_obj.get("categories").toString(), json_obj.get("countries").toString(), json_obj.get("cap").toString(), "",
                            (json_obj.get("description")!=null)?json_obj.get("description").toString():"",
                            json_obj.get("link").toString(),
                            json_obj.get("preview_url").toString(), null));
                }
                catch (NullPointerException e)
                {
                    logger.debug("problem with offer", json_obj);
                }
            }
            updateOffers= LocalDateTime.now(Clock.systemDefaultZone());
        }
        return offers;
    }

    public String getOffersToString()
    {
        String result=new String();
        for (Offer offer: getOffers())
        {
            result+=offer.toString()+"<br>";
        }
        return result;
    }

    public String getOfferById(String id)
    {
        for( Offer offer: offers)
        {
            if (offer.getId().equals(id))
                return offer.toStringAll();
        }

        return null;

    }

    String getPageContent(String uploadUrl) {
            BasicNameValuePair arguments=new BasicNameValuePair("API-Key", apiKey);
        String response2 = null;
        try {
            HttpClientBuilder builder = HttpClientBuilder.create();
            CloseableHttpClient httpClient = builder.build();
            HttpGet httpGet = new HttpGet(uploadUrl);
//           StringEntity params = new StringEntity(text, "UTF-8");
//            StringEntity params = new StringEntity(text);
//            httpGet.setEntity(new UrlEncodedFormEntity(arguments));
            httpGet.setHeader("API-Key", apiKey);
//            httpGet.getEntity();

            HttpResponse response = httpClient.execute(httpGet);
            response2 = getStringFromInputStream(response.getEntity().getContent());
        } catch (IOException ee) {
            logger.debug("ошибка отправки Get");
            ee.printStackTrace();
        }
        return response2;



    }

    public boolean postOffer(Offer offer) {
        Double revenueDouble = new Double(Double.parseDouble(offer.getPayout()) * percent);
        String revenue = revenueDouble.toString();
        if (revenue.length() > 4)
            revenue = revenue.substring(0, 5);
        String request = null;
//        try {
//            request = "title="+offer.getName()+"&url="+ URLEncoder.encode(offer.getTrackingLink(),"UTF8")+"&" +
//                    "url_preview="+offer.getPreviewLink()+"&advertiser="+mobvistaId+"&status=suspended&" +
//                    "privacy=public&payments[0][title]=install"+regions+"&payments[0][total]="+offer.getPayout()+"&payments[0][revenue]=" + revenue+
//                    "&payments[0][currency]=USD&payments[0][type]=fixed&hide_referer=1"+countries+"&total_cap="+offer.getCapTotal()+"&daily_cap="+offer.getCapDaily()+"&daily_cap_partner="+offer.getCapDaily();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//                +"&categories[0]="+offer.getCategory();

        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("title", offer.getName()));
        arguments.add(new BasicNameValuePair("url", offer.getTrackingLink()));
        arguments.add(new BasicNameValuePair("url_preview", offer.getPreviewLink()));
        arguments.add(new BasicNameValuePair("advertiser", mobvistaId));
        arguments.add(new BasicNameValuePair("status", "suspended"));
        arguments.add(new BasicNameValuePair("privacy", "public"));
        arguments.add(new BasicNameValuePair("payments[0][title]=", "install"));
        for (int i = 0; i < offer.getRegions().length; i++) {
            arguments.add(new BasicNameValuePair("payments[0][countries][]", offer.getRegions()[i]));
            arguments.add(new BasicNameValuePair("countries[" + i + "]", offer.getRegions()[i]));
        }
        arguments.add(new BasicNameValuePair("payments[0][total]=", offer.getPayout()));
        arguments.add(new BasicNameValuePair("payments[0][revenue]", revenue));
        arguments.add(new BasicNameValuePair("payments[0][currency]", "USD"));
        arguments.add(new BasicNameValuePair("hide_referer", "1"));
        arguments.add(new BasicNameValuePair("payments[0][type]", "fixed"));
        arguments.add(new BasicNameValuePair("total_cap", offer.getCapTotal().contains("Open Cap") ? "0" : offer.getCapTotal()));
        arguments.add(new BasicNameValuePair("daily_cap", offer.getCapDaily().contains("Open Cap") ? "0" : offer.getCapDaily()));
        arguments.add(new BasicNameValuePair("daily_cap_partner", offer.getCapDaily().contains("Open Cap") ? "0" : offer.getCapDaily()));


        System.out.println(arguments);
        System.out.println(postData(arguments));
        return true;

    }


    private Affise() {
        Properties property = new Properties();
        try {
            InputStream fis = this.getClass().getClassLoader().getResourceAsStream("config.properties");
            property.load(fis);

            apiKey = property.getProperty("apiKey");
            percent = Double.parseDouble(property.getProperty("percent"));
            mobvistaId =  property.getProperty("mobvistaId");

        } catch (IOException e) {
            logger.debug("ОШИБКА: Файл свойств отсуствует!");
        }
    }


    private String postData(List<NameValuePair> text) {

        return postData("http://api.adsheads.affise.com/2.1/admin/offer", text);

    }


    private String postData(String uploadUrl, List<NameValuePair> text) {
        String response2 = null;
        try {
            HttpClientBuilder builder = HttpClientBuilder.create();
            CloseableHttpClient httpClient = builder.build();
            HttpPost httpPost = new HttpPost(uploadUrl);
//           StringEntity params = new StringEntity(text, "UTF-8");
//            StringEntity params = new StringEntity(text);
            httpPost.setEntity(new UrlEncodedFormEntity(text));
            httpPost.setHeader("API-Key", apiKey);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.getEntity();

            HttpResponse response = httpClient.execute(httpPost);
            response2 = getStringFromInputStream(response.getEntity().getContent());
        } catch (IOException ee) {
            logger.debug("ошибка отправки post");
            ee.printStackTrace();
        }
        return response2;
    }

    String getStringFromInputStream(InputStream reader) {
        StringBuffer response2 = null;
        try {


            BufferedReader in = new BufferedReader(new InputStreamReader(reader));
            String inputLine;
            response2 = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response2.append(inputLine);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response2.toString();
    }


}

