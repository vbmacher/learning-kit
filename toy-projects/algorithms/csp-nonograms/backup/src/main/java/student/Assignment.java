package student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

class Assignment {
    final Block[][][] blocks;
    final int[][][] variables;
    final Collection<Constraint> constraints;
    int variablesCount = 0;

    final Map<String, Integer>[][] blockColorLengths;
    final Map<String, Integer>[][] maxBlockColorLengths;

    Assignment(Block[][][] blocks, Collection<Constraint> constraints) {
        this.blocks = Objects.requireNonNull(blocks);
        this.variables = initVariables(blocks);
        this.constraints = Objects.requireNonNull(constraints);

        blockColorLengths = fillBlockColorLengths();
        maxBlockColorLengths = fillMaxBlockColorLengths();
    }

    private Map<String, Integer>[][] fillBlockColorLengths() {
        Map<String, Integer>[][] result = new Map[variables.length][];

        for (int dimension = 0; dimension < variables.length; dimension++) {
            result[dimension] = new Map[variables[dimension].length];

            for (int index = 0; index < variables[dimension].length; index++) {
                result[dimension][index] = new HashMap<>();

                for (Block block : blocks[dimension][index]) {
                    if (result[dimension][index].containsKey(block.color)) {
                        result[dimension][index].put(block.color, result[dimension][index].get(block.color) + block.length);
                    } else {
                        result[dimension][index].put(block.color, block.length);
                    }
                }
            }
        }
        return result;
    }

    private Map<String, Integer>[][] fillMaxBlockColorLengths() {
        Map<String, Integer>[][] result = new Map[variables.length][];

        for (int dimension = 0; dimension < variables.length; dimension++) {
            result[dimension] = new Map[variables[dimension].length];

            for (int index = 0; index < variables[dimension].length; index++) {
                result[dimension][index] = new HashMap<>();

                for (Block block : blocks[dimension][index]) {
                    if (result[dimension][index].containsKey(block.color)) {
                        result[dimension][index].put(block.color, Math.max(result[dimension][index].get(block.color), block.length));
                    } else {
                        result[dimension][index].put(block.color, block.length);
                    }
                }
            }
        }
        return result;
    }


    private int[][][] initVariables(Block[][][] blocks) {
        int[][][] var = new int[blocks.length][][];

        variablesCount = 0;
        for (int dimension = 0; dimension < blocks.length; dimension++) {
            var[dimension] = new int[blocks[dimension].length][];
            for (int index = 0; index < blocks[dimension].length; index++) {
                var[dimension][index] = new int[blocks[dimension][index].length];

                variablesCount += var[dimension][index].length;

                for (int blockIndex = 0; blockIndex < blocks[dimension][index].length; blockIndex++) {
                    var[dimension][index][blockIndex] = -1;
                }
            }
        }
        return var;
    }


    boolean isComplete() {
        for (int[][] variablesPerDimension : variables) {
            for (int[] variablesPerIndex : variablesPerDimension) {
                for (int variable : variablesPerIndex) {
                    if (variable == -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    Collection<Selection> freeVariables() {
        List<Selection> freeVars = new ArrayList<>();

        for (int dimension = 0; dimension < variables.length; dimension++) {
            for (int index = 0; index < variables[dimension].length; index++) {
                for (int blockIndex = 0; blockIndex < variables[dimension][index].length; blockIndex++) {
                    int value = variables[dimension][index][blockIndex];
                    if (value == -1) {
                        freeVars.add(new Selection(dimension, index, blockIndex, this));
                    }
                }
            }
        }
        return freeVars;
    }

    Collection<Selection> relatedVariables(Selection sel, Domains domains) {
        List<Selection> relatedVars = new ArrayList<>();
        List<Integer> domain = domains.domain(sel);
        Block block = sel.block();

        if (domain.isEmpty()) {
            return Collections.emptyList();
        }
        int startPos = domain.get(0);
        int stopPos = domain.get(domain.size() - 1) + block.length;

        for (int index = startPos; index < stopPos; index++) {
            int[] transposedVar = sel.transposedVarPerIndex(index);
            for (int blockIndex = 0; blockIndex < transposedVar.length; blockIndex++) {
                if (transposedVar[blockIndex] == -1) {
                    relatedVars.add(sel.transposed(index, blockIndex));
                }
            }

        }
        return relatedVars;
    }

    Selection select(Domains domains) {
        // most-constrained variable = block with minimal domain
        NavigableMap<Integer, List<Selection>> forSelection = new TreeMap<>();
        for (Selection sel : freeVariables()) {
            int domainsCount = domains.domain(sel).size();

            if (!forSelection.containsKey(domainsCount)) {
                forSelection.put(domainsCount, new ArrayList<>());
            }
            List<Selection> sameDomains = forSelection.get(domainsCount);
            sameDomains.add(sel);
        }

        // most-constraining variable = longest block
        NavigableMap<Integer, Selection> result = new TreeMap<>();
        for (Map.Entry<Integer, List<Selection>> entry : forSelection.entrySet()) {
            Selection mostConstraining = null;
            Block mostConstrainingBlock = null;
            for (Selection sel : entry.getValue()) {
                Block selBlock = sel.block();
                if (mostConstraining == null || mostConstrainingBlock.length < selBlock.length) {
                    mostConstraining = sel;
                    mostConstrainingBlock = selBlock;
                }
            }
            result.put(entry.getKey(), mostConstraining);
        }

        return result.firstEntry().getValue();
    }

    void assign(Selection sel, String[][] grid, int value) {
        CSPMain.fillBlockOnGrid(grid, sel, value);
        variables[sel.dimension][sel.index][sel.blockIndex] = value;
    }

    void unassign(Selection sel) {
        variables[sel.dimension][sel.index][sel.blockIndex] = -1;
    }

    boolean isConsistent(Selection sel, String[][] grid) {
        for (Constraint constraint : constraints) {
            if (!constraint.check(sel, grid)) {
                return false;
            }
        }
        return true;
    }

    String stats() {
        return (variablesCount - freeVariables().size()) + " / " + variablesCount; // + " / " + failedConstraints;
    }

}
