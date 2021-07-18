// package mestoribios.proyecto.data.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Main
{
	public static void main(String[] args) {
                Course c1 = new Course("Ingenieria de Software 1", 2, 4, 2, 2, 45, 45, "comun", "comun", "CS", 5);
                Course c2 = new Course("Base de Datos 2", 1, 4, 1, 1, 45, 45, "comun", "comun", "CS", 5);
                Course c3 = new Course("Sistemas Operativos", 2, 4, 1, 1, 45, 45, "comun", "comun", "CS", 5);
                Course c4 = new Course("Compiladores", 2, 4, 1, 1, 45, 45, "comun", "comun", "CS", 5);
                Course c5 = new Course("Analisis y Diseno de Algoritmos", 2, 4, 100, 100, 45, 45, "comun", "comun", "CS", 5);
                Classroom cl1 = new Classroom(601, "A601", "comun");
                // Classroom cl2 = new Classroom(602, "A602", "comun");
                Map<String, ArrayList<Course>> utecCourses = new HashMap<>();
                ArrayList<Course> csCourses = new ArrayList<>();
                ArrayList<Classroom> totalClassrooms = new ArrayList<>();
                csCourses.add(c1);
                csCourses.add(c2);
                csCourses.add(c3);
                csCourses.add(c4);
                csCourses.add(c5);
                totalClassrooms.add(cl1);
                // totalClassrooms.add(cl2);
                utecCourses.put("CS", csCourses);
                Distribution distribution = new Distribution(utecCourses, totalClassrooms);
                distribution.generateDistribution();
                distribution.showDetailedDistribution();
                distribution.showDistribution();
                // distribution.getSalonesFaltantes();
	}
}
