package student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Domains {
    final List<Integer>[][][] domain;

    Domains(List<Integer>[][][] domain) {
        this.domain = Objects.requireNonNull(domain);
    }

    Domains backup() {
        List<Integer>[][][] newDomain = new List[domain.length][][];
        for (int dimension = 0; dimension < domain.length; dimension++) {
            newDomain[dimension] = new List[domain[dimension].length][];
            for (int index = 0; index < domain[dimension].length; index++) {
                newDomain[dimension][index] = new List[domain[dimension][index].length];
                for (int blockIndex = 0; blockIndex < domain[dimension][index].length; blockIndex++) {
                    newDomain[dimension][index][blockIndex] = new ArrayList<>(domain[dimension][index][blockIndex]);
                }
            }
        }
        return new Domains(newDomain);
    }

    List<Integer>[] perIndex(Selection sel) {
        return domain[sel.dimension][sel.index];
    }

    List<Integer> domain(Selection sel) {
        return perIndex(sel)[sel.blockIndex];
    }

}
