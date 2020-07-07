package com.bluebook.restcontrollers;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.bluebook.config.CustomUserDetails;
import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.domain.Test;
import com.bluebook.repositories.ClassroomRepository;
import com.bluebook.repositories.TestRepository;
import com.bluebook.repositories.UserRepository;
import com.bluebook.service.TeacherService;
import com.bluebook.service.TestService;

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
    TestRepository testRepo;
    @Autowired
    ClassroomRepository classroomRepo;
    @Autowired
    TeacherService teacherService;
    @Autowired
    TestService testService;


    @PostMapping("/add-student/{username}/{classId}")
    public final Boolean addStudent(@PathVariable final String username, @PathVariable final int classId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);
        //authentication
        if("STUDENT".equals(user.getRole())) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;
        
        return teacherService.addStudent(username, classroom);
    }

    @PostMapping("/update-title/{className}/{classDesc}/{classId}")
    public final Boolean updateTitle(@PathVariable final String className, 
        @PathVariable final String classDesc, @PathVariable final int classId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);
        //authentication
        if("STUDENT".equals(user.getRole())) return false;
        if(classroom == null) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;


        return teacherService.updateClassDetails(classroom, className, classDesc);
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

    @GetMapping("/get-test-info/{testId}")
    public final Object getTestInfo(@PathVariable final int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        //authentication
        final Test workingTest = testRepo.findById(testId);
        if("STUDENT".equals(user.getRole())) return false;
        if(workingTest.getTestOwner().getId() != user.getId()) return false;

        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy - h:mm a");
        
        returnMap.put("id", workingTest.getId());
        returnMap.put("name", workingTest.getName());
        returnMap.put("published", workingTest.getPublished().toString());
        if(workingTest.getScheduledFor() != null){
            returnMap.put("scheduled", sdf.format(workingTest.getScheduledFor()));
        } else {
            returnMap.put("scheduled", "N/A");
        }
        if(workingTest.getDueDate() != null){
            returnMap.put("duedate", sdf.format(workingTest.getDueDate()));
        } else {
            returnMap.put("duedate", "N/A");
        }
        returnMap.put("completedUsers", 0); //TODO: implement
        returnMap.put("fbType", workingTest.getFeedbackType());
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


        return teacherService.kickStudent(classroom, studentId, ban);
    }

    @PostMapping("/pardon-student/{studentId}/{classId}")
    public final Boolean pardonStudent(@PathVariable final int studentId, @PathVariable final int classId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Classroom classroom = classroomRepo.findById(classId);
        if(classroom == null) return false;
        if("STUDENT".equals(user.getRole())) return false;
        if(classroom.getClassOwner().getId() != user.getId()) return false;

        return teacherService.pardonStudent(classroom, studentId);
    }

    @PostMapping("/reschedule-test/{testId}")
    public final Boolean rescheduleTest(@PathVariable final int testId, @RequestParam String date, 
        @RequestParam String time) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test test = testRepo.findById(testId);
        if(test == null) return false;
        if("STUDENT".equals(user.getRole())) return false;
        if(test.getTestOwner().getId() != user.getId()) return false;

        return testService.scheduleTest(testId, date, time);
    }

    @PostMapping("/hide-test/{testId}")
    public final Boolean hideTest(@PathVariable final int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test test = testRepo.findById(testId);
        if(test == null) return false;
        if("STUDENT".equals(user.getRole())) return false;
        if(test.getTestOwner().getId() != user.getId()) return false;

        return testService.hideTest(test);
    }

    @PostMapping("/show-test/{testId}")
    public final Boolean showTest(@PathVariable final int testId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        final Test test = testRepo.findById(testId);
        if(test == null) return false;
        if("STUDENT".equals(user.getRole())) return false;
        if(test.getTestOwner().getId() != user.getId()) return false;

        return testService.publishTest(test);
    }
}