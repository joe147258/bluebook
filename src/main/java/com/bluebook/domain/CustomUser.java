package com.bluebook.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

@Entity
public class CustomUser {

    @Column(name = "user_id")
    @Id
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String role;
        
    @OneToMany(mappedBy="classOwner", cascade = CascadeType.ALL)
    private List<Classroom> ownedClassrooms = new ArrayList<Classroom>();

    @OneToMany(mappedBy="testOwner", cascade = CascadeType.ALL)
    private List<Test> ownedTests = new ArrayList<Test>();
    
    @ManyToMany
    @JoinTable(
        name = "student_in_classes",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="classroom_id")
    )
    private List<Classroom> studentClassrooms = new ArrayList<Classroom>();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Classroom> getOwnedClassrooms() {
        return ownedClassrooms;
    }

    public void setOwnedClassrooms(List<Classroom> ownedClassrooms) {
        this.ownedClassrooms = ownedClassrooms;
    }

    public List<Classroom> getStudentClassrooms() {
        return studentClassrooms;
    }

    public void setStudentClassrooms(List<Classroom> studentClassrooms) {
        this.studentClassrooms = studentClassrooms;
    }

    public void removeClassroom(int classId) {
        for(int i = 0; i < studentClassrooms.size(); ++i) {
            if(studentClassrooms.get(i).getId() == classId) {
                studentClassrooms.remove(i);
                break;
            }
        }
    } 
    
}