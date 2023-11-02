package frames.configuration;

import Itens.ITPixelBackup;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import system.OLinkConfiguration;
import urls.UrlsConnetionsWEB;
import util.Toast;
import webconnection.DialogNoConnection;
import webconnection.WebJson;

import java.net.URL;
import java.util.ResourceBundle;

import static system.OLinkConfiguration.getThreadGroup;
import static system.OLinkConfiguration.getAccount;
import static webconnection.WebJson.haveConnection;

public class ConfigurationController implements Initializable {

    @FXML
    private TextField etIdAccountADS, etAccessToken, etAccessToken2, etPixelName, etPixelExisting ;

    @FXML
    private StackPane stackPane ;

    @FXML
    private BorderPane bpProgress ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void saveAccountBackupOrPixelBackup(){
        showProgress(true);
        if(haveConnection()){
            Thread th = new Thread(getThreadGroup(),()->{
                StringBuilder sb = new StringBuilder();
                sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                sb.append(UrlsConnetionsWEB.FacebookControll.FACEBOOK);

                if(etAccessToken.getText() != null && !etAccessToken.getText().isEmpty()
                        && etIdAccountADS.getText() != null  && !etIdAccountADS.getText().isEmpty()
                        && etPixelName.getText() != null && !etPixelName.getText().isEmpty()){
                    sb.append(UrlsConnetionsWEB.FacebookControll.Method.SAVE_ACCOUNT_BACKUP);

                    etAccessToken2.setText(null);
                    etPixelExisting.setText(null);
                } else if(etPixelExisting.getText() != null){
                    sb.append(UrlsConnetionsWEB.FacebookControll.Method.SAVE_PIXEL_EXISTING);
                    clearFields();
                }

                String t1 = etAccessToken.getText();
                String t2 = etAccessToken2.getText();

                ITPixelBackup item = new ITPixelBackup();
                item.setIdUser(getAccount().getId());
                item.setAccessToken(!t1.isEmpty()?t1:t2);
                item.setIdAccountADS(etIdAccountADS.getText());
                item.setPixelName(etPixelName.getText());
                item.setIdPixel(etPixelExisting.getText());

                String json = new WebJson().postJson(sb.toString(), item);

                int result = new Gson().fromJson(json, Integer.class);

                Platform.runLater(()->{
                    showProgress(false);


                    Stage stage = (Stage) etAccessToken.getScene().getWindow();
                    String message = null ;
                    switch (result){
                        case -1: message = "Erro Inesperado!" ;{
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 345: message = "Pixel de Backup Criado com Sucesso!" ;{
                            Toast.makeText(stage,message,0,500,5000);
                            Platform.exit();
                            break;
                        }
                        case 2: message = "Já existe um pixel para esta conta!" ;{
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 3: message = "Erro de permissões!\nVocê Precisa Liberar Permissão No Facebook" ;{
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 4: message = "Parâmetro inválido" ;{
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 5: {
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 6: {
                            message = "O gerenciamento de anúncios requer um token de acesso com a permissão estendida para ads_management" ;
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 7: {
                            message = "Este login compartilhado está desativado. Acesse o Gerenciador de anúncios para obter mais informações." ;
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 8: {
                            message = "Parâmetro inválido!" ;
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 9: {
                            message = "Houve muitas chamadas para esta conta de anúncio. Espere um pouco e tente novamente. Para mais informações, entre em contato com o suporte: suporte-olink@rendalivre.com" ;
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 10:{
                            message = "A ação tentada foi considerada abusiva ou não é permitida" ;
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 11: {
                            message = "Token de acesso inválido!" ;
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                        case 12: {
                            message = "Você já possui um pixel de backup registrado" ;
                            Toast.makeText(stage,message,0,500,5000,true);
                            clearFields();
                            break;
                        }
                    }
                });
            });
            th.start();
        } else {
            showProgress(false);
            new DialogNoConnection(stackPane).show(true);
        }
    }

    private void enableFields(boolean ativar){
        etPixelName.setDisable(!ativar);
        etIdAccountADS.setDisable(!ativar);
        etAccessToken.setDisable(!ativar);
    }

    private void clearFields(){
        etPixelName.setText(null);
        etIdAccountADS.setText(null);
        etAccessToken.setText(null);
    }

    @FXML
    public void rateYes(){
        showProgress(true);
        if(haveConnection()){
            Thread th = new Thread(getThreadGroup(),()->{
                StringBuilder sb = new StringBuilder();
                sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                sb.append(UrlsConnetionsWEB.FacebookControll.FACEBOOK);
                sb.append(UrlsConnetionsWEB.FacebookControll.Method.HATING_PROJECT);
                sb.append(true);

                String json = new WebJson().getJson(sb.toString());

                int result = new Gson().fromJson(json, Integer.class);

                Platform.runLater(()->{
                    showProgress(false);
                });
            });
            th.start();
        } else {
            showProgress(false);
            new DialogNoConnection(stackPane).show(true);
        }
    }

    @FXML
    public void rateNot(){

        showProgress(true);
        if(haveConnection()){
            Thread th = new Thread(getThreadGroup(),()->{
                StringBuilder sb = new StringBuilder();
                sb.append(UrlsConnetionsWEB.DOMAIN_CONNECTION);
                sb.append(UrlsConnetionsWEB.FacebookControll.FACEBOOK);
                sb.append(UrlsConnetionsWEB.FacebookControll.Method.HATING_PROJECT);
                sb.append(false);

                String json = new WebJson().getJson(sb.toString());

                int result = new Gson().fromJson(json, Integer.class);

                System.out.println("RESULT RATING: "+result);

                Platform.runLater(()->{
                    showProgress(false);
                });
            });
            th.start();
        } else {
            showProgress(false);
            new DialogNoConnection(stackPane).show(true);
        }
    }

    public void showProgress(boolean show){
        bpProgress.setVisible(show);
    }
}
