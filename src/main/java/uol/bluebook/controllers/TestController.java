package uol.bluebook.controllers;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uol.bluebook.domain.Classroom;
import uol.bluebook.domain.CustomUser;
import uol.bluebook.repositories.ClassroomRepository;
import uol.bluebook.repositories.UserRepository;
import uol.bluebook.config.CustomUserDetails;

@Controller
@RequestMapping("/tests/")
public class TestController {
    /**
     * There are three types of tests that are planned to be implenented
     * the purpose of the array is to ensure the user doesn't bug the server.
     * The three types are:
     *  -END_FEEDBACK (feedback is given after the user has completed the quiz)
     *  -INSTANT_FEEDBACK (feedback is given as soon as the question is answered)
     *  -MARKED (questions are marked by the teacher and feedback is given then)
     *  TODO: as of 24/06/20 only END_FEEDBACK is valid
     */
    static final String[] types = {"END_FEEDBACK"};
    @Autowired
    ClassroomRepository classroomRepo;
    @Autowired
    UserRepository userRepo;

    @PostMapping(value="/new")
    public String newTest(@RequestParam Map<String,String> params) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUser user = userRepo.findById(((CustomUserDetails)principal).getId());
        Classroom classroom = classroomRepo.findById(Integer.parseInt(params.get("classId")));
        if(classroom == null) return "redirect:/permission denied"; //TODO: make a server problem page
        if(!user.getRole().equals("TEACHER")) return "redirect:/permission-denied";
        if(user.getId() != classroom.getClassOwner().getId()) return "redirect:/permission-denied";
        //this checks type of quizzes
        if(!Arrays.stream(types).parallel().anyMatch(params.get("type")::contains)) return "redirect:/permission-denied"; //TODO: make a server problem page
        String name = params.get("name");
        return "redirect:/index"; //TODO: continue quiz creation. this redirect is temp
    }
    
}