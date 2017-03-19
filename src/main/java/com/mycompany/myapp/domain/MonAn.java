package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MonAn.
 */
@Entity
@Table(name = "mon_an")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "monan")
public class MonAn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "ten", nullable = false)
    private String ten;

    @NotNull
    @Column(name = "gia", nullable = false)
    private Integer gia;

    @NotNull
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @NotNull
    @Column(name = "theloai", nullable = false)
    private String theloai;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "sale", nullable = false)
    private Integer sale;

    @Column(name = "date_create")
    private LocalDate date_create;

    @OneToMany(mappedBy = "monAn")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Billconect> billconects = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public MonAn ten(String ten) {
        this.ten = ten;
        return this;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Integer getGia() {
        return gia;
    }

    public MonAn gia(Integer gia) {
        this.gia = gia;
        return this;
    }

    public void setGia(Integer gia) {
        this.gia = gia;
    }

    public byte[] getImage() {
        return image;
    }

    public MonAn image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public MonAn imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getTheloai() {
        return theloai;
    }

    public MonAn theloai(String theloai) {
        this.theloai = theloai;
        return this;
    }

    public void setTheloai(String theloai) {
        this.theloai = theloai;
    }

    public Integer getSale() {
        return sale;
    }

    public MonAn sale(Integer sale) {
        this.sale = sale;
        return this;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public LocalDate getDate_create() {
        return date_create;
    }

    public MonAn date_create(LocalDate date_create) {
        this.date_create = date_create;
        return this;
    }

    public void setDate_create(LocalDate date_create) {
        this.date_create = date_create;
    }

    public Set<Billconect> getBillconects() {
        return billconects;
    }

    public MonAn billconects(Set<Billconect> billconects) {
        this.billconects = billconects;
        return this;
    }

    public MonAn addBillconect(Billconect billconect) {
        billconects.add(billconect);
        billconect.setMonAn(this);
        return this;
    }

    public MonAn removeBillconect(Billconect billconect) {
        billconects.remove(billconect);
        billconect.setMonAn(null);
        return this;
    }

    public void setBillconects(Set<Billconect> billconects) {
        this.billconects = billconects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MonAn monAn = (MonAn) o;
        if(monAn.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, monAn.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MonAn{" +
            "id=" + id +
            ", ten='" + ten + "'" +
            ", gia='" + gia + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", theloai='" + theloai + "'" +
            ", sale='" + sale + "'" +
            ", date_create='" + date_create + "'" +
            '}';
    }
}
