package uol.bluebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uol.bluebook.domain.Classroom;
import uol.bluebook.domain.CustomUser;
import uol.bluebook.repositories.ClassroomRepository;
import uol.bluebook.repositories.UserRepository;
import uol.bluebook.config.CustomUserDetails;
@RestController
@RequestMapping("/teacher")
public class TeacherRestController {
    
    @Autowired
    UserRepository userRepo;
    @Autowired
    ClassroomRepository classroomRepo;

    @PostMapping("/add-student/{username}/{classId}")
    public Boolean addStudent(@PathVariable String username, @PathVariable int classId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Classroom classroom = classroomRepo.findById(classId);
        CustomUser student = userRepo.findByUsername(username);
        //authentication
        if(student == null) return false;
        if(!user.getRole().equals("TEACHER")) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;
        if(student.getRole().equals("TEACHER")) return false;
        if(classroom.containsUser(username)) return false;
        //adding student to class
        classroom.getStudents().add(student);
        student.getStudentClassrooms().add(classroom);
        classroomRepo.save(classroom);
        userRepo.save(student);

        return true;
    }

    @PostMapping("/update-title/{className}/{classDesc}/{classId}")
    public Boolean updateTitle(@PathVariable String className, @PathVariable String classDesc, @PathVariable int classId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Classroom classroom = classroomRepo.findById(classId);

        //authentication
        if(!user.getRole().equals("TEACHER")) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;

        if(className.length() == 0 || classDesc.length() == 0) return false;  
        if(className.length() > 60 || classDesc.length() > 500) return false; 
        
        classroom.setName(className);
        classroom.setDescription(classDesc);

        classroomRepo.save(classroom);

        return true;
    }
}