package uol.bluebook.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        if(classroom.getBannedUsers().get(student.getId()) != null) return false;
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
    @GetMapping("/get-student-info/{studentId}")
    public Object getStudentInfo(@PathVariable int studentId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        //authentication
        if(!user.getRole().equals("TEACHER")) return false;

        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        CustomUser student = userRepo.findById(studentId);
        returnMap.put("username", student.getUsername());
        returnMap.put("fullName", student.getFirstName() + " " + student.getLastName());
        returnMap.put("id", student.getId());
        return returnMap;
    }

    @PostMapping("/kick-student/{studentId}/{classId}")
    public Boolean kickStudent(@PathVariable int studentId, @PathVariable int classId, @RequestParam Boolean ban) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Classroom classroom = classroomRepo.findById(classId);

        //authentication
        if(!user.getRole().equals("TEACHER")) return false;
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
    public Boolean pardonStudent(@PathVariable int studentId, @PathVariable int classId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Classroom classroom = classroomRepo.findById(classId);

        if(!user.getRole().equals("TEACHER")) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;

        if(classroom.getBannedUsers().get(studentId) == null) return false;
        else classroom.getBannedUsers().remove(studentId);
        
        classroomRepo.save(classroom);
        return true;
    }
}