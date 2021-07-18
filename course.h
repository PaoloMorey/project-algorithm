#ifndef PROYECTO_SOFT_COURSE_H
#define PROYECTO_SOFT_COURSE_H

#include <utility>

#include "header.h"

class course {
private:
    std::string name;
    int theoHours{};
    int labHours{};
    int theoSections{};
    int labSections{};
    int theoCapacity{};
    int labCapacity{};
    std::string classroomTheoType;
    std::string classroomLabType;
    std::string major;
    int semester{};
public:

    course() = default;

    course(std::string name, int theoHours, int labHours, int theoSections, int labSections, int theoCapacity,
           int labCapacity,
           std::string classroomTheoType,
           std::string classroomLabType, std::string major, int semester) {
        this->name = std::move(name);
        this->theoHours = theoHours;
        this->labHours = labHours;
        this->theoSections = theoSections;
        this->labSections = labSections;
        this->theoCapacity = theoCapacity;
        this->labCapacity = labCapacity;
        this->classroomTheoType = std::move(classroomTheoType);
        this->classroomLabType = std::move(classroomLabType);
        this->major = std::move(major);
        this->semester = semester;
    }

    std::string getName() {
        return name;
    }

    std::string getMajor() {
        return major;
    }

    int getSemester() {
        return semester;
    }

    int getTheoHours() {
        return theoHours;
    }

    int getLabHours() {
        return labHours;
    }

    int getTheoSections() {
        return theoSections;
    }

    int getLabSections() {
        return labSections;
    }

    std::string getClassroomTheoType() {
        return classroomTheoType;
    }

    std::string getClassroomLabType() {
        return classroomLabType;
    }

};

#endif //PROYECTO_SOFT_COURSE_H
