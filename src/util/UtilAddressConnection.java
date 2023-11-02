/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import webconnection.DialogNoConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static webconnection.WebJson.haveConnection;

/**
 *
 * @author Marlon
 */
public class UtilAddressConnection {
    
    
    public static String getMac(){
        if(haveConnection()){
            try {
                InetAddress ip;

                ip = InetAddress.getLocalHost();

                NetworkInterface network = NetworkInterface.getByInetAddress(ip);

                byte[] mac = network.getHardwareAddress();

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                return sb.toString();
            } catch (UnknownHostException | SocketException ex) {
                Logger.getLogger(UtilAddressConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Platform.runLater(()->{
                Toast.makeText(null,"Sem Conexão com a INTERNET",0,500,5000);
            });
        }

        return null ;
    }
    
    public static String getLocalIp(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(UtilAddressConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null ;
    }
    
    public static String getPublicIp(){
        if(haveConnection()){
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                return in.readLine();
            } catch (MalformedURLException ex) {
                Logger.getLogger(UtilAddressConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UtilAddressConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null ;
        } else {
            Platform.runLater(()->{
                Toast.makeText(null,"Sem Conexão com a INTERNET",0,500,5000);
            });
        }
        return null ;
    }
}
