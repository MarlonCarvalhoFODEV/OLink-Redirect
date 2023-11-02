/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.graphic;

import Itens.ITLogin;
import Itens.ITSearch;
import Itens.ITSocialChannel;
import Itens.ITSocialChannelContent;
import apis.encrypt.SHA256;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jfoenix.controls.JFXDialog;
import frames.account.UserAccount;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import system.OLinkConfiguration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import urls.UrlsConnetionsWEB;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import static webconnection.WebJson.haveConnection;

/**
 * @author Marlon
 */
public class DialogGraphicController extends OLinkConfiguration implements Initializable {

    private GraphicController graphicController;
    @FXML
    private PieChart chart;
    @FXML
    private ComboBox<String> cbLoadFromDate;

    private JFXDialog dialog;
    @FXML
    private BorderPane bpProgress;

    private int idChannel, dateType;

    private Map<String, String> orgNames = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDates();

        requestHistoricChannel("Este Mês");
    }

    private void setDates() {
        showProgress(true);

        ArrayList<String> datas = new ArrayList<>();
        datas.add("Este Mês");
        datas.add("Mês Passado");
        datas.add("Ultimos 3 Meses");
        datas.add("Ultimos 6 Meses");
        datas.add("Este Ano");
        datas.add("Ano Passado");
        datas.add("Ultimos 3 Anos");
        datas.add("Ultimos 6 Anos");

        cbLoadFromDate.setItems(FXCollections.observableArrayList(datas));
        cbLoadFromDate.valueProperty().addListener((observable, oldValue, newValue) -> requestHistoricChannel(newValue));

    }

    private void requestContentsFromDate(String value) {

        showProgress(true);

        switch (value) {
            case "Este Mês":
                dateType = 1;
                break;
            case "Mês Passado":
                dateType = 2;
                break;
            case "Ultimos 3 Meses":
                dateType = 3;
                break;
            case "Ultimos 6 Meses":
                dateType = 4;
                break;
            case "Este Ano":
                dateType = 5;
                break;
            case "Ano Passado":
                dateType = 6;
                break;
            case "3 Anos Anteriores":
                dateType = 7;
                break;
            case "6 Anos Anteriores":
                dateType = 8;
                break;
            default:
                ;
        }

        UserAccount ac = getAccount();
        ITLogin login = new ITLogin();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("idc", idChannel);
        send.put("dtp", dateType);
        send.put("sid", login.getSec().getSystemId());
        send.put("mac", login.getSec().getMac());

        orgNames.clear();

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("requestContentsFromDate");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ContentManager.CONTENTS);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Method.GET);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                    ITSocialChannelContent item;
                    ArrayList<ITSocialChannelContent> itens = new ArrayList<>();
                    for (JsonElement jsonE : array) {

                        item = gson.fromJson(jsonE, ITSocialChannelContent.class);
                        itens.add(item);
                        orgNames.put("C: " + item.getId(), item.getTitle());
                    }

                    ArrayList<PieChart.Data> newContents = graphicController.loadContentsFromChart(itens);

                    showProgress(false);
                    chart.getData().clear();
                    chart.getData().addAll(newContents);
                    chart.getData().stream().forEach((data) -> {
                        Node node = data.getNode();
                        node.setOnMouseClicked((e) -> {

                            ITSocialChannelContent nI = graphicController.getContents().get(data.getName());

                            if (nI != null) {
                                dialog.close();
                                graphicController.setConfigLinkChart(nI);
                                graphicController.getIndicator().setVisible(false);
                            }
                        });

                        node.setOnMouseEntered((e) -> {
                            GraphicController.setBorderNode(data.getNode(), true);
                            chart.setTitle(orgNames.get(data.getName()));
                        });

                        node.setOnMouseExited((e) -> {
                            GraphicController.setBorderNode(data.getNode(), false);
                            chart.setTitle("Conteúdos");
                        });
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                showProgress(false);
            }

            @Override
            public void notConnectionInternet() {
                new DialogNoConnection(graphicController.getStackPane()).show(true);
            }
        });

       /* if (haveConnection()) {
            showProgress(true);

            StringBuilder sb = new StringBuilder();
            sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            sb.append(UrlsConnetionsWEB.ContentManager.CONTENTS);
            sb.append(UrlsConnetionsWEB.ContentManager.Method.GET);

            switch (value) {
                case "Este Mês":
                    dateType = 1;
                    break;
                case "Mês Passado":
                    dateType = 2;
                    break;
                case "Ultimos 3 Meses":
                    dateType = 3;
                    break;
                case "Ultimos 6 Meses":
                    dateType = 4;
                    break;
                case "Este Ano":
                    dateType = 5;
                    break;
                case "Ano Passado":
                    dateType = 6;
                    break;
                case "3 Anos Anteriores":
                    dateType = 7;
                    break;
                case "6 Anos Anteriores":
                    dateType = 8;
                    break;
                default:
                    sb.append(0);
            }

            Thread th = new Thread(getThreadGroup(), () -> {
                ITSearch search = new ITSearch();
                search.setIdUser(getAccount().getId());
                search.setIdChannel(idChannel);
                search.setDateType(dateType);
                search.setCategory("");
                search.setSearch("");

                orgNames.clear();

                String json = new WebJson().postJson(sb.toString(), search);

                Gson gson = new Gson();
                JsonArray array = gson.fromJson(json, JsonArray.class);

                ITSocialChannelContent item;
                ArrayList<ITSocialChannelContent> itens = new ArrayList<>();
                for (JsonElement jsonE : array) {

                    item = gson.fromJson(jsonE, ITSocialChannelContent.class);
                    itens.add(item);
                    orgNames.put("C: "+item.getId(), item.getTitle());
                }



                ArrayList<PieChart.Data> newContents = graphicController.loadContentsFromChart(itens);

                Platform.runLater(() -> {
                    showProgress(false);
                    chart.getData().clear();
                    chart.getData().addAll(newContents);
                    chart.getData().stream().forEach((data) -> {
                        Node node = data.getNode();
                        node.setOnMouseClicked((e) -> {

                            ITSocialChannelContent nI = graphicController.getContents().get(data.getName());

                            if (nI != null) {
                                dialog.close();
                                graphicController.setConfigLinkChart(nI);
                                graphicController.getIndicator().setVisible(false);
                            }
                        });

                        node.setOnMouseEntered((e) -> {
                            GraphicController.setBorderNode(data.getNode(), true);
                            chart.setTitle(orgNames.get(data.getName()));
                        });

                        node.setOnMouseExited((e) -> {
                            GraphicController.setBorderNode(data.getNode(), false);
                            chart.setTitle("Conteúdos");
                        });
                    });
                });
            });
            th.start();
        } else {
            new DialogNoConnection(graphicController.getStackPane()).show(true);
        }*/
    }

    private void requestHistoricChannel(String dateRequest) {

        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.submit(()->{
            showProgress(true);
            dateType = decodeType(dateRequest);

            UserAccount ac = getAccount();
            ITLogin login = new ITLogin();

            JSONObject send = new JSONObject();
            send.put("idu", ac.getId());
            send.put("oip", ac.getSec().getIpPublicFirst());
            send.put("mac", ac.getSec().getMac());
            send.put("sid", ac.getSec().getSystemId());
            send.put("idc", getSelectedChannel().getId());
            send.put("dtp", dateType);
            send.put("src", "");
            send.put("cat", "");

            orgNames.clear();

            InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
            requestServer.setIdRequisition("requestContentsFromDate");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            requestServer.setController(UrlsConnetionsWEB.ContentManager.CONTENTS);
            requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Method.GET);
            requestServer.setObjectTransport(send);
            requestServer.request(new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {
                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(() -> {
                            Gson gson = new Gson();
                            JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                            ITSocialChannelContent item;
                            ArrayList<ITSocialChannelContent> itens = new ArrayList<>();
                            for (JsonElement jsonE : array) {

                                item = gson.fromJson(jsonE, ITSocialChannelContent.class);
                                itens.add(item);
                                orgNames.put("C: " + item.getId(), item.getTitle());
                            }

                            ArrayList<PieChart.Data> newContents = graphicController.loadContentsFromChart(itens);

                            Platform.runLater(() -> {
                                showProgress(false);

                                chart.getData().clear();
                                chart.getData().addAll(newContents);
                                chart.getData().stream().forEach((data) -> {
                                    Node node = data.getNode();
                                    node.setOnMouseClicked((e) -> {

                                        ITSocialChannelContent nI = graphicController.getContents().get(data.getName());

                                        if (nI != null) {
                                            dialog.close();
                                            graphicController.setConfigLinkChart(nI);
                                            graphicController.getIndicator().setVisible(false);
                                        }
                                    });

                                    node.setOnMouseEntered((e) -> {
                                        GraphicController.setBorderNode(data.getNode(), true);
                                        chart.setTitle(orgNames.get(data.getName()));
                                    });

                                    node.setOnMouseExited((e) -> {
                                        GraphicController.setBorderNode(data.getNode(), false);
                                        chart.setTitle("Conteúdos");
                                    });
                                });
                                executor.shutdown();
                            });
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    showProgress(false);
                }

                @Override
                public void notConnectionInternet() {
                    new DialogNoConnection(graphicController.getStackPane()).show(true);
                }
            });

        });

       /* if (haveConnection()) {
            showProgress(true);

            StringBuilder sb = new StringBuilder();
            sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
            sb.append(UrlsConnetionsWEB.ContentManager.CONTENTS);
            sb.append(UrlsConnetionsWEB.ContentManager.Method.GET);

            switch (value) {
                case "Este Mês":
                    dateType = 1;
                    break;
                case "Mês Passado":
                    dateType = 2;
                    break;
                case "Ultimos 3 Meses":
                    dateType = 3;
                    break;
                case "Ultimos 6 Meses":
                    dateType = 4;
                    break;
                case "Este Ano":
                    dateType = 5;
                    break;
                case "Ano Passado":
                    dateType = 6;
                    break;
                case "3 Anos Anteriores":
                    dateType = 7;
                    break;
                case "6 Anos Anteriores":
                    dateType = 8;
                    break;
                default:
                    sb.append(0);
            }

            Thread th = new Thread(getThreadGroup(), () -> {
                ITSearch search = new ITSearch();
                search.setIdUser(getAccount().getId());
                search.setIdChannel(idChannel);
                search.setDateType(dateType);
                search.setCategory("");
                search.setSearch("");

                orgNames.clear();

                String json = new WebJson().postJson(sb.toString(), search);

                Gson gson = new Gson();
                JsonArray array = gson.fromJson(json, JsonArray.class);

                ITSocialChannelContent item;
                ArrayList<ITSocialChannelContent> itens = new ArrayList<>();
                for (JsonElement jsonE : array) {

                    item = gson.fromJson(jsonE, ITSocialChannelContent.class);
                    itens.add(item);
                    orgNames.put("C: "+item.getId(), item.getTitle());
                }



                ArrayList<PieChart.Data> newContents = graphicController.loadContentsFromChart(itens);

                Platform.runLater(() -> {
                    showProgress(false);
                    chart.getData().clear();
                    chart.getData().addAll(newContents);
                    chart.getData().stream().forEach((data) -> {
                        Node node = data.getNode();
                        node.setOnMouseClicked((e) -> {

                            ITSocialChannelContent nI = graphicController.getContents().get(data.getName());

                            if (nI != null) {
                                dialog.close();
                                graphicController.setConfigLinkChart(nI);
                                graphicController.getIndicator().setVisible(false);
                            }
                        });

                        node.setOnMouseEntered((e) -> {
                            GraphicController.setBorderNode(data.getNode(), true);
                            chart.setTitle(orgNames.get(data.getName()));
                        });

                        node.setOnMouseExited((e) -> {
                            GraphicController.setBorderNode(data.getNode(), false);
                            chart.setTitle("Conteúdos");
                        });
                    });
                });
            });
            th.start();
        } else {
            new DialogNoConnection(graphicController.getStackPane()).show(true);
        }*/
    }

    private int decodeType(String dateRequest) {
        switch (dateRequest) {
            case "Este Mês":
                dateType = 1;
                break;
            case "Mês Passado":
                dateType = 2;
                break;
            case "Ultimos 3 Meses":
                dateType = 3;
                break;
            case "Ultimos 6 Meses":
                dateType = 4;
                break;
            case "Este Ano":
                dateType = 5;
                break;
            case "Ano Passado":
                dateType = 6;
                break;
            case "3 Anos Anteriores":
                dateType = 7;
                break;
            case "6 Anos Anteriores":
                dateType = 8;
                break;
            default:
                ;
        }
        return dateType;
    }

    public synchronized ArrayList<PieChart.Data> loadContentsFromChart() {
        try {

            ExecutorService exec = Executors.newSingleThreadExecutor();
            Future<ArrayList<PieChart.Data>> fut = exec.submit(new Callable<ArrayList<PieChart.Data>>() {
                @Override
                public ArrayList<PieChart.Data> call() throws Exception {
                    ArrayList<PieChart.Data> series = new ArrayList<>();

                    graphicController.getContents().forEach((content, val) -> {
                        PieChart.Data data = new PieChart.Data("C: " + val.getId(), val.getCountClickTotal());
                        orgNames.put("C: " + val.getId(), val.getTitle());
                        series.add(data);
                    });

                    return series;
                }
            });

            return fut.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(GraphicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void setConfigurations() {
        ArrayList<PieChart.Data> newContents = loadContentsFromChart();

        chart.getData().addAll(newContents);
        chart.getData().stream().forEach((data) -> {
            Node node = data.getNode();
            node.setOnMouseClicked((e) -> {
                ITSocialChannelContent item = graphicController.getContents().get(data.getName());
                if (item != null) {
                    dialog.close();
                    graphicController.setConfigLinkChart(item);
                    graphicController.getIndicator().setVisible(false);
                }
            });

            node.setOnMouseEntered((e) -> {
                GraphicController.setBorderNode(data.getNode(), true);
                chart.setTitle(orgNames.get(data.getName()));
            });

            node.setOnMouseExited((e) -> {
                GraphicController.setBorderNode(data.getNode(), false);
                chart.setTitle("Conteúdos");
            });
        });
        showProgress(false);
    }

    public void setGraphicController(GraphicController controller) {
        this.graphicController = controller;
    }

    public void setDialog(JFXDialog dialog) {
        this.dialog = dialog;
    }

    private void showProgress(boolean show) {
        bpProgress.setVisible(show);
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }
}
