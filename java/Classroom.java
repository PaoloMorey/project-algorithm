// package mestoribios.proyecto.data.entities;
import java.util.logging.Logger;

public class Classroom
{
    private Integer id;
    private String name;
    private String type;
    private boolean used = false;
    private boolean exist = true;
    private CourseElem[][] timeSchedule;
    static final Logger logger = Logger.getLogger(Classroom.class.getName());

    Classroom(){
        timeSchedule = new CourseElem[15][6];
    }

    Classroom(int id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;

        timeSchedule = new CourseElem[15][6];
        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 6; ++j) {
                timeSchedule[i][j] = new CourseElem("none", "none", -1, 0);
            }
        }
    }

    void fillHour(Course course, int hour, int day, int i) {
        CourseElem c = new CourseElem(course.getName(), course.getMajor(), course.getSemester(), i);
        timeSchedule[hour][day] = c;
    }

    boolean checkAvailability(int i, int j) {
        return timeSchedule[i][j].name == "none" && timeSchedule[i][j].major == "none" &&
               timeSchedule[i][j].semester == -1 && timeSchedule[i][j].section == 0;
    }

    String getType(){
        return type;
    }

    void setUsed(){
        this.used = true;
    }

    void setExist(){
        this.exist = false;
    }

    int getId(){
        return this.id;
    }

    boolean getUsed() {
        return this.used;
    }

    boolean getExist() {
        return this.exist;
    }
    
    void printTimeSchedule() {
        String strTimeSchedule = name + "\n";
        for (int i = 0; i < 15; ++i) {
            strTimeSchedule += Integer.toString(i+7)+":00";
            for (int j = 0; j < 6; ++j) {
                strTimeSchedule += "\t"+timeSchedule[i][j].name+","+Integer.toString(timeSchedule[i][j].section)+","+timeSchedule[i][j].major+"\t";
            }
            strTimeSchedule += "\n";
        }
        logger.info(strTimeSchedule);
    }
}