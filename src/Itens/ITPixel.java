package Itens;

import java.io.Serializable;

public class ITPixel implements Serializable {

    public static final String PIXEL_ID = "583889059129360" ;

    private int id, iduser;
    private String idPixel, code, event,eventPurchase, eventSubscriber, pixelBackup;

    public ITPixel(){}

    public ITPixel(String id, String event, String idPixelBackup){
        code = createPixelFromID(id, event);
        pixelBackup = createPixelFromID(idPixelBackup, event);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getIdPixel() {
        return idPixel;
    }

    public void setIdPixel(String idPixel) {
        this.idPixel = idPixel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String pixel) {
        this.code = pixel;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPixelBackup() {
        return pixelBackup;
    }

    public String getEventPurchase() {
        return eventPurchase;
    }

    public String createEventPurchase(String idProduct, double value, String moeda, int quantity) {
        return eventPurchase(idProduct, value, moeda, quantity);
    }

    private String createPixelFromID(String idPixel, String event){
        if(idPixel != null && event != null){
            String pixel = "<!-- Facebook Pixel Code -->\n" +
                    "<script>\n" +
                    "  !function(f,b,e,v,n,t,s)\n" +
                    "  {if(f.fbq)return;n=f.fbq=function(){n.callMethod?\n" +
                    "  n.callMethod.apply(n,arguments):n.queue.push(arguments)};\n" +
                    "  if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';\n" +
                    "  n.queue=[];t=b.createElement(e);t.async=!0;\n" +
                    "  t.src=v;s=b.getElementsByTagName(e)[0];\n" +
                    "  s.parentNode.insertBefore(t,s)}(window, document,'script',\n" +
                    "  'https://connect.facebook.net/en_US/fbevents.js');\n" +
                    "  fbq('init', '###');\n" +
                    "  fbq('track', '@@@@@@');\n" +
                    "</script>\n" +
                    "<noscript><img height=\"1\" width=\"1\" style=\"display:none\"\n" +
                    "  src=\"https://www.facebook.com/tr?id=###&ev=@@@@@@&noscript=1\"\n" +
                    "/></noscript>\n" +
                    "<!-- End Facebook Pixel Code -->";

            pixel = pixel.replace("###",idPixel);
            pixel = pixel.replace("@@@@@@",event);
            return pixel ;
        }
        return null ;
    }

    private String eventPurchase(String idProduct, double value, String moeda, int quantity){
        String pixelPurchase = "<script>\n" +
                "  fbq('track', 'Purchase', {\n" +
                "    value: RFGTÜJ,\n" +
                "    currency: 'BRL',\n" +
                "    contents: [\n" +
                "        {\n" +
                "            id: 'idproduct',\n" +
                "            quantity: quantidade\n" +
                "        }\n" +
                "    ],\n" +
                "  });\n" +
                "</script>";

        pixelPurchase = pixelPurchase.replace("RFGTÜJ", String.valueOf(value));
        pixelPurchase = pixelPurchase.replace("BRL",moeda);
        pixelPurchase = pixelPurchase.replace("quantidade", String.valueOf(quantity));
        pixelPurchase = pixelPurchase.replace("idproduct",idProduct);

        return pixelPurchase ;
    }


}
