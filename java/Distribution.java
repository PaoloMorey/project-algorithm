// package mestoribios.proyecto.data.entities;

import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
// import mestoribios.proyecto.config.Pair;

public class Distribution {
    private Map<String, ArrayList<Course>> courses;
    private ArrayList<Classroom> totalClassrooms;
    private Map<String, Integer> existingClassroomsNeeded;
    private Map<String, Integer> notExistingClassroomsNeeded;
    private ArrayList<Classroom> classroomsNeeded;
    private Map<Float, ArrayList<DistributionElem>> crossingOfSchedules;
    private String nameForNewClassrooms = "NEW";
    private int idForNewClassrooms = 0;
    static final Logger logger = Logger.getLogger(Distribution.class.getName());

    public Distribution() {
        // Distribution default constructor
        courses = new HashMap<String, ArrayList<Course>>();
        crossingOfSchedules = new HashMap<>();
        classroomsNeeded = new ArrayList<>();
        totalClassrooms  = new ArrayList<>();
        existingClassroomsNeeded = new HashMap<>();
        notExistingClassroomsNeeded = new HashMap<>();
    }

    public Distribution(Map<String, ArrayList<Course>> courses, ArrayList<Classroom> totalClassrooms) {
        this.courses = courses;
        this.totalClassrooms = totalClassrooms;
        crossingOfSchedules = new HashMap<>();
        classroomsNeeded = new ArrayList<>();
        totalClassrooms  = new ArrayList<>();
        existingClassroomsNeeded = new HashMap<>();
        notExistingClassroomsNeeded = new HashMap<>();
    }

    public void updateClassroom(Classroom a){
        for (int i = 0; i < totalClassrooms.size(); ++i){
            if (totalClassrooms.get(i).getId() == a.getId()){
                totalClassrooms.set(i, a);
            }
        }
    }

    public void allocateInMatrix(Classroom a, Course b, int hours, int beginTime, int day, int section) {
        for (int i = beginTime; i < beginTime + hours; ++i) {
            a.fillHour(b, i, day, section);
            updateClassroom(a);
        }
        float key = beginTime + day * 0.01f;
        Map<Integer, Boolean> crosses = new HashMap<>();
        crosses.put(section, true);
        DistributionElem elem = new DistributionElem(b.getMajor(), b.getSemester(), crosses);
        if (crossingOfSchedules.containsKey(key)) {
            crossingOfSchedules.get(key).add(elem);
        }
        else {
            ArrayList<DistributionElem> vec = new ArrayList<>();
            vec.add(elem);
            crossingOfSchedules.put(key, vec);
        }
    }

    public static boolean checkTypeOfClassroom(boolean theo, Classroom c, Course a) {
        if (theo) return c.getType() == a.getClassroomTheoType();
        else return c.getType() == a.getClassroomLabType();
    }

    public static boolean notInDays(int j, ArrayList<Integer> days) {
        for (int e : days) {
            if (e == j) return false;
        }
        return true;
    }

    public boolean checkCrossing(int i, int j, int section, Course a) {
        float key = i + j * 0.01f;
        if (!crossingOfSchedules.containsKey(key)) return true;
        ArrayList<DistributionElem> vec = crossingOfSchedules.get(key);
        for (DistributionElem v: vec) {
            if (v.semester == a.getSemester() && v.major == a.getMajor()) {
                if (v.crosses.containsKey(section)) return false;
            }
        }
        return true;
    }

    public Pair<Classroom, Pair<Integer, Integer>>
    makeMyPair(Course a, int hours, ArrayList<Integer> days, int n,
               int m, boolean theo, Classroom c, int section) {
        Pair<Classroom, Pair<Integer, Integer>> available = new Pair<>(new Classroom(), new Pair<>());
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (c.checkAvailability(i, j) && notInDays(j, days) && checkTypeOfClassroom(theo, c, a) &&
                    checkCrossing(i, j, section, a)) {
                    available.setFirst(c);
                    available.getSecond().setFirst(i);
                    available.getSecond().setSecond(j);
                    if (hours == 1) return available;
                    else {
                        if (c.checkAvailability(i + 1, j)) return available;
                    }
                }
            }
        }
        available.setFirst(c);
        available.getSecond().setFirst(-2);
        available.getSecond().setSecond(-2);
        return available;
    }

    public Pair<Classroom, Pair<Integer, Integer>>
    function(Course a, int hours, ArrayList<Integer> days, int n,
             int m, boolean theo, int section) {
        Pair<Classroom, Pair<Integer, Integer>> available = new Pair<>();
        for (Classroom c : totalClassrooms) {
            available = makeMyPair(a, hours, days, n, m, theo, c, section);
            if (available.getSecond().getFirst() != -2 && available.getSecond().getSecond() != -2) break;
        }

        if (available.getSecond().getFirst() != -2 && available.getSecond().getSecond() != -2) return available;
        String type;
        if (theo) type = a.getClassroomTheoType();
        else type = a.getClassroomLabType();
        Classroom newClassroom = new Classroom(idForNewClassrooms, nameForNewClassrooms + Integer.toString(idForNewClassrooms), type);
        ++idForNewClassrooms;
        newClassroom.setExist();
        newClassroom.setUsed();
        totalClassrooms.add(newClassroom);
        available = makeMyPair(a, hours, days, n, m, theo, newClassroom, section);
        return available;
    }

    public Pair<Classroom, Pair<Integer, Integer>>
    checkForClassroomAvailable(Course a, int hours, ArrayList<Integer> days, boolean theo, int section) {
        Pair<Classroom, Pair<Integer, Integer>> available = new Pair<>();
        boolean first_year;
        if (a.getSemester() <= 2) first_year = true;
        else first_year = false;
        if (first_year) {
            available = function(a, hours, days, 10, 6, theo, section);
        } else {
            available = function(a, hours, days, 14, 6, theo, section);
        }
        return available;
    }

    public void allocateCourse(Course a) {
        int i, j, classes, hours;
        Pair<Classroom, Pair<Integer, Integer>> b;
        for (i = 0; i < a.getTheoSections(); ++i) {
            if (a.getTheoHours() % 2 == 1) classes = (a.getTheoHours() / 2) + 1;
            else classes = a.getTheoHours()/2;
            for (j = 0; j < classes; ++j) {
                ArrayList<Integer> days = new ArrayList<>();
                if (j < a.getTheoHours() / 2) hours = 2;
                else hours = 1;
                b = checkForClassroomAvailable(a, hours, days, true, i+1);
                days.add(b.getSecond().getSecond());
                b.getFirst().setUsed();
                updateClassroom(b.getFirst());
                allocateInMatrix(b.getFirst(), a, hours, b.getSecond().getFirst(), b.getSecond().getSecond(), i+1);
            }
        }
        for (i = 0; i < a.getLabSections(); ++i) {
            if (a.getLabHours() % 2 == 1) classes = a.getLabHours() / 2 + 1;
            else classes = a.getLabHours()/2;
            for (j = 0; j < classes; ++j) {
                ArrayList<Integer> days = new ArrayList<>();
                if (j < a.getLabHours() / 2) hours = 2;
                else hours = 1;
                b = checkForClassroomAvailable(a, hours, days, false, i+1);
                days.add(b.getSecond().getSecond());
                b.getFirst().setUsed();
                updateClassroom(b.getFirst());
                allocateInMatrix(b.getFirst(), a, hours, b.getSecond().getFirst(), b.getSecond().getSecond(), i+1);
            }
        }
    }

    public void generateDistribution() {
        for (ArrayList<Course> course : courses.values()) {
            for (int j = 0; j < course.size(); ++j) {
                allocateCourse(course.get(j));
            }
        }
        for (Classroom classroom : totalClassrooms) {
            if (classroom.getUsed()) {
                classroomsNeeded.add(classroom);
                if (classroom.getExist()) {
                    if (existingClassroomsNeeded.containsKey(classroom.getType())) {
                        existingClassroomsNeeded.put(classroom.getType(), existingClassroomsNeeded.get(classroom.getType())+1);
                    }
                    else {
                        existingClassroomsNeeded.put(classroom.getType(), 1);
                    }
                } 
                else {
                    if (notExistingClassroomsNeeded.containsKey(classroom.getType())) {
                        notExistingClassroomsNeeded.put(classroom.getType(), notExistingClassroomsNeeded.get(classroom.getType())+1);
                    }
                    else {
                        notExistingClassroomsNeeded.put(classroom.getType(), 1);
                    }
                }
            }            
        }
    }

    public void showDistribution() {
        logger.info("Existing Classrooms");
        for (Map.Entry<String, Integer> entry : existingClassroomsNeeded.entrySet()) {
            logger.info(entry.getKey() + ": " + entry.getValue());
        }
        logger.info("Non-Existing Classrooms");
        for (Map.Entry<String, Integer> entry : notExistingClassroomsNeeded.entrySet()) {
            logger.info(entry.getKey() + ": " + entry.getValue());
        }
    }

    public int getSalonesFaltantes() {
        logger.info(Integer.toString(idForNewClassrooms));
        return (idForNewClassrooms+1);
    }

    public void showDetailedDistribution() {
        for (Classroom classroom : classroomsNeeded) {
            classroom.printTimeSchedule();
        }
    }
}