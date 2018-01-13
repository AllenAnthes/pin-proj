//package edu.ucmo.fightingmongeese.teamcredentialsstore.models;
//
//
//import org.hibernate.validator.constraints.NotEmpty;
//
//import javax.persistence.*;
//
//@Entity
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long id;
//
//    @NotEmpty
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @NotEmpty
//    @Column(name = "password", nullable = false)
//    private String password;
//
//    @NotEmpty
//    @Column(name = "group", nullable = false)
//    private String group;
//
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
//}
