package com.bluebook.restcontrollers;

import java.util.HashMap;

import com.bluebook.config.CustomUserDetails;
import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.repositories.ClassroomRepository;
import com.bluebook.repositories.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/teacher")
public class TeacherRestController {
    
    @Autowired
    UserRepository userRepo;
    @Autowired
    ClassroomRepository classroomRepo;


    @PostMapping("/add-student/{username}/{classId}")
    public final Boolean addStudent(@PathVariable final String username, @PathVariable final int classId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);
        final CustomUser student = userRepo.findByUsername(username);
        //authentication
        if(student == null) return false;
        if("STUDENT".equals(user.getRole())) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;
        if("TEACHER".equals(student.getRole())) return false;
        if(classroom.containsUser(username)) return false;
        if(classroom.getBannedUsers().get(student.getId()) != null) return false;
        //adding student to class
        classroom.getStudents().add(student);
        student.getStudentClassrooms().add(classroom);
        classroomRepo.save(classroom);
        userRepo.save(student);

        return true;
    }

    @PostMapping("/update-title/{className}/{classDesc}/{classId}")
    public final Boolean updateTitle(@PathVariable final String className, 
        @PathVariable final String classDesc, @PathVariable final int classId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);

        //authentication
        if("STUDENT".equals(user.getRole())) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;

        if(className.length() == 0 || classDesc.length() == 0) return false;  
        if(className.length() > 60 || classDesc.length() > 500) return false; 
        
        classroom.setName(className);
        classroom.setDescription(classDesc);

        classroomRepo.save(classroom);

        return true;
    }
    @GetMapping("/get-student-info/{studentId}")
    public final Object getStudentInfo(@PathVariable final int studentId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        //authentication
        if("STUDENT".equals(user.getRole())) return false;

        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        final CustomUser student = userRepo.findById(studentId);
        returnMap.put("username", student.getUsername());
        returnMap.put("fullName", student.getFirstName() + " " + student.getLastName());
        returnMap.put("id", student.getId());
        return returnMap;
    }

    @PostMapping("/kick-student/{studentId}/{classId}")
    public final Boolean kickStudent(@PathVariable final int studentId, @PathVariable final int classId, 
        @RequestParam final Boolean ban) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);

        //authentication
        if("STUDENT".equals(user.getRole())) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;
        if(!classroom.containsUser(studentId)) return false;

        CustomUser student = userRepo.findById(studentId);
            
        classroom.removeUser(studentId);
        student.removeClassroom(classId);
        if(ban == true) 
            classroom.getBannedUsers().put(studentId, student.getUsername());
    
        classroomRepo.save(classroom);
        userRepo.save(student);

        return true;
    }

    @PostMapping("/pardon-student/{studentId}/{classId}")
    public final Boolean pardonStudent(@PathVariable final int studentId, @PathVariable final int classId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);

        if("STUDENT".equals(user.getRole())) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;

        if(classroom.getBannedUsers().get(studentId) == null) return false;
        else classroom.getBannedUsers().remove(studentId);
        
        classroomRepo.save(classroom);
        return true;
    }
}