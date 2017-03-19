package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Billconect.
 */
@Entity
@Table(name = "billconect")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "billconect")
public class Billconect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "soluong", nullable = false)
    private Integer soluong;

    @ManyToOne
    private MonAn monAn;

    @ManyToOne
    private Bill bill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSoluong() {
        return soluong;
    }

    public Billconect soluong(Integer soluong) {
        this.soluong = soluong;
        return this;
    }

    public void setSoluong(Integer soluong) {
        this.soluong = soluong;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public Billconect monAn(MonAn monAn) {
        this.monAn = monAn;
        return this;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public Bill getBill() {
        return bill;
    }

    public Billconect bill(Bill bill) {
        this.bill = bill;
        return this;
    }

    public double get_thanhtoan(){
        double result ;
        result = getMonAn().getGia() * getSoluong() ;
        return  result;
    }

    public double get_thanhtoansale(){
        double result ;
        result = get_thanhtoan() * (getMonAn().getSale() / 100.0);
        return  result;
    }

    public double get_thanhtoanid(){
        double result ;
        result = get_thanhtoan() - get_thanhtoansale();
        return result;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Billconect billconect = (Billconect) o;
        if(billconect.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, billconect.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Billconect{" +
            "id=" + id +
            ", soluong='" + soluong + "'" +
            '}';
    }
}
