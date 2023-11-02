package apis.internet;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InternetRequisitionAPI extends Thread {

    private int typeRequest, useCryptographToSend, useCryptographToReceiver, typeResponse ;
    private String idRequisition, domain, controller, function, paramters, authorization;
    private Object objectTransport ;

    private InternetRequisition request ;
    private InternetRequisition.CryptographRSA cryptographRSA ;
    private InternetRequisition.CryptographRSA otherCryptographRSA ;
    private InternetRequisition.CryptographAES cryptographAES ;

    private Map<String, String> curl = new HashMap<>();
    private Map<String, ExecutorService> requisitions = new HashMap<>();

    public InternetRequisition.CryptographRSA getCryptographRSA() {
        return cryptographRSA;
    }

    public void setCryptographRSA(InternetRequisition.CryptographRSA cryptographRSA) {
        this.cryptographRSA = cryptographRSA;
    }

    public InternetRequisition.CryptographRSA getOtherCryptographRSA() {
        return otherCryptographRSA;
    }

    public void setOtherCryptographRSA(InternetRequisition.CryptographRSA otherCryptographRSA) {
        this.otherCryptographRSA = otherCryptographRSA;
    }

    public InternetRequisition.CryptographAES getCryptographAES() {
        return cryptographAES;
    }

    public void setCryptographAES(InternetRequisition.CryptographAES cryptographAES) {
        this.cryptographAES = cryptographAES;
    }

    public String getIdRequisition() {
        return idRequisition;
    }

    public void setIdRequisition(String idRequisition) {
        this.idRequisition = idRequisition;
    }

    public int getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(int typeRequest) {
        this.typeRequest = typeRequest;
    }

    public int getTypeResponse() {
        return typeResponse;
    }

    public void setTypeResponse(int typeJson) {
        this.typeResponse = typeJson;
    }

    public int getUseCryptographToSend() {
        return useCryptographToSend;
    }

    public int getUseCryptographToReceiver() {
        return useCryptographToReceiver;
    }

    public void useCryptographToSend(int typeCryptograph) {
        this.useCryptographToSend = typeCryptograph;
    }

    public void useCryptographToReceiver(int typeCryptograph) {
        this.useCryptographToReceiver = typeCryptograph;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Object getObjectTransport() {
        return objectTransport;
    }

    public void setObjectTransport(Object objectTransport) {
        this.objectTransport = objectTransport;
    }

    public void setAuthorization(String authorization) {
        idRequisition = null ;
        typeRequest = 0 ;
        useCryptographToReceiver = 0 ;
        useCryptographToSend = 0 ;
        domain = null ;
        controller = null ;
        function = null ;
        paramters = null ;
        objectTransport = null ;

        this.authorization = authorization;
    }

    public synchronized void request(InternetRequisitionResponse response) {
        request = new InternetRequisition(this);
        request.setId(idRequisition);
        request.setAuthorization(authorization);
        request.setTypeRequest(typeRequest);
        request.setUseCryptographToSend(useCryptographToSend);
        request.setUseCryptographToReceiver(useCryptographToReceiver);
        request.setDomain(domain);
        request.setController(controller);
        request.setFunction(function);
        request.setParamters(paramters);
        request.setObjectTransport(objectTransport);
        request.setCryptographRSA(cryptographRSA);
        request.setOtherCryptographRSA(otherCryptographRSA);
        request.setCryptographAES(cryptographAES);
        request.setResponse(response);

        if (requisitions.containsKey(idRequisition))
            requisitions.get(idRequisition).shutdown();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(request);

        //request.request();

        requisitions.put(idRequisition, executor);

    }

    public synchronized void callbackExceptions(Exception exception){
        exception.printStackTrace();
    }

    public void addUrlParam(String param, String value){
        curl.put(param, value);
        paramters = getParams();
    }

    private String getParams(){
        StringBuilder sb = new StringBuilder();

        int count = 0 ;
        for(String param : curl.keySet()){
            if(count == 0){
                count++;
                sb.append("?");
                sb.append(param);
                sb.append("=");
                sb.append(curl.get(param));
            } else {
                sb.append("&");
                sb.append(param);
                sb.append("=");
                sb.append(curl.get(param));
            }
        }

        return sb.toString();
    }

    public void disconnect(String idRequisition){
        requisitions.get(idRequisition).shutdown();
    }
}
