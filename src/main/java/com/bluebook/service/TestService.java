package com.bluebook.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import static java.util.concurrent.TimeUnit.*;

import com.bluebook.domain.Classroom;
import com.bluebook.domain.CustomUser;
import com.bluebook.domain.Test;
import com.bluebook.repositories.TestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    TestRepository testRepo;

    private static final Timer timer = new Timer();
    /**
     * There are three types of tests that are planned to be implenented the purpose
     * of the array is to ensure the user doesn't bug the server. The three types
     * are: -END_FEEDBACK (feedback is given after the user has completed the quiz)
     * -INSTANT_FEEDBACK (feedback is given as soon as the question is answered)
     * -MARKED (questions are marked by the teacher and feedback is given then)
     */
    private static final String[] validTypes = { "END_FEEDBACK", "INSTANT_FEEDBACK", "MARKED_FEEDBACK" };

    public final Boolean changeFeedbackType(final Test workingTest, final String newFbType) {
        if (workingTest.getPublished())
            return false;
        if (!Arrays.stream(validTypes).parallel().anyMatch(newFbType::contains))
            return false;

        workingTest.setFeedbackType(newFbType);
        testRepo.save(workingTest);
        return true;
    }

    /**
     * 
     * @param classroom the classroom the test belongs to
     * @param name      The name of the test
     * @param type      the type of feedback the test has
     * @param owner     the owner of the test
     * @return returns the class id if it is successfully create, otherwise it
     *         returns -1
     */
    public final int createNewTest(final Classroom classroom, final String name, final String type,
            final CustomUser owner) {

        if (!Arrays.stream(validTypes).parallel().anyMatch(type::contains))
            return -1;

        if (name.contains(";"))
            return -1;

        int id = 0;
        while (testRepo.existsById(id))
            id++;
        Test newTest = new Test(id, name, type, owner, classroom);
        testRepo.save(newTest);

        return id;
    }

    /**
     * 
     * @param test     the test to be changed
     * @param newTitle the new title
     * @return true if successful, else false
     */
    public final Boolean updateTestTitle(final Test test, final String newTitle) {
        if (newTitle.contains(";"))
            return false;
        test.setName(newTitle);
        testRepo.save(test);
        return true;
    }

    public final Boolean publishTest(final Test test) {
        test.setPublished(true);
        testRepo.save(test);
        return true;
    }

    public final Boolean hideTest(final Test test) {
        test.setPublished(false);
        testRepo.save(test);
        return true;
    }

    public final Boolean scheduleTest(final int testId, final String date, final String time) {
        Test workingTest = testRepo.findById(testId);
        if(workingTest == null) return false;
        if(workingTest.getPublished()) return false; 
        /**
         * This time task is run from the date that is taken
         * from user input and it parsed in the try catch below.
         * therefore, everything inside of the run with run at the given date.
         */
        TimerTask task = new TimerTask() {
            public void run() {
                Test test = testRepo.findById(testId);
                /**
                 * This is a short segment of code to ensure
                 * that checks that the scheduled time is still the same time (within a minute).
                 * If it isn't the code won't change the test's published status.
                 */
                Date now = new Date();
                long MAX_DURATION = MILLISECONDS.convert(1, MINUTES);
                long duration = now.getTime() - test.getScheduledFor().getTime();
                if (duration <= MAX_DURATION && duration > 0L) {
                    testRepo.updatePublished(true, testId);
                } 
            }
        };
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date scheduledFor = sdf.parse(date + " " + time);
            workingTest.setScheduledFor(scheduledFor);
            testRepo.save(workingTest);
            if(scheduledFor.before(new Date())) return false;
            timer.schedule(task, scheduledFor);
            return true;
        } catch (ParseException e) {
            return false;
        }
    } 

    public final Boolean setDueDate(final Test workingTest, final String date, final String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dueDate = sdf.parse(date + " " + time);
            workingTest.setDueDate(dueDate);
            testRepo.save(workingTest);
            if(dueDate.before(new Date())) return false;
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}