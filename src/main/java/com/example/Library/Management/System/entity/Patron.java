package com.example.Library.Management.System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//● Patron: Contains details like ID, name, contact information, etc.
@Entity
@Table(name = "patron")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "patron", fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "patron", fetch = FetchType.LAZY)
    private List<BorrowingRecord> borrowingRecords;

    public Patron(long id, String name, String phoneNumber, String address, String email, User user) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.user = user;
    }

    public Patron(String name, String phoneNumber, String address, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }

    public Patron(String name, String phoneNumber, String address, String email, User user) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.user = user;
    }

    public Patron(long id, String name, String phoneNumber, String address, String email) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }
    public void addBorrowingRecord(BorrowingRecord borrowingRecord) {
        if(borrowingRecords == null){
            borrowingRecords = new ArrayList<>();
        }
        borrowingRecords.add(borrowingRecord);
    }
}
