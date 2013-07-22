package student;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Monad {
    List<Monad> monads = new ArrayList<>();
    final int index;
    boolean thisFailed;

    public static Monad empty = new Monad(0);
    public static Monad failed = new Monad(0, true);

    public Monad(int index) {
        this.index = index;
    }

    public Monad(int index, boolean failed) {
        this.index = index;
        this.thisFailed = failed;
    }

    public Monad addMonad(Monad monad) {
        monads.add(monad);
        return this;
    }

    public static Monad fail() {
        return failed;
    }

    public static Monad end() {
        return empty;
    }

    public Monad flatMap(Function<Monad, Monad> operation) {
        return monads
                .stream()
                .flatMap(monad -> {
                    Monad result = operation.apply(monad);
                    if (result.thisFailed) {
                        monad.thisFailed = true; // failure propagation
                        return Stream.empty();
                    } else {
                        return result.monads.stream();
                    }
                })
                .reduce(new Monad(index), (acc, monad) -> {
                    if (monad.thisFailed) {
                        acc.thisFailed = true;
                    }
                    if (acc.thisFailed) {
                        return acc;
                    } else {
                        return acc.addMonad(monad);
                    }
                });
    }

    public <T>List<T> map(Function<Integer, T> operation) {
        return monads.stream().map(monad -> operation.apply(monad.index)).collect(Collectors.toList());
    }

}
