#ifndef PROYECTO_SOFT_DISTRIBUTION_H
#define PROYECTO_SOFT_DISTRIBUTION_H

#include <utility>

#include "header.h"
#include "course.h"
#include "classroom.h"

struct distributionElem {
    std::string major;
    int semester;
    std::unordered_map<int, bool> crosses;
};

class distribution {
private:
    std::unordered_map<std::string, std::vector<course>> courses;
    std::vector<classroom> totalClassrooms;
    std::unordered_map<std::string, int> existingClassroomsNeeded;
    std::unordered_map<std::string, int> notExistingClassroomsNeeded;
    std::vector<classroom> classroomsNeeded;
    std::unordered_map<float, std::vector<distributionElem>> crossingOfSchedules;
    std::string nameForNewClassrooms = "NEW";
    int idForNewClassrooms = 0;

public:
    distribution() = default;

    distribution(std::unordered_map<std::string, std::vector<course>> &courses, std::vector<classroom> &totalClassrooms) {
        this->courses = std::move(courses);
        this->totalClassrooms = std::move(totalClassrooms);
    }

    void updateClassroom(classroom &a){
        for(int i = 0; i < totalClassrooms.size(); ++i){
            if (totalClassrooms[i].getId() == a.getId()){
                totalClassrooms[i] = a;
            }
        }
    }

    void allocateInMatrix(classroom &a, course &b, int hours, int beginTime, int day, int section) {
        for (int i = beginTime; i < beginTime + hours; ++i) {
            a.fillHour(b, i, day, section);
            updateClassroom(a);
        }
        float key = beginTime + day * 0.10;
        std::unordered_map<int, bool> crosses;
        crosses[section] = true;
        distributionElem elem{b.getMajor(), b.getSemester(), crosses};
        if (crossingOfSchedules.find(key) == crossingOfSchedules.end()) {
            std::vector<distributionElem> vec;
            vec.push_back(elem);
            crossingOfSchedules[key] = vec;
        } else {
            crossingOfSchedules[key].push_back(elem);
        }
    }

    static bool checkTypeOfClassroom(bool theo, classroom &c, course &a) {
        if (theo) return c.getType() == a.getClassroomTheoType();
        else return c.getType() == a.getClassroomLabType();
    }

    static bool notInDays(int j, std::vector<int> &days) {
        for (auto e: days) {
            if (e == j) return false;
        }
        return true;
    }

    bool checkCrossing(int i, int j, int section, course &a) {
        float key = i + j * 0.10;
        if (crossingOfSchedules.find(key) == crossingOfSchedules.end()) return true;
        else {
            std::vector<distributionElem> vec = crossingOfSchedules[key];
            for (auto v: vec) {
                if (v.semester == a.getSemester() && v.major == a.getMajor()) {
                    if (v.crosses.find(section) != v.crosses.end()) return false;
                }
            }
        }
        return true;
    }

    std::pair<classroom, std::pair<int, int>>
    makeMyPair(course &a, int hours, std::vector<int> &days, int n,
               int m, bool theo, classroom &c, int section) {
        std::pair<classroom, std::pair<int, int>> available;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (c.checkAvailability(i, j) && notInDays(j, days) && checkTypeOfClassroom(theo, c, a) &&
                    checkCrossing(i, j, section, a)) {
                    available.first = c;
                    available.second.first = i;
                    available.second.second = j;
                    if (hours == 1) return available;
                    else {
                        if (c.checkAvailability(i + 1, j)) return available;

                    }
                }
            }
        }
        available.first = c;
        available.second.first = -2;
        available.second.second = -2;
        return available;
    }

    std::pair<classroom, std::pair<int, int>>
    function(course &a, int hours, std::vector<int> &days, int n,
             int m, bool theo, int section) {
        std::pair<classroom, std::pair<int, int>> available;
        for (auto &c: totalClassrooms) {
            available = makeMyPair(a, hours, days, n, m, theo, c, section);
            if (available.second.first != -2 && available.second.second != -2) break;
        }
        if (available.second.first != -2 && available.second.second != -2) return available;
        std::string type;
        if (theo) type = a.getClassroomTheoType();
        else type = a.getClassroomLabType();
        classroom newClassroom(idForNewClassrooms, nameForNewClassrooms + std::to_string(idForNewClassrooms), type);
        idForNewClassrooms++;
        newClassroom.setExist();
        newClassroom.setUsed();
        totalClassrooms.push_back(newClassroom);
        available = makeMyPair(a, hours, days, n, m, theo, newClassroom, section);
        return available;
    }

    std::pair<classroom, std::pair<int, int>>
    checkForClassroomAvailable(course &a, int hours, std::vector<int> &days, bool theo, int section) {
        std::pair<classroom, std::pair<int, int>> available;
        bool first_year;
        if (a.getSemester() <= 2) first_year = true;
        else first_year = false;
        if (first_year) {
            available = function(a, hours, days, 10, 6, theo, section);
        } else {
            available = function(a, hours, days, 14, 6, theo, section);
        }
        return available;
    }

    void allocateCourse(course &a) {
        int i, j, classes, hours;
        std::pair<classroom, std::pair<int, int>> b;
        for (i = 0; i < a.getTheoSections(); ++i) {
            if (a.getTheoHours() % 2 == 1) classes = (a.getTheoHours() / 2) + 1;
            else classes = a.getTheoHours()/2;
            for (j = 0; j < classes; ++j) {
                std::vector<int> days;
                if (j < a.getTheoHours() / 2) hours = 2;
                else hours = 1;
                b = checkForClassroomAvailable(a, hours, days, true, i+1);
                days.push_back(b.second.second);
                b.first.setUsed();
                updateClassroom(b.first);
                allocateInMatrix(b.first, a, hours, b.second.first, b.second.second, i+1);
            }
        }
        for (i = 0; i < a.getLabSections(); ++i) {
            if (a.getLabHours() % 2 == 1) classes = a.getLabHours() / 2 + 1;
            else classes = a.getLabHours()/2;
            for (j = 0; j < classes; ++j) {
                std::vector<int> days;
                if (j < a.getLabHours() / 2) hours = 2;
                else hours = 1;
                b = checkForClassroomAvailable(a, hours, days, false, i+1);
                days.push_back(b.second.second);
                b.first.setUsed();
                updateClassroom(b.first);
                allocateInMatrix(b.first, a, hours, b.second.first, b.second.second, i+1);
            }
        }
    }

    void generateDistribution() {
        for (auto &course : courses) {
            for (auto j = 0; j <= course.second.size(); ++j) {
                allocateCourse((course.second)[j]);
            }
        }
        for (auto &classroom: totalClassrooms) {
            if (classroom.getUsed()) {
                classroomsNeeded.push_back(classroom);
                if (classroom.getExist()) {
                    if (existingClassroomsNeeded.find(classroom.getType()) ==
                        existingClassroomsNeeded.end())
                        existingClassroomsNeeded[classroom.getType()] = 1;
                    else existingClassroomsNeeded[classroom.getType()] += 1;
                } else {
                    if (notExistingClassroomsNeeded.find(classroom.getType()) ==
                        notExistingClassroomsNeeded.end())
                        notExistingClassroomsNeeded[classroom.getType()] = 1;
                    else notExistingClassroomsNeeded[classroom.getType()] += 1;
                }
            }
        }
    }

    void showDistribution() {
        std::cout<<"Existing Classrooms\n";
        for (auto & i : existingClassroomsNeeded) {
            std::cout<<i.first<<" "<<i.second << "\n";
        }
        std::cout<<"Non-Existing Classrooms\n";
        for (auto & i : notExistingClassroomsNeeded) {
            std::cout<<i.first<<" "<<i.second << "\n";
        }
    }


    void showDetailedDistribution() {
        for (auto & classroom :classroomsNeeded) {
            classroom.printTimeSchedule();
        }
    }
};

#endif //PROYECTO_SOFT_DISTRIBUTION_H
