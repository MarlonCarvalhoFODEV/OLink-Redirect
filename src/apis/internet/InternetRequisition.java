package apis.internet;

import apis.encrypt.AES;
import apis.encrypt.RSA;
import com.google.gson.Gson;
import javafx.application.Platform;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HttpsURLConnection;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.*;

public class InternetRequisition implements Runnable{

    private String id;
    private InternetRequisitionResponse response;
    private String domain, controller, function, paramters;
    private Object objectTransport;

    private int typeRequest;
    private int useCryptographToSend;
    private int useCryptographToReceiver;

    private WebJson jsonRequest;
    private CryptographRSA cryptographRSA;
    private CryptographRSA otherCryptographRSA;
    private CryptographAES cryptographAES;
    private static InternetRequisitionAPI api;

    public InternetRequisition() {
        this.jsonRequest = new WebJson();
    }

    public InternetRequisition(int typeRequest, InternetRequisitionResponse newResponse) {
        this.response = newResponse;
        this.typeRequest = typeRequest;
        this.jsonRequest = new WebJson();
    }

    public InternetRequisition(int typeRequest) {
        this.typeRequest = typeRequest;
        this.jsonRequest = new WebJson();
    }

    public InternetRequisition(InternetRequisitionAPI api) {
        this.api = api;
        this.jsonRequest = new WebJson(api);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public String getController() {
        return controller;
    }

    public String getFunction() {
        return function;
    }

    public Object getObjectTransport() {
        return objectTransport;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setObjectTransport(Object objectTransport) {
        this.objectTransport = objectTransport;
    }

    public int getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(int typeRequest) {
        this.typeRequest = typeRequest;
    }

    public void setUseCryptographToSend(int typeCriptograph) {
        this.useCryptographToSend = typeCriptograph;
    }

    public void setUseCryptographToReceiver(int typeCriptograph) {
        this.useCryptographToReceiver = typeCriptograph;
    }

    public void setCryptographRSA(CryptographRSA cryptographRSA) {
        this.cryptographRSA = cryptographRSA;
    }

    public void setOtherCryptographRSA(CryptographRSA otherCryptographRSA) {
        this.otherCryptographRSA = otherCryptographRSA;
    }

    public void setCryptographAES(CryptographAES cryptographAES) {
        this.cryptographAES = cryptographAES;
    }

    public InternetRequisitionResponse getResponse() {
        return response;
    }

    public void setResponse(InternetRequisitionResponse response) {
        this.response = response;
    }



    private synchronized void request() {
        response.onLoading(true);

        StringBuilder sb = new StringBuilder();
        if (domain != null)
            sb.append(domain);
        if (controller != null)
            sb.append(controller);
        if (function != null)
            sb.append(function);
        if (paramters != null)
            sb.append(paramters);

        if (haveConnection()) {
            switch (typeRequest) {
                case TypeRequest.GET: {

                    String sendJson = null;
                    if (useCryptographToSend == TypeCryptograph.RSA && cryptographRSA != null) {
                        sendJson = cryptographRSA.encryptToJson(objectTransport);
                    } else if (useCryptographToSend == TypeCryptograph.OTHER_RSA && otherCryptographRSA != null) {
                        sendJson = otherCryptographRSA.encryptToJson(objectTransport);
                    } else if (useCryptographToSend == TypeCryptograph.AES && cryptographAES != null) {
                        sendJson = cryptographAES.encryptToAESJson(objectTransport);
                    }

                    String json = null;

                    if (id != null && sendJson == null)
                        json = jsonRequest.jsonRequest(id, "GET", sb.toString());
                    else if (id != null && objectTransport != null)
                        json = jsonRequest.jsonRequest(id, "GET", sb.toString(), sendJson);
                    else
                        json = jsonRequest.getJson(sb.toString());

                    Object object = null;

                    if (useCryptographToReceiver == TypeCryptograph.RSA && cryptographRSA != null) {
                        object = json;
                    } else if (useCryptographToReceiver == TypeCryptograph.OTHER_RSA && otherCryptographRSA != null) {
                        object = json;
                    } else if (useCryptographToReceiver == TypeCryptograph.AES && cryptographAES != null) {
                        if (json != null) {
                            object = cryptographAES.decryptToJson(json);
                        }
                    } else {
                        object = json ;
                    }

                    Object finalV = object;

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                response.onSucess(finalV);
                                response.onLoading(false);
                            } catch (ClassCastException es) {
                                response.onSucess(finalV);
                                response.onLoading(false);
                            }
                            api.disconnect(id);
                        }
                    });
                    break;
                }
                case TypeRequest.POST: {
                    try {
                        String sendJson = null;
                        if (useCryptographToSend == TypeCryptograph.RSA && cryptographRSA != null) {
                            sendJson = cryptographRSA.encryptToJson(objectTransport);
                        } else if (useCryptographToSend == TypeCryptograph.OTHER_RSA && otherCryptographRSA != null) {
                            sendJson = otherCryptographRSA.encryptToJson(objectTransport);
                        } else if (useCryptographToSend == TypeCryptograph.AES && cryptographAES != null) {
                            sendJson = cryptographAES.encryptToAESJson(objectTransport);
                        }

                        String json = null;
                        if (id != null) {
                            json = jsonRequest.jsonRequest(id, "POST", sb.toString(), sendJson);
                        } else
                            json = jsonRequest.jsonRequest("POST", sb.toString(), sendJson);

                        Object obj = null;
                        if (useCryptographToReceiver == TypeCryptograph.RSA && cryptographRSA != null) {
                            if (json != null)
                                obj = json;
                        } else if (useCryptographToReceiver == TypeCryptograph.OTHER_RSA && otherCryptographRSA != null) {
                            if (json != null)
                                obj = json;
                        } else if (useCryptographToReceiver == TypeCryptograph.AES && cryptographAES != null) {
                            if (json != null)
                                obj = cryptographAES.decryptToJson(json);
                        }
                        Object finalObj = obj;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    response.onSucess(finalObj);
                                    response.onLoading(false);
                                } catch (ClassCastException es) {
                                    response.onSucess(finalObj);
                                    response.onLoading(false);
                                }
                                api.disconnect(id);
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    response.onError(e);
                                    response.onLoading(false);
                                } catch (ClassCastException es) {
                                    response.onError(es);
                                    response.onLoading(false);
                                }
                                api.disconnect(id);
                            }
                        });
                    }

                    break;
                }
                case TypeRequest.PUT: {
                    try {
                        String sendJson = null;
                        if (useCryptographToSend == TypeCryptograph.RSA && cryptographRSA != null) {
                            sendJson = cryptographRSA.encryptToJson(objectTransport);
                        } else if (useCryptographToSend == TypeCryptograph.OTHER_RSA && otherCryptographRSA != null) {
                            sendJson = otherCryptographRSA.encryptToJson(objectTransport);
                        } else if (useCryptographToSend == TypeCryptograph.AES && cryptographAES != null) {
                            sendJson = cryptographAES.encryptToAESJson(objectTransport);
                        }

                        String json = null;
                        if (id != null) {

                            json = jsonRequest.jsonRequest(id, "PUT", sb.toString(), sendJson);
                        } else
                            json = jsonRequest.jsonRequest("PUT", sb.toString(), sendJson);

                        Object obj = null;
                        if (useCryptographToReceiver == TypeCryptograph.RSA && cryptographRSA != null) {
                            if (json != null)
                                obj = json;
                        } else if (useCryptographToReceiver == TypeCryptograph.OTHER_RSA && otherCryptographRSA != null) {
                            if (json != null)
                                obj = json;
                        } else if (useCryptographToReceiver == TypeCryptograph.AES && cryptographAES != null) {
                            if (json != null)
                                obj = cryptographAES.decryptToJson(json);
                        }
                        Object finalObj = obj;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    response.onSucess(finalObj);
                                    response.onLoading(false);
                                } catch (ClassCastException es) {
                                    response.onSucess(finalObj);
                                    response.onLoading(false);
                                }
                                api.disconnect(id);
                            }
                        });
                    } catch (Exception e) {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    response.onError(e);
                                    response.onLoading(false);
                                } catch (ClassCastException es) {
                                    response.onError(es);
                                    response.onLoading(false);
                                }
                                api.disconnect(id);
                            }
                        });
                    }
                    break;
                }
                case TypeRequest.UPDATE: {

                    break;
                }
                case TypeRequest.DELETE:{
                    try {
                        String sendJson = null;
                        if (useCryptographToSend == TypeCryptograph.RSA && cryptographRSA != null) {
                            sendJson = cryptographRSA.encryptToJson(objectTransport);
                        } else if (useCryptographToSend == TypeCryptograph.OTHER_RSA && otherCryptographRSA != null) {
                            sendJson = otherCryptographRSA.encryptToJson(objectTransport);
                        } else if (useCryptographToSend == TypeCryptograph.AES && cryptographAES != null) {
                            sendJson = cryptographAES.encryptToAESJson(objectTransport);
                        }

                        String json = null;
                        if (id != null) {
                            json = jsonRequest.jsonRequest(id, "DELETE", sb.toString(), sendJson);
                        } else
                            json = jsonRequest.jsonRequest("DELETE", sb.toString(), sendJson);

                        Object obj = null;
                        if (useCryptographToReceiver == TypeCryptograph.RSA && cryptographRSA != null) {
                            if (json != null)
                                obj = json;
                        } else if (useCryptographToReceiver == TypeCryptograph.OTHER_RSA && otherCryptographRSA != null) {
                            if (json != null)
                                obj = json;
                        } else if (useCryptographToReceiver == TypeCryptograph.AES && cryptographAES != null) {
                            if (json != null)
                                obj = cryptographAES.decryptToJson(json);
                        }
                        Object finalObj = obj;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    response.onSucess(finalObj);
                                    response.onLoading(false);
                                } catch (ClassCastException es) {
                                    response.onSucess(finalObj);
                                    response.onLoading(false);
                                }
                                api.disconnect(id);
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    response.onError(e);
                                    response.onLoading(false);
                                } catch (ClassCastException es) {
                                    response.onError(es);
                                    response.onLoading(false);
                                }
                                api.disconnect(id);
                            }
                        });
                    }
                    break;
                }
            }
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        response.notConnectionInternet();
                        response.onLoading(false);
                    } catch (ClassCastException es) {
                        response.notConnectionInternet();
                        response.onLoading(false);
                    }
                    api.disconnect(id);
                }
            });
        }
    }

    public void onDestroyConnections(String activitySimpleName) {
        jsonRequest.onDestroyConnections(activitySimpleName);
    }

    public void disconnect() {
        HttpURLConnection con = jsonRequest.getConnections().get(id);
        HttpsURLConnection conSSl = jsonRequest.getConnectionsSSL().get(id);

        if (con != null)
            con.disconnect();

        if (conSSl != null)
            conSSl.disconnect();
    }

    private synchronized boolean haveConnection() {
        try {
            java.net.URL mandarMail = new java.net.URL("https://google.com");
            java.net.URLConnection conn = mandarMail.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.connect();
            int x = httpConn.getResponseCode();
            if (x == 200) {
                httpConn.disconnect();
                return true;
            } else {
                httpConn.disconnect();
            }
        } catch (java.net.MalformedURLException urlmal) {
            return false;
        } catch (java.io.IOException ioexcp) {
            return false;
        }
        return false;
    }

    public void setParamters(String params) {
        paramters = params;
    }

    public void setAuthorization(String authorization) {
        jsonRequest.setAuthorization(authorization);
    }

    @Override
    public void run() {
        request();
    }

    public static class TypeRequest {
        public static final int GET = 0;
        public static final int POST = 1;
        public static final int PUT = 2;
        public static final int DELETE = 3;
        public static final int UPDATE = 4;
    }

    public static class CryptographRSA {

        private RSA rsa;

        public CryptographRSA(String publicKeyBASE64) {
            try {
                byte[] objectBytes = Base64.getDecoder().decode(publicKeyBASE64);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(objectBytes));
                rsa = new RSA(publicKey, null);
            } catch (NoSuchAlgorithmException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (InvalidKeySpecException e) {
                if (api != null)
                    api.callbackExceptions(e);
            }
        }

        public final byte[] encrypt(Object object) {
            try {
                return rsa.encrypt(new Gson().toJson(object));
            } catch (InvalidKeyException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (UnsupportedEncodingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            }
            return null;
        }

        public final String encryptToJson(Object object) {
            try {
                return rsa.encryptToJson(object);
            } catch (InvalidKeyException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (UnsupportedEncodingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            }
            return null;
        }
    }

    public static class CryptographAES {

        private AES aes;

        public CryptographAES(String key) {
            aes = new AES();
            aes.setKey(key);
        }

        public byte[] encrypt(Object object) {
            try {
                return aes.encrypt(new Gson().toJson(object));
            } catch (InvalidKeyException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (UnsupportedEncodingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            }
            return null;
        }

        public String encryptToAESJson(Object object) {
            try {
                return aes.encryptToAESJson(object);
            } catch (InvalidKeyException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (UnsupportedEncodingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            }
            return null;
        }

        public Object decrypt(byte[] objectEncryptedJson) {
            Gson gson = new Gson();
            //byte[] buyerJsonCrypt = gson.fromJson(objectEncryptedJson, byte[].class);
            String buyerJson = null;
            try {
                buyerJson = new String(AES.decrypt(objectEncryptedJson));
            } catch (InvalidKeyException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                if (api != null)
                    api.callbackExceptions(e);
            }
            return gson.fromJson(buyerJson, Object.class);
        }

        public String decryptToJson(String objectEncryptedJson) {
            try {
                return AES.decryptToJson(objectEncryptedJson);
            } catch (InvalidKeyException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (NoSuchAlgorithmException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (BadPaddingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (IllegalBlockSizeException e) {
                if (api != null)
                    api.callbackExceptions(e);
            } catch (UnsupportedEncodingException e) {
                if (api != null)
                    api.callbackExceptions(e);
            }
            return null;
        }
    }

    public static class TypeCryptograph {
        public static final int RSA = 1;
        public static final int OTHER_RSA = 2;
        public static final int AES = 3;
    }
}
