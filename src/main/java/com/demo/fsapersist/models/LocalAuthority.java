package com.demo.fsapersist.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "local_authority")
public class LocalAuthority {

    @Id
    @Column(name = "code")
    private Long code;

    private String name;

    private String website;

    private String email;

    @OneToMany(mappedBy = "localAuthority"
            , cascade = {CascadeType.DETACH
            , CascadeType.MERGE
            , CascadeType.PERSIST
            , CascadeType.REFRESH})
    private List<EstablishmentDetail> establishmentDetails;


    public void addEstablishmentDetail(EstablishmentDetail establishmentDetail) {

        if (establishmentDetails == null)
            establishmentDetails = new ArrayList<>();

        establishmentDetails.add(establishmentDetail);
        establishmentDetail.setLocalAuthority(this);
    }

    public LocalAuthority() {
    }

    public LocalAuthority(Long code, String name, String website, String email) {
        this.code = code;
        this.name = name;
        this.website = website;
        this.email = email;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String webSite) {
        this.website = webSite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<EstablishmentDetail> getEstablishmentDetails() {
        return establishmentDetails;
    }

    public void setEstablishmentDetails(List<EstablishmentDetail> establishmentDetails) {
        this.establishmentDetails = establishmentDetails;
    }

    @Override
    public String toString() {
        return "LocalAuthority{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", webSite='" + website + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
