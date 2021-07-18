#ifndef PROYECTO_SOFT_CLASSROOM_H
#define PROYECTO_SOFT_CLASSROOM_H

#include <utility>

#include "header.h"

struct courseElem {
    std::string name;
    std::string major;
    int semester;
    int section;
};

class classroom {
private:
    int id{};
    std::string name;
    std::string type;
    courseElem timeSchedule[15][6];
    bool used = false;
    bool exist = true;
public:

    classroom() = default;

    classroom(int id, std::string name, std::string type) {
        this->id = id;
        this->name = std::move(name);
        this->type = std::move(type);
        for (auto &i : timeSchedule) {
            for (auto &j : i) {
                courseElem c{"none", "none", -1, 0};
                j = c;
            }
        }
    }

    void fillHour(course &course_, int hour, int day, int i) {
        courseElem c{course_.getName(), course_.getMajor(), course_.getSemester(), i};
        timeSchedule[hour][day] = c;
    }

    bool checkAvailability(int i, int j) {
        return timeSchedule[i][j].name == "none" && timeSchedule[i][j].major == "none" &&
               timeSchedule[i][j].semester == -1 && timeSchedule[i][j].section == 0;
    }

    std::string getType(){
        return type;
    }

    void setUsed(){
        this->used = true;
    }

    void setExist(){
        this->exist = false;
    }

    int getId(){
        return this->id;
    }

    bool getUsed() {
        return this->used;
    }

    bool getExist() {
        return this->exist;
    }

    void printTimeSchedule(){
        std::cout<<name<<"\n";
        for (int i = 0; i < 15; ++i) {
            std::cout<<i+7<<":00";
            for (int j = 0; j < 6; ++j) {
                std::cout<<"\t"<<timeSchedule[i][j].name<<","<<timeSchedule[i][j].section<<","<<timeSchedule[i][j].major<<"\t";
            }
            std::cout<<"\n";
        }
        std::cout<<"\n";
    }
};

#endif //PROYECTO_SOFT_CLASSROOM_H
