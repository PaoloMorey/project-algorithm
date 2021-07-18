// package mestoribios.proyecto.data.entities;
import java.util.Map;

public class DistributionElem {
    String major;
    int semester;
    Map<Integer, Boolean> crosses;

    DistributionElem(String major, int semester, Map<Integer, Boolean> crosses){
        this.major = major;
        this.semester = semester;
        this.crosses = crosses;
    }
}
