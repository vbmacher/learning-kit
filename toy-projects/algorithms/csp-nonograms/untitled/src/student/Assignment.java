package student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

class Assignment {
    final Block[][][] blocks;
    final int[][][] variables;
    final Collection<Constraint> constraints;

    private final Constraint.InconsistencyCatch inconsistencyCatch = new InconsistencyCatcher();
    // runtime vars
    final Deque<Selection> assignedVars = new LinkedList<>(); // order is important
    Selection lastInconsistency = null;

    Assignment(Block[][][] blocks, Collection<Constraint> constraints) {
        this.blocks = Objects.requireNonNull(blocks);
        this.variables = initVariables(blocks);
        this.constraints = Objects.requireNonNull(constraints);
    }

    private int[][][] initVariables(Block[][][] blocks) {
        int[][][] var = new int[blocks.length][][];
        for (int dimension = 0; dimension < blocks.length; dimension++) {
            var[dimension] = new int[blocks[dimension].length][];

            for (int index = 0; index < blocks[dimension].length; index++) {
                var[dimension][index] = new int[blocks[dimension][index].length];

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

    Collection<Selection> relatedVariables(Selection sel, Domains domains, boolean onlyFreeVars) {
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
                if (onlyFreeVars && transposedVar[blockIndex] != -1) {
                    continue;
                }
                relatedVars.add(sel.transposed(index, blockIndex));
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

    private int findBlockIndex(int dimension, int index, int position, String color) {
        for (int blockIndex = 0; blockIndex < blocks[dimension][index].length; blockIndex++) {
            int varStart = variables[dimension][index][blockIndex];
            Block block = blocks[dimension][index][blockIndex];

            if (varStart <= position && varStart + block.length >= position) {
                if (block.color.equals(color)) {
                    return blockIndex;
                }
            }
        }
        return -1;
    }

    private Selection findSelectionOnGrid(String color, GridDimension dim) {
        for (int dimension = 0; dimension < variables.length; dimension++) {
            int index = dimension == 0 ? dim.row : dim.col;
            int position = dimension == 0 ? dim.col : dim.row;
            int blockIndex = findBlockIndex(dimension, index, position, color);

            if (blockIndex != -1) {
                if (variables[dimension][index][blockIndex] != -1) {
                    return new Selection(dimension, index, blockIndex, this);
                }
            }
        }
        return null;
    }

    private boolean fillBlockOnGrid(String[][] grid, Selection sel, int startPos) {
        Block block = sel.block();

        // verify if we can put selected block on the grid
        GridDimension dim = sel.toGridDimension(startPos);
        for (int k = 0; k < block.length; k++) {
            if (!(dim.cell(grid).isEmpty() || dim.cell(grid).equals(block.color))) {
                // cell already has a different color
                inconsistencyCatch.catchGridValue(dim, dim.cell(grid));
                return false;
            }
            dim.advance();
        }

        // draw the block
        dim.reset(startPos);
        for (int k = 0; k < block.length; k++) {
            dim.set(grid, block.color);
            dim.advance();
        }

        return true;
    }


    boolean assign(Selection sel, String[][] grid, int value) {
        lastInconsistency = null;
        if (!fillBlockOnGrid(grid, sel, value)) {
            return false;
        }
        variables[sel.dimension][sel.index][sel.blockIndex] = value;
        assignedVars.push(sel);
        return true;
    }

    Optional<Selection> unassignLast() {
        if (!assignedVars.isEmpty()) {
            Selection sel = assignedVars.pop();
            variables[sel.dimension][sel.index][sel.blockIndex] = -1;
            return Optional.of(sel);
        }
        return Optional.empty();
    }

    boolean isConsistent(Selection sel, String[][] grid, Domains domains) {
        if (!checkConstraints(sel, grid)) {
            return false;
        }
        for (Selection relatedVar : relatedVariables(sel, domains, false)) {
            if (!checkConstraints(relatedVar, grid)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkConstraints(Selection sel, String[][] grid) {
        for (Constraint constraint : constraints) {
            if (!constraint.check(sel, grid, inconsistencyCatch)) {
                return false;
            }
        }
        return true;
    }


    private class InconsistencyCatcher implements Constraint.InconsistencyCatch {

        @Override
        public void catchGridValue(GridDimension dim, String color) {
            Selection sel = findSelectionOnGrid(color, dim);

            System.out.println("\tnot consistent at grid! " + dim + "; "  + sel);
            if (sel == null) {
                System.out.println("\t\tWell, couldn't find selection. Assigned ones are: " + assignedVars);
            }
            lastInconsistency = sel;
        }

        @Override
        public void catchSelection(Selection selection) {
            System.out.println("\tnot consistent! " + selection);
            lastInconsistency = selection;
        }
    }
}
