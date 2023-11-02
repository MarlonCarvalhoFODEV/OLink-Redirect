package webconnection;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import urls.UrlsConnetionsWEB;
import util.UtilConsoleColors;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Base64;

public class WebJson {

    public String getJson(String urlConnection) {
        HttpURLConnection conn = null;
        BufferedReader br = null ;
        URL url = null;
        try {
            url = new URL(urlConnection);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization",auto());
            conn.setRequestProperty("Content-Language", "pt-BR");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream()),StandardCharsets.UTF_8));

            StringBuilder receiver = new StringBuilder();
            String line ;
            while ((line = br.readLine()) != null) {
                receiver.append(line);
            }

            conn.disconnect();
            br.close();

            return receiver.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            try {
                conn.disconnect();
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            try {
                conn.disconnect();
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                conn.disconnect();
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null ;
    }

    public String postJson(String urlConnection, Object object) {

        BufferedReader br ;
        HttpURLConnection conn = null ;
        try {
            URL url = new URL(urlConnection);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", auto());

            OutputStream os = conn.getOutputStream();
            os.write(new Gson().toJson(object).getBytes(StandardCharsets.UTF_8));
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder response = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();

            br.close();
            os.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public String jsonRequest(String method, String urlConnection, Object object){
        HttpURLConnection conn = null ;
        BufferedReader br = null ;

        try {
            URL url = new URL(urlConnection);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization",auto());

            OutputStream os = conn.getOutputStream();
            os.write(new Gson().toJson(object).getBytes(StandardCharsets.UTF_8));
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder response = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            os.close();
            conn.disconnect();
            br.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();

            conn.disconnect();
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null ;
    }

    public String jsonRequest(String method, String urlConnection) {
        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            URL url = new URL(urlConnection);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization",auto());

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder receiver = new StringBuilder();
            String line ;
            while ((line = br.readLine()) != null) {
                receiver.append(line);
            }

            conn.disconnect();
            br.close();

            return receiver.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null ;
    }

    public static void downloadFile(String fileURL, String saveDir, Label status, ProgressBar progress)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + "/"+ fileName;

            File file = new File(saveFilePath);
            file.mkdirs();
            file.delete();

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[1024];
                int counterBytesRead = 0 ;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    counterBytesRead += bytesRead;
                    outputStream.write(buffer, 0, bytesRead);

                    updateProgress("OLink Redirect",contentLength,counterBytesRead,status,progress);
                }

                outputStream.close();
                inputStream.close();

            Platform.runLater(()->{
                progress.setProgress(-1);
                status.setText("Aguarde, iniciando instalador...");
            });

            try {
              Process p = new ProcessBuilder(saveFilePath).start();
              if(p.isAlive())
                  closeApplication();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("File downloaded");
        } else {
            //System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }

    private static boolean existFile(String path){
        return Files.exists(Paths.get(path));
    }

    private static synchronized void updateProgress(String fileName, int fileSize, int fileDonwloaded, Label status, ProgressBar progress){
        Platform.runLater(()->{

            double percentDownloaded = ((fileDonwloaded*100.0)/fileSize);

            StringBuilder sb = new StringBuilder();
            sb.append("Baixando... ");
            sb.append(fileName);
            sb.append(" ");
            sb.append(((int)percentDownloaded));
            sb.append("%");
            status.setText(sb.toString());

            double prog = Double.valueOf(new DecimalFormat("0.##").
                    format(percentDownloaded/100).replace(",","."));
            progress.setProgress(prog);
        });
    }

    private static final String auto(){
        String creds = String.format("%s:%s","client","M@7832@78d4de");
        return "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
    }

    public synchronized static boolean haveConnection(){
        try{
            URL mandarMail = new URL("https://google.com");
            java.net.URLConnection conn = mandarMail.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.connect();
            int x = httpConn.getResponseCode();
            if(x == 200){
                httpConn.disconnect();
                return true ;
            } else {
                httpConn.disconnect();
            }
        }
        catch(MalformedURLException urlmal){
            return false ;
        }
        catch(IOException ioexcp){
            return false ;
        }
        return false ;
    }

    public static void closeApplication(){
        System.exit(0);
    }

    public static void startApplication(){
        try {
            new ProcessBuilder("C:\\Program Files (x86)\\Renda Livre\\OLink Redirect\\OLink Redirect.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
