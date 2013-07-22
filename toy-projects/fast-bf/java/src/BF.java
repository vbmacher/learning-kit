import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BF {
  public final static int MEM_SIZE = 65536;

  public final static short I_STOP = ';';
  public final static short I_INC = '>';
  public final static short I_DEC = '<';
  public final static short I_INCV = '+';
  public final static short I_DECV = '-';
  public final static short I_PRINT = '.';
  public final static short I_READ = ',';
  public final static short I_LOOP_START = '[';
  public final static short I_LOOP_END = ']';
  public final static short I_COPY_AND_CLEAR = 0xA1; // any copyloop, including clear
  public final static short I_SCANLOOP = 0xA2; // [<] or [>]

  public final CachedOperation[] operationsCache = new CachedOperation[MEM_SIZE];
  public final int[] loopEndsCache = new int[MEM_SIZE];

  public final int[] memory = new int[MEM_SIZE];

  private final Deque<Integer> loopPointers = new ArrayDeque<>();

  private int repeatedOptsCount = 0;
  private int copyLoopsCount = 0;
  private int scanLoopsCount = 0;

  public int IP, P;

  public BF() {
    for (int i = 0; i < MEM_SIZE; i++) {
      loopEndsCache[i] = -1;
    }
  }

  public static final class CachedOperation {
    public int nextIP;

    // for repeats
    public int argument;
    public int operation;

    // for copyloops
    public List<CopyLoop> copyLoops;
  }

  public final static class CopyLoop {
    int factor;
    int relativePosition;

    int specialOP;

    public CopyLoop(int factor, int relativePosition) {
      this.factor = factor;
      this.relativePosition = relativePosition;
    }

  }

  @Override
  public String toString() {
    return "Profiler{optimizations=" + (repeatedOptsCount + copyLoopsCount + scanLoopsCount)
        + ", repeatedOps=" + repeatedOptsCount + ", copyLoops=" + copyLoopsCount
        + ", scanLoops=" + scanLoopsCount
        + "}";
  }



  public void profileAndOptimize(int programSize) {
    optimizeLoops(programSize);
    optimizeCopyLoops(programSize);
    optimizeScanLoops(programSize);
    optimizeRepeatingOperations(programSize);

    System.out.println(this);
  }

  private void optimizeRepeatingOperations(int programSize) {
    int lastOperation = -1;
    int OP;
    for (int tmpIP = 0; tmpIP < programSize; tmpIP++) {
      OP = memory[tmpIP];
      if (OP != I_LOOP_START && OP != I_LOOP_END && (lastOperation == OP)) {
        int previousIP = tmpIP - 1;
        CachedOperation operation = new CachedOperation();

        operation.operation = OP;
        operation.argument = 2;

        while ((tmpIP+1) < programSize && (memory[tmpIP+1] == lastOperation)) {
          operation.argument++;
          tmpIP++;
        }
        operation.nextIP = tmpIP + 1;
        if (operationsCache[previousIP] == null) {
          operationsCache[previousIP] = operation;
          repeatedOptsCount++;
        }
      }
      lastOperation = OP;
    }
  }

  private void optimizeLoops(int programSize) {
    int OP;
    for (int tmpIP = 0; tmpIP < programSize; tmpIP++) {
      if (memory[tmpIP] != I_LOOP_START) {
        continue;
      }
      int loop_count = 0; // loop nesting level counter

      // we start to look for "]" instruction
      // on the same nesting level (according to loop_count value)
      // IP is pointing at following instruction
      int tmpIP2 = tmpIP + 1;
      while ((tmpIP2 < programSize) && (OP = memory[tmpIP2++]) != I_STOP) {
        if (OP == I_LOOP_START) {
          loop_count++;
        }
        if (OP == I_LOOP_END) {
          if (loop_count == 0) {
            loopEndsCache[tmpIP] = tmpIP2;
            break;
          } else {
            loop_count--;
          }
        }
      }
    }
  }

  private void optimizeCopyLoops(int programSize) {
    int OP;
    for (int tmpIP = 0; tmpIP < programSize; tmpIP++) {
      OP = memory[tmpIP];
      if (OP == I_LOOP_START && tmpIP+2 < programSize) {
        tmpIP++;
        OP = memory[tmpIP];
        CachedOperation copyLoop = findCopyLoop(tmpIP, OP);

        if (copyLoop != null) {
          operationsCache[tmpIP - 1] = copyLoop;
          tmpIP = copyLoop.nextIP;
          copyLoopsCount++;
        }
      }
    }
  }

  private int[] repeatRead(int incOp, int decOp, int pos, int stopPos, int var) {
    if (pos >= stopPos) {
      return new int[] { pos, var};
    }
    do {
      int op = memory[pos];
      if (op == incOp) {
        var++;
      } else if (op == decOp) {
        var--;
      } else break;
      pos++;
    } while (pos < stopPos);
    return new int[] { pos, var };
  }

  private int readSpecial(int startIP, int stopIP, List<CopyLoop> copyLoops) {
    if (startIP > stopIP) {
      return startIP;
    }
    do {
      int specialOP = memory[startIP];
      if (specialOP == I_PRINT || specialOP == I_READ) {
        CopyLoop c = new CopyLoop(0, 0);
        c.specialOP = specialOP;
        copyLoops.add(c);
        startIP++;
      } else break;
    } while (startIP < stopIP);
    return startIP;
  }

  private CachedOperation findCopyLoop(int tmpIP, int OP) {
    if (loopEndsCache[tmpIP - 1] == -1) {
      return null; // we don't have optimized loops
    }

    int startIP = tmpIP;
    int stopIP = loopEndsCache[tmpIP - 1] - 1;
    int nextIP = stopIP + 1;

    // first find [-  ...] or [... -]
    if (OP == I_DECV) {
      startIP++;
    } else if (memory[stopIP - 1] == I_DECV) {
      stopIP--;
    } else {
      return null; // not a copy-loop
    }

    // now identify the copyloops. General scheme:
    // 1. pointer increments / decrements
    //   2. value updates
    // 3. pointer decrements / increments in reverse order
    // 4. repeat - basically on the end the pointer should be on the same position

    int pointerInvEntrophy = 0;
    List<CopyLoop> copyLoops = new ArrayList<>();

    while (startIP < stopIP) {
      startIP = readSpecial(startIP, stopIP, copyLoops);

      int[] posVar = repeatRead(I_INC, I_DEC, startIP, stopIP, pointerInvEntrophy);
      if (posVar[0] == startIP) {
        break; // weird stuff
      }
      startIP = posVar[0];
      pointerInvEntrophy = posVar[1];

      if (pointerInvEntrophy == 0) {
        break;
      }

      int[] posFactor = repeatRead(I_INCV, I_DECV, startIP, stopIP, 0);
      if (posFactor[0] == startIP) {
        break; // weird stuff
      }
      startIP = posFactor[0];

      copyLoops.add(new CopyLoop(posFactor[1],pointerInvEntrophy));
    }
    if (pointerInvEntrophy == 0 && startIP == stopIP) {
      CachedOperation operation = new CachedOperation();
      operation.copyLoops = copyLoops;
      operation.nextIP = nextIP;
      operation.operation = I_COPY_AND_CLEAR;
      return operation;
    }
    return null;
  }

  private void optimizeScanLoops(int programSize) {
    int OP;
    for (int tmpIP = 0; tmpIP < programSize; tmpIP++) {
      OP = memory[tmpIP];
      if (OP == I_LOOP_START) {
        if (loopEndsCache[tmpIP] == -1) {
          // loop optimization is needed
          break;
        }
        int loopEnd = loopEndsCache[tmpIP] - 1;
        int posVar[] = repeatRead(I_INC, I_DEC, tmpIP+1, loopEnd, 0);

        if (posVar[0] == loopEnd) {
          // we have scanloop
          CachedOperation scanLoop = new CachedOperation();
          scanLoop.nextIP = posVar[0] + 1;
          scanLoop.argument = posVar[1];
          scanLoop.operation = I_SCANLOOP;

          operationsCache[tmpIP] = scanLoop;
          tmpIP = scanLoop.nextIP;
          scanLoopsCount++;
        }
      }
    }
  }



  public void run() throws IOException {
    int OP;

    while (IP < MEM_SIZE && memory[IP] != 0) {
      int argument = 1;
      CachedOperation operation = operationsCache[IP];
      if (operation != null) {
        OP = operation.operation;
        IP = operation.nextIP;
        argument = operation.argument;
      } else {
        OP = memory[IP++];
      }

      // DECODE
      switch (OP) {
        case I_STOP: /* ; */
          return;
        case I_INC: /* >  */
          P += argument;
          break;
        case I_DEC: /* < */
          P -= argument;
          break;
        case I_INCV: /* + */
          memory[P] = memory[P] + argument;
          break;
        case I_DECV: /* - */
          memory[P] = memory[P] - argument;
          break;
        case I_PRINT: /* . */
          while (argument > 0) {
            System.out.print((char)(memory[P] & 0xFF));
            argument--;
          }
          break;
        case I_READ: /* , */
          while (argument > 0) {
            memory[P] = System.in.read();
            argument--;
          }
          break;
        case I_LOOP_START: /* [ */
          int startingBrace = IP - 1;
          if (memory[P] != 0) {
            loopPointers.push(startingBrace);
            break;
          }
          IP = loopEndsCache[startingBrace];
          break;
        case I_LOOP_END: /* ] */
          int tmpIP = loopPointers.pop();
          if (memory[P] != 0) {
            IP = tmpIP;
          }
          break;
        case I_COPY_AND_CLEAR: // [>+<-] or [>-<-] or [<+>-] or [<->-] or [-] or combinations
          for (CopyLoop copyLoop : operation.copyLoops) {
            if (copyLoop.specialOP == I_PRINT) {
              System.out.print(String.valueOf(memory[P]));
            } else if (copyLoop.specialOP == I_READ) {
              memory[P] = System.in.read();
            } else {
              memory[P + copyLoop.relativePosition] = memory[P] * copyLoop.factor + memory[P + copyLoop.relativePosition];
            }
          }
          memory[P] = 0;
          break;
        case I_SCANLOOP: // [<] or [>] or combinations
          for (; memory[P] != 0; P += operation.argument) ;
          break;
        default: /* invalid instruction */
          ; // ignore
      }
    }
  }

  public static void main(String[] args) throws IOException {
    BF bf = new BF();

    int i = 0;
    int c;
    while ((c = System.in.read()) != -1) {
      if (c != I_STOP
          && c != I_INC
          && c != I_DEC
          && c != I_INCV
          && c != I_DECV
          && c != I_PRINT
          && c != I_READ
          && c != I_LOOP_END
          && c != I_LOOP_START
          ) {
        continue;
      }

      bf.memory[i++] = c;
    }

    bf.profileAndOptimize(i);

    bf.P = i;
    bf.IP = 0;

    bf.run();
  }

}
