package ru.grishkova.kursovaya.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "good", length = 65535)
    @Type(type = "text")
    private String good;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public void addGood(String good){
        this.good = this.good + good;
    }
}
