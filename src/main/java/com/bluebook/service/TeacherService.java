package com.bluebook.service;

import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.repositories.ClassroomRepository;
import com.bluebook.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    
    @Autowired
    UserRepository userRepo;
    @Autowired
    ClassroomRepository classroomRepo;

    /**
     * @param studentUsername The student's username
     * @param classroom The classroom the student will be added to
     * @return returns true if the user is added, or false if the student cannot be added.
     */
    public final Boolean addStudent(final String studentUsername, final Classroom classroom) {
        final CustomUser student = userRepo.findByUsername(studentUsername);
        //to ensure the student can be added to the classroom
        if(student == null) return false;
        if("TEACHER".equals(student.getRole())) return false;
        if(classroom.containsUser(studentUsername)) return false;
        if(classroom.getBannedUsers().get(student.getId()) != null) return false;
        //code that adds student to the class
        classroom.getStudents().add(student);
        student.getStudentClassrooms().add(classroom);
        classroomRepo.save(classroom);
        userRepo.save(student);

        return true;
    }
    /**
     * 
     * @param classroom The classroom to be updated.
     * @param newClassName The new name
     * @param newClassDesc The new descrption
     * @return returns true if successful, else it returns false
     */
    public final Boolean updateClassDetails(final Classroom classroom, final String newClassName, 
        final String newClassDesc) {
        
        if(newClassName == null || newClassDesc == null) return false;
        if(newClassName.length() <= 0 || newClassName.length() > 60) return false;  
        if(newClassDesc.length() <= 0 || newClassDesc.length() > 500) return false; 
        
        classroom.setName(newClassName);
        classroom.setDescription(newClassDesc);
        classroomRepo.save(classroom);

        return true;
    }
    /**
     * 
     * @param classroom The classroom you want to kick a student from
     * @param studentId The student's ID, who you want to be kicked
     * @param ban Whether or not they should be banned or not
     * @return
     */
    public final Boolean kickStudent(final Classroom classroom, final int studentId, final Boolean ban) {

        CustomUser student = userRepo.findById(studentId);
        if(classroom == null) return false;
        if(!classroom.containsUser(studentId)) return false;
        
        classroom.removeUser(studentId);
        student.removeClassroom(classroom.getId());
        if(ban == true) 
            classroom.getBannedUsers().put(studentId, student.getUsername());
    
        classroomRepo.save(classroom);
        userRepo.save(student);

        return true;
    }
    public final Boolean PardonStudent(final Classroom classroom, final int studentId) {

        if(classroom.getBannedUsers().get(studentId) == null) return false;
        else classroom.getBannedUsers().remove(studentId);
        
        classroomRepo.save(classroom);

        return true;
    }
}