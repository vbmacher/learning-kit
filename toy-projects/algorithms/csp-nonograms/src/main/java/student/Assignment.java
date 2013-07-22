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
import java.util.function.Function;

class Assignment {
    private final Collection<Constraint> constraints = new ArrayList<>();
    private final Collection<BinaryConstraint> binaryConstraints = new ArrayList<>();

    private int variablesCount = 0;

    final Block[][][] blocks;
    final int[][][] variables;

    final Map<String, Integer>[][] blockColorLengths;
    final Map<String, Integer>[][] maxBlockColorLengths;

    Assignment(Block[][][] blocks, Collection<Constraint> constraints, Collection<BinaryConstraint> binaryConstraints) {
        this.blocks = Objects.requireNonNull(blocks);
        this.variables = initVariables(blocks);
        this.constraints.addAll(constraints);
        this.binaryConstraints.addAll(binaryConstraints);

        blockColorLengths = fillBlockColorLengths();
        maxBlockColorLengths = fillMaxBlockColorLengths();
    }

    public Monad blocks(Monad dimensionMonad, Monad indexMonad, Function<Block, Monad> operation) {
        Monad monad = new Monad(indexMonad.index);
        for (int blockIndex = 0; blockIndex < variables[dimensionMonad.index][indexMonad.index].length; blockIndex++) {
            monad.addMonad(operation.apply(blocks[dimensionMonad.index][indexMonad.index][blockIndex]));
        }
        return monad;
    }

    public Monad blockIndexes(Monad dimensionMonad, Monad indexMonad, Function<Integer, Monad> operation) {
        Monad monad = new Monad(indexMonad.index);
        for (int blockIndex = 0; blockIndex < variables[dimensionMonad.index][indexMonad.index].length; blockIndex++) {
            monad.addMonad(operation.apply(blockIndex));
        }
        return monad;
    }

    public Monad indexes(Monad dimensionMonad) {
        Monad indexMonad = new Monad(dimensionMonad.index);
        for (int index = 0; index < variables[dimensionMonad.index].length; index++) {
            indexMonad.addMonad(new Monad(index));
        }
        return indexMonad;
    }

    public Monad dimensions() {
        Monad dimensionMonad = new Monad(0);
        for (int dimension = 0; dimension < variables.length; dimension++) {
            dimensionMonad.addMonad(new Monad(dimension));
        }
        return dimensionMonad;
    }


    @FunctionalInterface
    interface ThreeArgsConsumer<T,U,V> {
        void accept(T t, U u, V v);
    }

    public void forAll(ThreeArgsConsumer<Integer, Integer, Integer> consumer) {
        dimensions().flatMap(dim -> indexes(dim).flatMap(index -> blockIndexes(dim, index, blockIndex -> {
            consumer.accept(dim.index, index.index, blockIndex);
            return Monad.end();
        })));
    }

    private Map<String, Integer>[][] fillBlockColorLengths() {
        Map<String, Integer>[][] result = new Map[variables.length][];

        dimensions().flatMap(dim -> {
            int dimension = dim.index;
            result[dimension] = new Map[variables[dimension].length];
            return indexes(dim).flatMap(in -> {
                int index = in.index;
                result[dimension][index] = new HashMap<>();

                return blocks(dim, in, block -> {
                    if (result[dimension][index].containsKey(block.color)) {
                        result[dimension][index].put(block.color, result[dimension][index].get(block.color) + block.length);
                    } else {
                        result[dimension][index].put(block.color, block.length);
                    }
                    return Monad.end();
                });
            });
        });
        return result;
    }

    private Map<String, Integer>[][] fillMaxBlockColorLengths() {
        Map<String, Integer>[][] result = new Map[variables.length][];

        dimensions().flatMap(dim -> {
            int dimension = dim.index;
            result[dimension] = new Map[variables[dimension].length];

            return indexes(dim).flatMap(in -> {
                int index = in.index;
                result[dimension][index] = new HashMap<>();

                return blocks(dim, in, block -> {
                    if (result[dimension][index].containsKey(block.color)) {
                        result[dimension][index].put(block.color, Math.max(result[dimension][index].get(block.color), block.length));
                    } else {
                        result[dimension][index].put(block.color, block.length);
                    }
                    return Monad.end();
                });
            });
        });
        return result;
    }


    private int[][][] initVariables(Block[][][] blocks) {
        int[][][] var = new int[blocks.length][][];

        variablesCount = 0;
        dimensions().flatMap(dim -> {
            int dimension = dim.index;
            var[dimension] = new int[blocks[dimension].length][];

            return indexes(dim).flatMap(in -> {
                int index = in.index;
                var[dimension][index] = new int[blocks[dimension][index].length];

                variablesCount += var[dimension][index].length;

                return blockIndexes(dim, in, blockIndex -> {
                    var[dimension][index][blockIndex] = -1;
                    return Monad.end();
                });
            });
        });

        return var;
    }


    boolean isComplete() {
        Monad result = dimensions().flatMap(d -> indexes(d).flatMap(i -> blockIndexes(d, i, b -> {
            if (variables[d.index][i.index][b] == -1) {
                return Monad.fail();
            }
            return Monad.end();
        })));
        return !result.monads.isEmpty();
    }

    Collection<Selection> freeVariables() {
        List<Selection> freeVars = new ArrayList<>();
        dimensions().flatMap(d -> indexes(d).flatMap(i -> blockIndexes(d,i, blockIndex -> {
            int value = variables[d.index][i.index][blockIndex];
            if (value == -1) {
                freeVars.add(new Selection(d.index, i.index, blockIndex, this));
            }
            return Monad.end();
        })));
        return freeVars;
    }

    Collection<Selection> freeBlockVars(Selection sel) {
        List<Selection> relatedVars = new ArrayList<>();
        for (int blockIndex = 0; blockIndex < variables[sel.dimension][sel.index].length; blockIndex++) {
            if (blockIndex == sel.blockIndex) {
                continue;
            }
            if (variables[sel.dimension][sel.index][blockIndex] == -1) {
                relatedVars.add(new Selection(sel.dimension, sel.index, blockIndex, sel.assignment));
            }
        }
        return relatedVars;
    }

    Collection<Selection> relatedVariables(Selection sel, Domains domains) {
        List<Selection> relatedVars = new ArrayList<>();

        relatedVars.addAll(freeBlockVars(sel));

        List<Integer> selDomain = domains.domain(sel);
        if (selDomain.isEmpty()) {
            return Collections.emptyList();
        }
        if (sel.index == 1 && sel.dimension == 0 && sel.blockIndex == 0) {
            System.out.println(selDomain);
        }


        Block block = sel.block();
        int startPos = selDomain.get(0);
        int stopPos = selDomain.get(selDomain.size() - 1) + block.length;

        for (int index = startPos; index < stopPos; index++) {
            int[] transposedVar = sel.transposedVarPerIndex(index);
            for (int blockIndex = 0; blockIndex < transposedVar.length; blockIndex++) {
                Selection transposed = sel.transposed(index, blockIndex);
                List<Integer> transposedDomain = domains.domain(transposed);
                if (!transposedDomain.isEmpty() && transposedDomain.get(0) > sel.index) {
                    break;
                }
                if (transposedVar[blockIndex] == -1) {
                    relatedVars.add(sel.transposed(index, blockIndex));
                }
            }

        }

        System.out.println("Rerlated vars for " + sel + " are: " + relatedVars);
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

    boolean assign(Selection sel, String[][] grid, int value) {
        if (!CSPMain.fillBlockOnGrid(grid, sel, value)) {
            return false;
        }
        variables[sel.dimension][sel.index][sel.blockIndex] = value;
        return true;
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

    boolean isBinaryConsistent(Selection x, Selection y) {
        for (BinaryConstraint constraint : binaryConstraints) {
            if (!constraint.check(x, y)) {
                return false;
            }
        }
        return true;
    }

    String stats() {
        return (variablesCount - freeVariables().size()) + " / " + variablesCount; // + " / " + failedConstraints;
    }

}
