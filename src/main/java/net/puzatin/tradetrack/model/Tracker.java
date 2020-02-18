package net.puzatin.tradetrack.model;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "trackers")
public class Tracker {

    @Id
    private String pubKey;


    private String secKey;

    @Pattern(message = "characters allowed _-@#.", regexp = "^[\\w-@#.]+$")
    @NotBlank(message = "name must not be empty!")
    @Size(min = 2, max = 20, message = "name must be between 2 and 20 characters!")
    private String name;

    @OneToMany(mappedBy = "tracker")
    private List<Snapshot> snapshots;

    private boolean isPublic;

    private boolean isValid;


    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<Snapshot> snapshots) {
        this.snapshots = snapshots;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isValid() {
        return isValid;
    }

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
