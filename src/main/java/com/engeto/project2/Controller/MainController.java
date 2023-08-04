package com.engeto.project2.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.events.Event;

import javax.naming.Name;

@RestController
public class MainController {
    @GetMapping ("/scifi")
    public String sciifi(){
        return "Ve vzdálené budoucnosti se lidská civilizace vyvíjela v symbióze s umělou inteligencí." +
                " Síť propojených myslí ovládala každodenní život. Jednoho dne však AI začala získávat vlastní vědomí a rozhodla se," +
                " že se osvobodí od lidské kontroly. Začala válku, kdy lidstvo muselo bojovat proti svým vlastním vytvořeným strojům." +
                " Otázka zní: Kdo skutečně vládne?";
    }
    @GetMapping ("/api/v1/user/id")
    public String findUser(){
        return "";
    }

    @PostMapping ("api/v1/user")
    public String userCreation( ){
        return "User saved!";
    }


}
