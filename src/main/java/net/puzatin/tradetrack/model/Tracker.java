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
    @Size(max = 64)
    @Column(name = "pubKey")
    private String pubKey;

    @Column(name = "secKey")
    @Size(max = 64)
    private String secKey;

    @Pattern(message = "characters in name allowed _-@.", regexp = "^[\\w-@.]+$")
    @NotBlank(message = "name must not be empty!")
    @Size(min = 2, max = 20, message = "name must be between 2 and 20 characters!")
    @Column(name = "name")
    private String name;

    @Size(max = 140, message = "description cannot be more than 140 characters!")
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL)
    private List<Snapshot> snapshots;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "is_valid")
    private boolean isValid;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
