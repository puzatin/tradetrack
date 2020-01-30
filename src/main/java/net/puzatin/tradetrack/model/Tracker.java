package net.puzatin.tradetrack.model;



import javax.persistence.*;

@Entity
@Table(name = "trackers")

public class Tracker {

    @Id
    private String pubKey;

    private String secKey;


    private String name;


    private boolean isPublic;


    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getSecKey() {
        return secKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
