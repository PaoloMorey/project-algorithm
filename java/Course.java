// package mestoribios.proyecto.data.entities;

public class Course {
        private String name;
        private int theoHours;
        private int labHours;
        private int theoSections;
        private int labSections;
        private int theoCapacity;
        private int labCapacity;
        private String classroomTheoType;
        private String classroomLabType;
        private String major;
        private int semester;
    
        public Course() {
            // Course default constructor
        }
    
        public Course(String name, int theoHours, int labHours, int theoSections, int labSections, int theoCapacity,
               int labCapacity,
               String classroomTheoType,
               String classroomLabType, String major, int semester) {
            this.name = name;
            this.theoHours = theoHours;
            this.labHours = labHours;
            this.theoSections = theoSections;
            this.labSections = labSections;
            this.theoCapacity = theoCapacity;
            this.labCapacity = labCapacity;
            this.classroomTheoType = classroomTheoType;
            this.classroomLabType = classroomLabType;
            this.major = major;
            this.semester = semester;
        }
    
        public String getName() {
            return name;
        }
    
        public String getMajor() {
            return major;
        }
    
        public int getSemester() {
            return semester;
        }
    
        public int getTheoHours() {
            return theoHours;
        }
    
        public int getLabHours() {
            return labHours;
        }
    
        public int getTheoSections() {
            return theoSections;
        }
    
        public int getLabSections() {
            return labSections;
        }
    
        public String getClassroomTheoType() {
            return classroomTheoType;
        }
    
        public String getClassroomLabType() {
            return classroomLabType;
        }
    
    };