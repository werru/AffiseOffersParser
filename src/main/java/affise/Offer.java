package affise;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wer2 on 08.04.17.
 */

public class Offer {
    private String id;
    private String name;
    private String payout;
    private String category;
    private String[] regions;
    private String capDaily,capTotal;
    private String description;
    private String trackingLink;
    private String previewLink;
    private ArrayList<Path> creatives;

    public Offer(String id,String name, String payout, String category, String regions, String capDaily, String capTotal, String description, String trackingLink, String previewLink, ArrayList<Path> creatives) {
        this.id = id;
        this.name = name;
        this.payout = payout;
        this.category = category;
        this.regions = regions.split(",");
        this.capDaily = capDaily;
        this.capTotal = capTotal;
        this.description = description;
        this.trackingLink = trackingLink;
        this.previewLink = previewLink;
        this.creatives = creatives;
    }
    public Offer(String id,String name, String payout, String category, String regions, String capDaily) {
        this.id = id;
        this.name = name;
        this.payout = payout;
        this.category = category;
        this.regions = regions.split(",");;
        this.capDaily = capDaily;
    }

    @Override
    public String toString() {
        return id +" "+ name +
//                ", regions='" + regions + " " +
                " payout " + payout + " " +
                (category.length()>2 ?" category " + category:"") +
//                " capDaily='" + capDaily + '\'' +
//                ", capTotal='" + capTotal + '\'' +
                "\n";
    }


    public String toStringAll() {
        return
                "<b>id:</b> " + id + "<br>" +
                "<b>title:</b> " + name + "<br>" +
                "<b>payout:</b> " + payout + "<br>" +
                (category.length()>2?"<b>category</b>" + category + "<br>":"")+
                "<b>regions:</b> " + Arrays.toString(regions).replaceAll("\\[|\\]|\"","")+ "<br>"+
                "<b>cap daily:</b> " + capDaily + "<br>" +
                "<b>description:</b> " + description + "<br>" +
                "<b>trackingLink:</b> " + trackingLink + "<br>" +
                "<b>previewLink:</b> " + previewLink  +"<br>";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayout() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout = payout;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getRegions() {
        return regions;
    }

    public void setRegions(String[] regions) {
        this.regions = regions;
    }

    public String getCapDaily() {
        return capDaily;
    }

    public void setCapDaily(String capDaily) {
        this.capDaily = capDaily;
    }

    public String getCapTotal() {
        return capTotal;
    }

    public void setCapTotal(String capTotal) {
        this.capTotal = capTotal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public ArrayList<Path> getCreatives() {
        return creatives;
    }

    public void setCreatives(ArrayList<Path> creatives) {
        this.creatives = creatives;
    }
}

