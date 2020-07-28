package com.bluebook.service;



import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.repository.ClassroomRepository;
import com.bluebook.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    ClassroomRepository classroomRepo;
    @Autowired
    TestService testService;

    /**
     * @param studentUsername The student's username
     * @param classroom       The classroom the student will be added to
     * @return returns true if the user is added, or false if the student cannot be
     *         added.
     */
    public Boolean addStudent(String studentUsername, Classroom classroom) {
        CustomUser student = userRepo.findByUsername(studentUsername);
        // to ensure the student can be added to the classroom
        if (student == null)
            return false;
        if ("TEACHER".equals(student.getRole()))
            return false;
        if (classroom.containsUser(studentUsername))
            return false;
        if (classroom.getBannedUsers().get(student.getId()) != null)
            return false;
        // code that adds student to the class
        classroom.getStudents().add(student);
        student.getStudentClassrooms().add(classroom);
        classroomRepo.save(classroom);
        userRepo.save(student);

        return true;
    }

    /**
     * 
     * @param classroom    The classroom to be updated.
     * @param newClassName The new name
     * @param newClassDesc The new descrption
     * @return returns true if successful, else it returns false
     */
    public Boolean updateClassDetails(Classroom classroom, String newClassName, String newClassDesc) {
        if (newClassName == null || newClassDesc == null)
            return false;
        if (newClassName.length() <= 0 || newClassName.length() > 60)
            return false;
        if (newClassDesc.length() <= 0 || newClassDesc.length() > 500)
            return false;

        classroom.setName(newClassName);
        classroom.setDescription(newClassDesc);
        classroomRepo.save(classroom);

        return true;
    }

    /**
     * 
     * @param classroom The classroom you want to kick a student from
     * @param studentId The student's ID, who you want to be kicked
     * @param ban       Whether or not they should be banned or not
     * @return
     */
    public Boolean kickStudent(Classroom classroom, int studentId, Boolean ban) {

        CustomUser student = userRepo.findById(studentId);
        if (classroom == null)
            return false;
        if (!classroom.containsUser(studentId))
            return false;

        classroom.removeUser(studentId);
        student.removeClassroom(classroom.getId());
        if (ban == true)
            classroom.getBannedUsers().put(studentId, student.getUsername());

        classroomRepo.save(classroom);
        userRepo.save(student);

        return true;
    }

    /**
     * 
     * @param classroom The classroom where to pardon a student
     * @param studentId the student ID of who should be pardonned
     * @return true if successful and false if not
     */
    public Boolean pardonStudent(Classroom classroom, int studentId) {

        if (classroom.getBannedUsers().get(studentId) == null)
            return false;
        else
            classroom.getBannedUsers().remove(studentId);

        classroomRepo.save(classroom);

        return true;
    }

}