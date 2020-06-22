package uol.bluebook.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Classroom {
    @Column(name = "class_id")
    @Id
    private int id;
    private String name;
    private String joinCode;
    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="classOwner")
    CustomUser classOwner = new CustomUser();

    @ManyToMany(mappedBy = "studentClassrooms", fetch = FetchType.EAGER)
    List<CustomUser> students = new ArrayList<CustomUser>();

    public Classroom() {

    }
    /**
     * This constructor is the standard way to create a new classroom.
     * The above constructor is for JPA.
    */
    public Classroom(int id, CustomUser classOwner, String name, String description) {
        this.id = id;
        this.classOwner = classOwner;
        this.name = name;
        this.description = description;
        this.joinCode = id + name.substring(0, 3) + classOwner.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomUser getClassOwner() {
        return classOwner;
    }

    public void setClassOwner(CustomUser classOwner) {
        this.classOwner = classOwner;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public List<CustomUser> getStudents() {
        return students;
    }

    public void setStudents(List<CustomUser> students) {
        this.students = students;
    }

    public Boolean containsUser(int userId) {
        for(CustomUser u : students) {
            if (u.getId() == userId) return true;
        }
        return false;
    }
    public Boolean containsUser(String username) {
        for(CustomUser u : students) {
            if (u.getUsername().equalsIgnoreCase(username)) return true;
        }
        return false;
    }

    
}