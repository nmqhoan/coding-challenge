package com.genesys.challenge.client.configuration;

import com.genesys.challenge.client.socket.SocketClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfiguration {
    public static String baseUrl;
    public static Properties prop;

    static{
        try (InputStream input = SocketClient.class.getClassLoader().getResourceAsStream("application.properties")) {

            prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");

            }
            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            baseUrl = "http://" + prop.getProperty("server.ip") + ":" + prop.getProperty("server.port") + "/api/";
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
