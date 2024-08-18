package com.example.rac.notelist.adress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class IpAdress {
    public String myIp (){
        try {
            // Получаем внешний IP-адрес с помощью внешнего веб-сервиса
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            return br.readLine().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
