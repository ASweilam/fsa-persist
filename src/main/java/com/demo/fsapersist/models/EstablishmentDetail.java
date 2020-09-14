package com.demo.fsapersist.models;

import javax.persistence.*;

@Entity(name = "establishment_detail")
public class EstablishmentDetail {

    @Id
    private Long fhrs_id;

    private String local_authority_business_id;

    private String business_name;

    private String business_type;

    private Long business_type_id;

    private String address_line_1;

    private String address_line_2;

    private String address_line_3;

    private String address_line_4;

    private String postcode;

    private String rating_value;

    private String rating_key;

    private String rating_date;

    private String scores;

    private String scheme_type;

    private Boolean new_rating_pending;

    private Double longitude;

    private Double latitude;

    @ManyToOne(cascade = {CascadeType.DETACH
            , CascadeType.MERGE
            , CascadeType.PERSIST
            , CascadeType.REFRESH})
    @JoinColumn(name = "local_authority_code")
    private LocalAuthority localAuthority;

    public EstablishmentDetail() {
    }

    public EstablishmentDetail(Long fhrs_id, String local_authority_business_id, String business_name, String business_type, Long business_type_id,
                               String address_line_1, String address_line_2, String address_line_3, String address_line_4, String postcode,
                               String rating_value, String rating_key, String rating_date, String scores, String scheme_type, Boolean new_rating_pending,
                               Double longitude, Double latitude) {
        this.fhrs_id = fhrs_id;
        this.local_authority_business_id = local_authority_business_id;
        this.business_name = business_name;
        this.business_type = business_type;
        this.business_type_id = business_type_id;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.address_line_3 = address_line_3;
        this.address_line_4 = address_line_4;
        this.postcode = postcode;
        this.rating_value = rating_value;
        this.rating_key = rating_key;
        this.rating_date = rating_date;
        this.scores = scores;
        this.scheme_type = scheme_type;
        this.new_rating_pending = new_rating_pending;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Long getFhrs_id() {
        return fhrs_id;
    }

    public void setFhrs_id(Long fhrsId) {
        this.fhrs_id = fhrsId;
    }

    public String getLocal_authority_business_id() {
        return local_authority_business_id;
    }

    public void setLocal_authority_business_id(String local_authority_business_id) {
        this.local_authority_business_id = local_authority_business_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public Long getBusiness_type_id() {
        return business_type_id;
    }

    public void setBusiness_type_id(Long business_type_id) {
        this.business_type_id = business_type_id;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getAddress_line_3() {
        return address_line_3;
    }

    public void setAddress_line_3(String address_line_3) {
        this.address_line_3 = address_line_3;
    }

    public String getAddress_line_4() {
        return address_line_4;
    }

    public void setAddress_line_4(String address_line_4) {
        this.address_line_4 = address_line_4;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postCode) {
        this.postcode = postCode;
    }

    public String getRating_value() {
        return rating_value;
    }

    public void setRating_value(String rating_value) {
        this.rating_value = rating_value;
    }

    public String getRating_key() {
        return rating_key;
    }

    public void setRating_key(String rating_key) {
        this.rating_key = rating_key;
    }

    public String getRating_date() {
        return rating_date;
    }

    public void setRating_date(String rating_date) {
        this.rating_date = rating_date;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getScheme_type() {
        return scheme_type;
    }

    public void setScheme_type(String scheme_type) {
        this.scheme_type = scheme_type;
    }

    public Boolean getNew_rating_pending() {
        return new_rating_pending;
    }

    public void setNew_rating_pending(Boolean new_rating_pending) {
        this.new_rating_pending = new_rating_pending;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public LocalAuthority getLocalAuthority() {
        return localAuthority;
    }

    public void setLocalAuthority(LocalAuthority localAuthority) {
        this.localAuthority = localAuthority;
    }

    @Override
    public String toString() {
        return "EstablishmentDetail{" +
                "fhrsId=" + fhrs_id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", localAuthority=" + localAuthority +
                '}';
    }
}
