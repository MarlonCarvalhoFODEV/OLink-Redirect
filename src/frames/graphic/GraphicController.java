/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames.graphic;

import Itens.ITDay;
import Itens.ITHour;
import Itens.ITLinkHistoric;
import Itens.ITLink;
import Itens.ITMonth;
import Itens.ITReceptor;
import Itens.ITSocialChannelContent;
import Itens.ITYear;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import com.google.gson.JsonElement;
import frames.account.UserAccount;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.json.JSONObject;
import system.OLinkConfiguration;
import util.DecodeChannelID;
import util.LKImageManager;

import static system.OLinkConfiguration.*;
import static webconnection.WebJson.haveConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import urls.UrlsConnetionsWEB;
import util.Toast;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

/**
 * @author Marlon
 */
public class GraphicController implements Initializable {

    @FXML
    private ImageView ivGraphic;
    @FXML
    private StackedBarChart<String, Integer> chart;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label lbContentName, lbCountClick;

    @FXML
    private BorderPane bpLoading;

    @FXML
    private AnchorPane ap0, ap1, ap2, ap3;
    private int step = 0;

    private ArrayList<ITSocialChannelContent> contents;
    private final Map<String, ITSocialChannelContent> orgContent = new HashMap<>();
    private final Map<String, ITLink> orgLink = new HashMap<>();
    private final Map<String, ITYear> orgYear = new HashMap<>();
    private final Map<String, ITMonth> orgMonth = new HashMap<>();
    private final Map<String, ITDay> orgDay = new HashMap<>();
    private final Map<String, ITHour> orgHours = new HashMap<>();

    @FXML
    private Label lbDesemp;
    @FXML
    private AnchorPane apIndicator;

    private Map<Integer, Object> historic = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setConfigSteps();
    }

    private void setConfigSteps() {
        ap0.setOnMouseClicked(mouseEvent -> {
            step = 0;
            getSetYearHitoric((ITLink) historic.get(step));
        });
        ap1.setOnMouseClicked(mouseEvent -> {
            step = 1;
            getSetMonthHistoric((ITYear) historic.get(step));
        });
        ap2.setOnMouseClicked(mouseEvent -> {
            step = 2;
            getSetDayHistoric((ITMonth) historic.get(step));
        });
        ap3.setOnMouseClicked(mouseEvent -> {
            step = 3;
            getSetHourHistoric((ITDay) historic.get(step));
        });
    }

    public void setContents(ArrayList<ITSocialChannelContent> contents) {
        this.contents = contents;
        this.contents.forEach((item) -> {
            orgContent.put("C: " + item.getId(), item);
        });
    }

    private void setHistoricHourLink(ITDay d) {
        step = 3;
        setStep(step);

        lbCountClick.setText(String.valueOf(d.getTotalResult()));
        //lbDesemp.setText("Relatório Diário");
        chart.getXAxis().setLabel("Períodos em 24 Horas");
        //chart.getXAxis().setAnimated(true);
        chart.getYAxis().setLabel("Taxa de Cliques por Hora");
        chart.getData().clear();

        ArrayList<XYChart.Series<String, Integer>> seriesLoad = loadHourFromChart(new ArrayList<>(d.values()));
        Platform.runLater(() -> {
            chart.getData().addAll(seriesLoad);
            chart.requestFocus();
            chart.requestLayout();
        });
    }

    private void setHistoricDayLink(ITMonth m) {
        step = 2;
        setStep(step);

        lbCountClick.setText(String.valueOf(m.getTotalResult()));

        //lbDesemp.setText("Relatórios Diários");
        chart.getXAxis().setLabel("Períodos em Dias");
        //chart.getXAxis().setAnimated(true);
        chart.getYAxis().setLabel("Taxa de Cliques Diários");
        //chart.getYAxis().setAnimated(true);
        chart.getData().clear();

        ArrayList<XYChart.Series<String, Integer>> seriesLoad = loadDayFromChart(new ArrayList<>(m.values()));

        Platform.runLater(() -> {
            chart.getData().addAll(seriesLoad);
            chart.requestFocus();
            chart.requestLayout();
            chart.getData().forEach((series) -> {
                series.getData().forEach((data) -> {
                    Node node = data.getNode();
                    node.setOnMouseClicked((e) -> {
                        step = 3;
                        historic.put(step, orgDay.get(data.getXValue()));
                        ITDay day = orgDay.get(data.getXValue());
                        day.setMonth(m.getMonth());
                        getSetHourHistoric(day);
                    });

                    node.setOnMouseEntered((e) -> {
                        setBorderNode(node, true);
                    });

                    node.setOnMouseExited((e) -> {
                        setBorderNode(node, false);
                    });
                });
            });
        });

    }

    private void setHistoricMonthLink(ITYear y) {
        step = 1;
        setStep(step);

        lbCountClick.setText(String.valueOf(y.getTotalResult()));

        //lbDesemp.setText("Relatórios Mensais");
        chart.getXAxis().setLabel("Períodos em Meses");
        //chart.getXAxis().setAnimated(true);
        chart.getYAxis().setLabel("Taxa de Cliques Mensais");
        //chart.getYAxis().setAnimated(true);
        chart.getData().clear();

        ArrayList<XYChart.Series<String, Integer>> seriesLoad = loadMonthFromChart(new ArrayList<>(y.values()));

        Platform.runLater(() -> {
            chart.getData().addAll(seriesLoad);
            chart.requestFocus();
            chart.requestLayout();
            chart.getData().forEach((series) -> {
                series.getData().forEach((data) -> {
                    Node node = data.getNode();
                    node.setOnMouseClicked((e) -> {
                        step = 2;
                        historic.put(step, orgMonth.get(data.getXValue()));
                        getSetDayHistoric(orgMonth.get(data.getXValue()));
                    });

                    node.setOnMouseEntered((e) -> {
                        setBorderNode(node, true);
                    });

                    node.setOnMouseExited((e) -> {
                        setBorderNode(node, false);
                    });
                });

            });
        });
    }

    private void setHistoricYear(ITLinkHistoric h) {
        step = 0;
        setStep(step);

        lbCountClick.setText(String.valueOf(h.getTotalResult()));

        //lbDesemp.setText("Relatórios Anuais");
        chart.getXAxis().setLabel("Poríodos em anos");
        //chart.getXAxis().setAnimated(true);
        chart.getYAxis().setLabel("Taxa de Cliques Anuais");
        chart.getData().clear();

        ArrayList<XYChart.Series<String, Integer>> seriesLoad = loadYearFromChart(new ArrayList<>(h.values()));

        Platform.runLater(() -> {
            chart.getData().addAll(seriesLoad);
            chart.requestFocus();
            chart.requestLayout();

            chart.getData().forEach((series) -> {
                series.getData().forEach((data) -> {
                    Node node = data.getNode();
                    node.setOnMouseClicked((e) -> {
                        step = 1;
                        historic.put(step, orgYear.get(data.getXValue()));
                        getSetMonthHistoric(orgYear.get(data.getXValue()));
                    });

                    node.setOnMouseEntered((e) -> {
                        setBorderNode(node, true);
                    });

                    node.setOnMouseExited((e) -> {
                        setBorderNode(node, false);
                    });
                });

            });
        });
    }

    private void getSetYearHitoric(ITLink link) {

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("idc", link.getIdChannel());
        send.put("idl", link.getId());
        send.put("idt", link.getIdContent());

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("getSetYearHitoric");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ContentManager.Link.LINKS);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Link.Method.GET_YEAR_HISTORIC);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
                showProgress(loading);
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                    ITLinkHistoric historic = new ITLinkHistoric();

                    array.forEach(el -> {
                        ITReceptor rec = gson.fromJson(el, ITReceptor.class);
                        historic.addReceptorYear(rec);
                    });

                    if (historic != null) {
                        setHistoricYear(historic);
                    } else {
                        Stage stage = (Stage) ivGraphic.getScene().getWindow();
                        Toast.makeText(stage, "Erro ao carregar historico", 0, 500, 5000, true);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                showProgress(false);
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                showProgress(false);
                new DialogNoConnection(stackPane).show(true);
            }
        });

       /* if (haveConnection()) {
            if (link != null) {
                showProgress(true);

                Thread th = new Thread(getThreadGroup(), () -> {

                    StringBuilder sb = new StringBuilder();
                    sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                    sb.append(UrlsConnetionsWEB.ContentManager.Link.LINKS);
                    sb.append(UrlsConnetionsWEB.ContentManager.Link.Method.GET_YEAR_HISTORIC);

                    ITLink linkPost = new ITLink();
                    linkPost.setId(link.getId());
                    linkPost.setIdUser(link.getIdUser());
                    linkPost.setIdChannel(link.getIdChannel());
                    linkPost.setIdContent(link.getIdContent());

                    String json = new WebJson().jsonRequest("POST", sb.toString(), linkPost);

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(json, JsonArray.class);

                    ITLinkHistoric historic = new ITLinkHistoric();

                    array.forEach(el -> {
                        ITReceptor rec = gson.fromJson(el, ITReceptor.class);
                        historic.addReceptorYear(rec);
                    });

                    Platform.runLater(() -> {
                        showProgress(false);

                        if (historic != null) {
                            setHistoricYear(historic);
                        } else {
                            Stage stage = (Stage) ivGraphic.getScene().getWindow();
                            Toast.makeText(stage, "Erro ao carregar historico", 0, 500, 5000, true);
                        }
                    });
                });
                th.start();
            }
        } else {
            new DialogNoConnection(stackPane).show(true);
        }*/
    }

    private void getSetMonthHistoric(ITYear year) {
        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("idc", year.getIdChannel());
        send.put("idl", year.getIdLink());
        send.put("idt", year.getIdContent());

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("getSetMonthHistoric");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ContentManager.Link.LINKS);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Link.Method.GET_MONTH_HISTORIC);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
                showProgress(loading);
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                    ITYear newYear = new ITYear();

                    array.forEach(el -> {
                        ITReceptor rec = gson.fromJson(el, ITReceptor.class);
                        newYear.addReceptorMonth(rec);
                    });

                    if (newYear != null) {
                        setHistoricMonthLink(newYear);
                    } else {
                        Stage stage = (Stage) ivGraphic.getScene().getWindow();
                        Toast.makeText(stage, "Erro ao carregar historico", 0, 500, 5000, true);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                showProgress(false);
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                showProgress(false);
                new DialogNoConnection(stackPane).show(true);
            }
        });

       /* if (haveConnection()) {
            showProgress(true);

            Thread th = new Thread(getThreadGroup(), () -> {
                StringBuilder sb = new StringBuilder();
                sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                sb.append(UrlsConnetionsWEB.ContentManager.Link.LINKS);
                sb.append(UrlsConnetionsWEB.ContentManager.Link.Method.GET_MONTH_HISTORIC);

                ITLink linkPost = new ITLink();
                linkPost.setId(year.getIdLink());
                linkPost.setIdUser(year.getIdUser());
                linkPost.setIdChannel(year.getIdChannel());
                linkPost.setIdContent(year.getIdContent());

                String json = new WebJson().jsonRequest("POST", sb.toString(), linkPost);

                Gson gson = new Gson();
                JsonArray array = gson.fromJson(json, JsonArray.class);

                ITYear newYear = new ITYear();

                array.forEach(el -> {
                    ITReceptor rec = gson.fromJson(el, ITReceptor.class);
                    newYear.addReceptorMonth(rec);
                });

                Platform.runLater(() -> {
                    showProgress(false);
                    if (newYear != null) {
                        setHistoricMonthLink(newYear);
                    } else {
                        Stage stage = (Stage) ivGraphic.getScene().getWindow();
                        Toast.makeText(stage, "Erro ao carregar historico", 0, 500, 5000, true);
                    }
                });
            });
            th.start();
        } else {
            new DialogNoConnection(stackPane).show(true);
        }*/
    }

    private void getSetDayHistoric(ITMonth monthReferer) {

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("idc", monthReferer.getIdChannel());
        send.put("idl", monthReferer.getIdLink());
        send.put("idt", monthReferer.getIdContent());
        send.put("mth", monthReferer.getMonth());

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("getSetDayHistoric");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ContentManager.Link.LINKS);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Link.Method.GET_DAY_HISTORIC);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
                showProgress(loading);
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                    ITMonth month = new ITMonth();
                    month.setMonth(monthReferer.getMonth());

                    array.forEach(el -> {
                        ITReceptor rec = gson.fromJson(el, ITReceptor.class);
                        month.addReceptorDay(rec);
                    });

                    showProgress(false);
                    if (month != null) {
                        setHistoricDayLink(month);
                    } else {
                        Stage stage = (Stage) ivGraphic.getScene().getWindow();
                        Toast.makeText(stage, "Erro ao carregar historico", 0, 500, 5000, true);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                showProgress(false);
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                showProgress(false);
                new DialogNoConnection(stackPane).show(true);
            }
        });

      /*  if (haveConnection()) {
            showProgress(true);

            Thread th = new Thread(getThreadGroup(), () -> {
                StringBuilder sb = new StringBuilder();
                sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                sb.append(UrlsConnetionsWEB.ContentManager.Link.LINKS);
                sb.append(UrlsConnetionsWEB.ContentManager.Link.Method.GET_DAY_HISTORIC);

                ITReceptor recept = new ITReceptor();
                recept.setIdLink(monthReferer.getIdLink());
                recept.setIdUser(monthReferer.getIdUser());
                recept.setIdChannel(monthReferer.getIdChannel());
                recept.setIdContent(monthReferer.getIdContent());
                recept.setMonth(monthReferer.getMonth());

                String json = new WebJson().jsonRequest("POST", sb.toString(), recept);

                Gson gson = new Gson();
                JsonArray array = gson.fromJson(json, JsonArray.class);

                ITMonth month = new ITMonth();
                month.setMonth(monthReferer.getMonth());

                array.forEach(el -> {
                    ITReceptor rec = gson.fromJson(el, ITReceptor.class);
                    month.addReceptorDay(rec);
                });

                Platform.runLater(() -> {
                    showProgress(false);
                    if (month != null) {
                        setHistoricDayLink(month);
                    } else {
                        Stage stage = (Stage) ivGraphic.getScene().getWindow();
                        Toast.makeText(stage, "Erro ao carregar historico", 0, 500, 5000, true);
                    }
                });
            });
            th.start();
        } else {
            new DialogNoConnection(stackPane).show(true);
        }*/
    }

    private void getSetHourHistoric(ITDay day) {

        UserAccount ac = getAccount();

        JSONObject send = new JSONObject();
        send.put("idu", ac.getId());
        send.put("oip", ac.getSec().getIpPublicFirst());
        send.put("mac", ac.getSec().getMac());
        send.put("sid", ac.getSec().getSystemId());
        send.put("idc", day.getIdChannel());
        send.put("idl", day.getIdLink());
        send.put("idt", day.getIdContent());
        send.put("mth", day.getMonth());
        send.put("day", day.getDay());

        InternetRequisitionAPI requestServer = OLinkConfiguration.getApiRequisition();
        requestServer.setIdRequisition("getSetHourHistoric");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.OTHER_RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(UrlsConnetionsWEB.DOMAIN_CONNECTION);
        requestServer.setController(UrlsConnetionsWEB.ContentManager.Link.LINKS);
        requestServer.setFunction(UrlsConnetionsWEB.ContentManager.Link.Method.GET_HOUR_HISTORIC);
        requestServer.setObjectTransport(send);
        requestServer.request(new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
                showProgress(loading);
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    Gson gson = new Gson();
                    JsonArray array = gson.fromJson(object.toString(), JsonArray.class);

                    ITDay newDay = new ITDay();

                    array.forEach(el -> {
                        ITReceptor rec = gson.fromJson(el, ITReceptor.class);
                        newDay.addReceptorHour(rec);
                    });

                    if (newDay != null) {
                        setHistoricHourLink(newDay);

                    } else {
                        Stage stage = (Stage) ivGraphic.getScene().getWindow();
                        Toast.makeText(stage, "Erro ao carregar historico", 0, 500, 5000, true);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                showProgress(false);
                e.printStackTrace();
            }

            @Override
            public void notConnectionInternet() {
                showProgress(false);
                new DialogNoConnection(stackPane).show(true);
            }
        });

        /*if (haveConnection()) {
            showProgress(true);
            Thread th = new Thread(getThreadGroup(), () -> {
                StringBuilder sb = new StringBuilder();
                sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                sb.append(UrlsConnetionsWEB.ContentManager.Link.LINKS);
                sb.append(UrlsConnetionsWEB.ContentManager.Link.Method.GET_HOUR_HISTORIC);

                ITReceptor recept = new ITReceptor();
                recept.setIdLink(day.getIdLink());
                recept.setIdUser(day.getIdUser());
                recept.setIdChannel(day.getIdChannel());
                recept.setIdContent(day.getIdContent());
                recept.setMonth(day.getMonth());
                recept.setDay(day.getDay());

                String json = new WebJson().jsonRequest("POST", sb.toString(), recept);

                Gson gson = new Gson();
                JsonArray array = gson.fromJson(json, JsonArray.class);

                ITDay newDay = new ITDay();

                array.forEach(el -> {
                    ITReceptor rec = gson.fromJson(el, ITReceptor.class);
                    newDay.addReceptorHour(rec);
                });

                Platform.runLater(() -> {
                    showProgress(false);
                    if (newDay != null) {
                        setHistoricHourLink(newDay);

                    } else {
                        Stage stage = (Stage) ivGraphic.getScene().getWindow();
                        Toast.makeText(stage, "Erro ao carregar historico", 0, 500, 5000, true);
                    }
                });
            });
            th.start();
        } else {
            new DialogNoConnection(stackPane).show(true);
        }*/
    }

    private synchronized ArrayList<XYChart.Series<String, Integer>> loadLinkFromChart(List<ITLink> links) {
        try {
            ArrayList<XYChart.Series<String, Integer>> series = new ArrayList<>();

            ExecutorService exec = Executors.newSingleThreadExecutor();
            Future<Boolean> fut = exec.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    links.forEach((link) -> {
                        orgLink.put(link.getLinkName(), link);
                        XYChart.Data data = new XYChart.Data(String.valueOf(link.getLinkName()), (link.getCountClick() + link.getCountSale()));
                        data.setNode(createLabel((String.valueOf(link.getCountClick() + link.getCountSale()))));

                        XYChart.Series serie = new XYChart.Series<>();
                        serie.setName(link.getLinkName());
                        serie.getData().add(data);
                        series.add(serie);
                    });
                    return true;
                }
            });

            if (fut.get()) {
                return series;
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(GraphicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private synchronized ArrayList<XYChart.Series<String, Integer>> loadYearFromChart(List<ITYear> years) {
        try {
            ArrayList<XYChart.Series<String, Integer>> series = new ArrayList<>();

            ExecutorService exec = Executors.newSingleThreadExecutor();
            Future<Boolean> fut = exec.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() {

                    years.forEach((year) -> {
                        orgYear.put(year.getYear() + " Ano", year);
                        XYChart.Data data = new XYChart.Data(year.getYear() + " Ano", year.getTotalResult());
                        data.setNode(createLabel(String.valueOf(year.getTotalResult())));

                        XYChart.Series serie = new XYChart.Series<>();
                        serie.setName(year.getYear() + " Ano");
                        serie.getData().add(data);

                        series.add(serie);
                    });
                    return true;
                }
            });

            if (fut.get()) {
                fut.cancel(true);
                return series;
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(GraphicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        label.setTextFill(Paint.valueOf("#FFFFFF"));
        return label;
    }

    private synchronized ArrayList<XYChart.Series<String, Integer>> loadMonthFromChart(List<ITMonth> months) {
        try {

            ArrayList<XYChart.Series<String, Integer>> series = new ArrayList<>();

            ExecutorService exec = Executors.newSingleThreadExecutor();
            Future<Boolean> fut = exec.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() {

                    months.forEach((month) -> {
                        orgMonth.put(month.getMonthName(), month);
                        XYChart.Data data = new XYChart.Data(month.getMonthName(), month.getTotalResult());
                        data.setNode(createLabel(String.valueOf(month.getTotalResult())));

                        XYChart.Series serie = new XYChart.Series<>();
                        serie.setName(month.getMonthName());
                        serie.getData().add(data);
                        series.add(serie);
                    });
                    return true;
                }
            });

            if (fut.get()) {
                fut.cancel(true);
                return series;
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(GraphicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private synchronized ArrayList<XYChart.Series<String, Integer>> loadDayFromChart(List<ITDay> days) {
        try {
            ArrayList<XYChart.Series<String, Integer>> series = new ArrayList<>();

            ExecutorService exec = Executors.newSingleThreadExecutor();
            Future<Boolean> fut = exec.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() {

                    days.forEach((day) -> {
                        orgDay.put("Dia " + day.getDay(), day);
                        XYChart.Data data = new XYChart.Data("Dia " + day.getDay(), day.getTotalResult());
                        data.setNode(createLabel(String.valueOf(day.getTotalResult())));

                        XYChart.Series<String, Integer> serie = new XYChart.Series<>();
                        serie.setName("Dia " + day.getDay());
                        serie.getData().add(data);

                        series.add(serie);
                    });

                    return true;
                }
            });

            if (fut.get()) {
                fut.cancel(true);
                return series;
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(GraphicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private synchronized ArrayList<XYChart.Series<String, Integer>> loadHourFromChart(List<ITHour> hours) {
        try {
            Collections.sort(hours);
            ArrayList<XYChart.Series<String, Integer>> series = new ArrayList<>();

            ExecutorService exec = Executors.newSingleThreadExecutor();
            Future<Boolean> fut = exec.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    hours.forEach((hour) -> {
                        orgHours.put(String.valueOf(hour.getHour() + " HR"), hour);
                        XYChart.Data data = new XYChart.Data(hour.getHour() + " HR", hour.getTotalResult());
                        data.setNode(createLabel(String.valueOf(hour.getTotalResult())));

                        XYChart.Series<String, Integer> serie = new XYChart.Series<>();
                        serie.setName(String.valueOf(hour.getHour() + " HR"));
                        serie.getData().add(data);

                        series.add(serie);
                    });
                    return true;
                }
            });

            if (fut.get()) {
                fut.cancel(true);
                return series;
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(GraphicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public synchronized ArrayList<PieChart.Data> loadContentsFromChart(List<ITSocialChannelContent> contents) {
        try {
            ArrayList<PieChart.Data> series = new ArrayList<>();

            ExecutorService exec = Executors.newSingleThreadExecutor();
            Future<Boolean> fut = exec.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    contents.forEach((content) -> {
                        PieChart.Data data = new PieChart.Data("C: " + content.getId(), content.getCountClickTotal());

                        series.add(data);
                    });
                    return true;
                }
            });

            if (fut.get()) {
                fut.cancel(true);
                return series;
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(GraphicController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Map<String, ITSocialChannelContent> getOrgContent() {
        return orgContent;
    }

    public void setConfigLinkChart(ITSocialChannelContent c) {
        ap0.setDisable(false);
        ((ImageView) ap0.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-first_disable.png").getImage());
        ap1.setDisable(true);
        ((ImageView) ap1.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle_disable.png").getImage());
        ap2.setDisable(true);
        ((ImageView) ap2.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle_disable.png").getImage());
        ap3.setDisable(true);
        ((ImageView) ap3.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-final_disable.png").getImage());

        lbDesemp.setText("Desempenho de Conteúdo");
        lbContentName.setText(c.getTitle());
        lbCountClick.setText(String.valueOf(c.getCountClickTotal() + c.getCountSale()));

        chart.getXAxis().setLabel("Links do Conteúdo");
        chart.getYAxis().setLabel("Cliques Por Link");
        chart.getData().clear();
        chart.getData().addAll(loadLinkFromChart(c.getLinks()));

        Platform.runLater(() -> {

            chart.getData().forEach((series) -> {
                series.getData().forEach((data) -> {
                    Node node = data.getNode();
                    node.setOnMouseClicked((e) -> {
                        //historic.put(step, orgLink.get(data.getXValue()));
                        historic.put(step, orgLink.get(data.getXValue()));
                        getSetYearHitoric(orgLink.get(data.getXValue()));
                    });

                    node.setOnMouseEntered((e) -> {
                        setBorderNode(node, true);
                    });

                    node.setOnMouseExited((e) -> {
                        setBorderNode(node, false);
                    });
                });
            });
        });
    }

    public AnchorPane getIndicator() {
        return apIndicator;
    }

    private void setStep(int step) {

        switch (step) {
            case 0: {
                ap0.setDisable(false);
                ((ImageView) ap0.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-first.png").getImage());
                ap1.setDisable(true);
                ((ImageView) ap1.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle_disable.png").getImage());
                ap2.setDisable(true);
                ((ImageView) ap2.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle_disable.png").getImage());
                ap3.setDisable(true);
                ((ImageView) ap3.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-final_disable.png").getImage());
                break;
            }
            case 1: {
                ap0.setDisable(false);
                ((ImageView) ap0.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-first.png").getImage());
                ap1.setDisable(false);
                ((ImageView) ap1.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle.png").getImage());
                ap2.setDisable(true);
                ((ImageView) ap2.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle_disable.png").getImage());
                ap3.setDisable(true);
                ((ImageView) ap3.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-final_disable.png").getImage());
                break;
            }
            case 2: {
                ap0.setDisable(false);
                ((ImageView) ap0.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-first.png").getImage());
                ap1.setDisable(false);
                ((ImageView) ap1.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle.png").getImage());
                ap2.setDisable(false);
                ((ImageView) ap2.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle.png").getImage());
                ap3.setDisable(true);
                ((ImageView) ap3.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-final_disable.png").getImage());
                break;
            }
            case 3: {
                ap0.setDisable(false);
                ((ImageView) ap0.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-first.png").getImage());
                ap1.setDisable(false);
                ((ImageView) ap1.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle.png").getImage());
                ap2.setDisable(false);
                ((ImageView) ap2.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-midle.png").getImage());
                ap3.setDisable(false);
                ((ImageView) ap3.getChildren().get(0)).setImage(new LKImageManager().getImageResource("step-final.png").getImage());
                break;
            }
        }
    }

    public static void setBorderNode(Node node, boolean set) {
        if (set) {
            node.setStyle("-fx-border-color: #00D9D9; -fx-border-width: 4");
        } else {
            node.setStyle("-fx-border-color: #FFFFFF; -fx-border-width: 1");
        }
    }

    @FXML
    public void openPopup() {
        DialogGraphic dialog = new DialogGraphic(stackPane, contents.isEmpty() ? 0 : contents.get(0).getIdChannel());
        dialog.show();
    }

    private void showProgress(boolean show) {
        bpLoading.setVisible(show);
    }

    @FXML
    public void ivChartOver() {
        ivGraphic.setImage(new LKImageManager().getImageResource("/icon_graphic_pizza_over.png").getImage());
    }

    @FXML
    public void ivChartExited() {
        ivGraphic.setImage(new LKImageManager().getImageResource("/icon_graphic_pizza.png").getImage());
    }

    public Map<String, ITSocialChannelContent> getContents() {
        return orgContent;
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
