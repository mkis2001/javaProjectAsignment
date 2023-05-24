package hr.java.musicshop.entitet;

import hr.java.musicshop.sucelja.LoadFile;

import java.io.Serializable;
import java.time.LocalDateTime;

import static hr.java.musicshop.controller.HelloApplication.logger;

public class Dogadaj implements LoadFile, Serializable {
    private String naziv;
    private LocalDateTime vrijeme;
    private String user;

    public Dogadaj(String naziv) {
        this.naziv = naziv;
        this.vrijeme = LocalDateTime.now();
        this.user = "none";
        this.user = getCurrentUser();
        logger.info(naziv + " (User: " + user + ")");
    }

    public String getNaziv() {
        return naziv;
    }

    public Dogadaj setNaziv(String naziv) {
        this.naziv = naziv;
        return this;
    }

    public LocalDateTime getVrijeme() {
        return vrijeme;
    }

    public Dogadaj setVrijeme(LocalDateTime vrijeme) {
        this.vrijeme = vrijeme;
        return this;
    }

    public String getUser() {
        return user;
    }

    public Dogadaj setUser(String user) {
        this.user = user;
        return this;
    }
}
