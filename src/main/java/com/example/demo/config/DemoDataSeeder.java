package com.example.demo.config;

import com.example.demo.model.Faculty;
import com.example.demo.model.Role;
import com.example.demo.model.Subject;
import com.example.demo.model.User;
import com.example.demo.repository.FacultyRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DemoDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;

    public DemoDataSeeder(UserRepository userRepository,
                          SubjectRepository subjectRepository,
                          FacultyRepository facultyRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public void run(String... args) {
        seedAdminUser();

        Map<String, String> departments = new LinkedHashMap<>();
        departments.put("cse", "CSE");
        departments.put("ece", "ECE");
        departments.put("eee", "EEE");
        departments.put("mech", "MECHANICAL");
        departments.put("civil", "CIVIL");

        Map<Integer, List<String>> yearSubjectNames = new LinkedHashMap<>();
        yearSubjectNames.put(1, List.of("Mathematics I", "Physics", "Programming Fundamentals", "Engineering Graphics"));
        yearSubjectNames.put(2, List.of("Data Structures", "Object Oriented Programming", "Database Systems", "Signals and Systems"));
        yearSubjectNames.put(3, List.of("Operating Systems", "Computer Networks", "Software Engineering", "Web Technologies"));
        yearSubjectNames.put(4, List.of("Cloud Computing", "Artificial Intelligence", "Major Project", "Professional Ethics"));

        Map<String, List<Subject>> subjectsByDepartment = seedSubjects(departments, yearSubjectNames);
        seedUsersAndFaculty(departments, subjectsByDepartment);
        enforceSingleHodPerDepartment(departments);
    }

    private void seedAdminUser() {
        String adminUsername = "admin123";
        if (userRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword("admin@123");
        admin.setRole(Role.ADMIN);
        admin.setDepartmentIds(new ArrayList<>());
        admin.setSubjectIds(new ArrayList<>());
        admin.setFullName("System Administrator");
        admin.setEmail("admin@college.edu");
        admin.setCreatedByUserId("system");
        admin.setCreatedByRole("system");
        userRepository.save(admin);
    }

    private Map<String, List<Subject>> seedSubjects(Map<String, String> departments,
                                                    Map<Integer, List<String>> yearSubjectNames) {
        Map<String, List<Subject>> subjectsByDepartment = new HashMap<>();

        for (Map.Entry<String, String> department : departments.entrySet()) {
            String departmentId = department.getKey();
            String branchName = department.getValue();
            List<Subject> createdSubjects = new ArrayList<>();

            for (Map.Entry<Integer, List<String>> yearEntry : yearSubjectNames.entrySet()) {
                Integer year = yearEntry.getKey();
                List<String> names = yearEntry.getValue();

                for (int index = 0; index < names.size(); index++) {
                    int serial = index + 1;
                    String subjectId = String.format("sub-%s-y%d-%d", departmentId, year, serial);

                    Subject subject = subjectRepository.findById(subjectId).orElseGet(Subject::new);
                    subject.setId(subjectId);
                    subject.setName(names.get(index));
                    subject.setCode(String.format("%s-Y%d-S%d", departmentId.toUpperCase(), year, serial));
                    subject.setDepartmentId(departmentId);
                    subject.setBranch(branchName);
                    subject.setYear(year);

                    Subject saved = subjectRepository.save(subject);
                    createdSubjects.add(saved);
                }
            }

            subjectsByDepartment.put(departmentId, createdSubjects);
        }

        return subjectsByDepartment;
    }

    private void seedUsersAndFaculty(Map<String, String> departments,
                                     Map<String, List<Subject>> subjectsByDepartment) {
        for (Map.Entry<String, String> department : departments.entrySet()) {
            String departmentId = department.getKey();
            String branchName = department.getValue();

            String hodUsername = "hod_" + departmentId;
            if (userRepository.findByUsername(hodUsername).isEmpty()) {
                User hod = new User();
                hod.setUsername(hodUsername);
                hod.setPassword("hod@123");
                hod.setRole(Role.HOD);
                hod.setDepartmentId(departmentId);
                hod.setDepartmentIds(List.of(departmentId));
                hod.setSubjectIds(new ArrayList<>());
                hod.setFullName(branchName + " HOD");
                hod.setEmployeeId("HOD-" + departmentId.toUpperCase());
                hod.setEmail("hod." + departmentId + "@college.edu");
                hod.setCreatedByUserId("system");
                hod.setCreatedByRole("admin");
                userRepository.save(hod);
            }

            List<Subject> departmentSubjects = subjectsByDepartment.getOrDefault(departmentId, List.of());
            if (departmentSubjects.isEmpty()) {
                continue;
            }

            for (int facultyIndex = 1; facultyIndex <= 3; facultyIndex++) {
                String facultyUsername = String.format("fac_%s_%d", departmentId, facultyIndex);
                List<String> assignedSubjectIds = pickAssignedSubjects(departmentSubjects, facultyIndex);

                if (userRepository.findByUsername(facultyUsername).isEmpty()) {
                    User facultyUser = new User();
                    facultyUser.setUsername(facultyUsername);
                    facultyUser.setPassword("fac@123");
                    facultyUser.setRole(Role.FACULTY);
                    facultyUser.setDepartmentId(departmentId);
                    facultyUser.setDepartmentIds(List.of(departmentId));
                    facultyUser.setSubjectIds(assignedSubjectIds);
                    facultyUser.setFullName(branchName + " Faculty " + facultyIndex);
                    facultyUser.setEmployeeId(String.format("FAC-%s-%02d", departmentId.toUpperCase(), facultyIndex));
                    facultyUser.setEmail(String.format("faculty%d.%s@college.edu", facultyIndex, departmentId));
                    facultyUser.setCreatedByUserId("hod_" + departmentId);
                    facultyUser.setCreatedByRole("hod");
                    userRepository.save(facultyUser);
                }

                String facultyId = String.format("fac-%s-%d", departmentId, facultyIndex);
                Faculty faculty = facultyRepository.findById(facultyId).orElseGet(Faculty::new);
                faculty.setId(facultyId);
                faculty.setName(branchName + " Faculty " + facultyIndex);
                faculty.setEmployeeId(String.format("FAC-%s-%02d", departmentId.toUpperCase(), facultyIndex));
                faculty.setDepartmentId(departmentId);
                faculty.setBranch(branchName);
                faculty.setTeaching(buildTeachingAssignments(assignedSubjectIds, departmentSubjects, facultyIndex));
                facultyRepository.save(faculty);
            }
        }
    }


    private void enforceSingleHodPerDepartment(Map<String, String> departments) {
        Map<String, String> expectedHodUsernameByDepartment = new HashMap<>();
        for (String departmentId : departments.keySet()) {
            expectedHodUsernameByDepartment.put(departmentId, "hod_" + departmentId);
        }

        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (user.getRole() != Role.HOD) {
                continue;
            }

            String departmentId = user.getDepartmentId();
            String expectedUsername = expectedHodUsernameByDepartment.get(departmentId);
            if (expectedUsername == null) {
                userRepository.deleteById(user.getId());
                continue;
            }

            if (!expectedUsername.equalsIgnoreCase(user.getUsername())) {
                userRepository.deleteById(user.getId());
            }
        }
    }
    private List<String> pickAssignedSubjects(List<Subject> departmentSubjects, int facultyIndex) {
        int size = departmentSubjects.size();
        List<String> subjectIds = new ArrayList<>();
        int start = (facultyIndex - 1) * 2;

        for (int offset = 0; offset < 3; offset++) {
            Subject subject = departmentSubjects.get((start + offset) % size);
            subjectIds.add(subject.getId());
        }

        return subjectIds;
    }

    private List<Map<String, Object>> buildTeachingAssignments(List<String> subjectIds,
                                                                List<Subject> departmentSubjects,
                                                                int facultyIndex) {
        Map<String, Subject> byId = new HashMap<>();
        for (Subject subject : departmentSubjects) {
            byId.put(subject.getId(), subject);
        }

        List<Map<String, Object>> teaching = new ArrayList<>();
        int section = facultyIndex;

        for (String subjectId : subjectIds) {
            Subject subject = byId.get(subjectId);
            if (subject == null) continue;

            Map<String, Object> entry = new HashMap<>();
            entry.put("subjectId", subjectId);
            entry.put("year", subject.getYear());
            entry.put("section", section);
            teaching.add(entry);
            section++;
        }

        return teaching;
    }
}
