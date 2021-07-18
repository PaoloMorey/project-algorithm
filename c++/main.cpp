#include "distribution.h"

int main() {
    course c("Ingenieria de Software 1", 2, 4, 2, 2, 45, 45, "comun", "comun", "CS", 5);
    course c1("Base de Datos 2", 1, 4, 1, 1, 45, 45, "comun", "comun", "CS", 5);
    course c3("Sistemas Operativos", 2, 4, 1, 1, 45, 45, "comun", "comun", "CS", 5);
    course c4("Compiladores", 2, 4, 1, 1, 45, 45, "comun", "comun", "CS", 5);
    course c5("Analisis y Diseno de Algoritmos", 2, 4, 50, 50, 45, 45, "comun", "comun", "CS", 5);
    classroom cl(601, "A601", "comun");
    classroom cl1(602, "A602", "comun");
    std::unordered_map<std::string, std::vector<course>> utecCourses;
    std::vector<course> csCourses;
    std::vector<classroom> totalClassrooms;
    csCourses.push_back(c);
    csCourses.push_back(c1);
    csCourses.push_back(c3);
    csCourses.push_back(c4);
    csCourses.push_back(c5);
    totalClassrooms.push_back(cl);
    utecCourses["CS"] = csCourses;
    distribution distribution(utecCourses, totalClassrooms);
    distribution.generateDistribution();
    distribution.showDistribution();
    distribution.showDetailedDistribution();
    return 0;
}
