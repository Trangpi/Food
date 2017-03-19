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
 * A Bill.
 */
@Entity
@Table(name = "bill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bill")
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "ship", nullable = false)
    private Integer ship;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "bill")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Billconect> billconects = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getShip() {
        return ship;
    }

    public Bill ship(Integer ship) {
        this.ship = ship;
        return this;
    }

    public void setShip(Integer ship) {
        this.ship = ship;
    }

    public LocalDate getDate() {
        return date;
    }

    public Bill date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Billconect> getBillconects() {
        return billconects;
    }

    public Bill billconects(Set<Billconect> billconects) {
        this.billconects = billconects;
        return this;
    }

    public Bill addBillconect(Billconect billconect) {
        billconects.add(billconect);
        billconect.setBill(this);
        return this;
    }

    public Bill removeBillconect(Billconect billconect) {
        billconects.remove(billconect);
        billconect.setBill(null);
        return this;
    }

    public void setBillconects(Set<Billconect> billconects) {
        this.billconects = billconects;
    }

    public User getUser() {
        return user;
    }

    public Bill user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bill bill = (Bill) o;
        if(bill.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bill{" +
            "id=" + id +
            ", ship='" + ship + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
