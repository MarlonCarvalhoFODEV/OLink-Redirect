package testingfield;


import java.io.IOException;

public class FiedTest {

    public static void main(String[] args) {
        try {
            Process process = new ProcessBuilder("C:/Users/Marlon Carvalho/Desktop/SETUP-OLink Redirect.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
