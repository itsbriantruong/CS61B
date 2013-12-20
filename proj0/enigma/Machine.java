package enigma;

/** Class that represents a complete enigma machine.
 *  @author Brian Truong
 */
class Machine {
    /** Instance variable ALLROTORS is an array of all the rotors mentioned in
     *  the CONFIGURATION input in the machine constructor. */
    private Rotor[] allrotors;

    /** Instance variable INDIVIDUAL splits the original CONFIGURATION line,
     *  by removing all white spaces, and creates an array containing strings
     *  of all the rotors and even the last element of CONFIGURATION,
     *  which is the initial position string. */
    private String[] individual;

    /** Instance variable RSETTINGS takes the last element in INDIVIDUAL,
     *  which is the initial position string from CONFIGURATION. */
    private String rsettings;

    /** Machine constructor that takes in a CONFIGURATION line beginning with
     *  an asterisk (*) and containing six elements consisting
     *  (from left to right) of one reflector, one non-advancing rotor, three
     *  rotors that can advance and a string of four upper-case letters
     *  (rotor settings). Separates the rotors/reflector and the rotor
     *  settings by constructing instance variables that take in the first
     *  five elements and the last element respectively. */
    Machine(String configuration) {
        individual = ((configuration.substring(1)).trim()).split("\\s+");
        rsettings = individual[5];
    }

    /** Creates an array of rotors (from left to right) according to
     *  the four rotors and one reflector specified in configuration
     *  passed through Machine constructor and sets them all up at 'A'. */
    void replaceRotors() {
        allrotors = new Rotor[5];
        int count = 0;
        while (count < 5) {
            allrotors[count] = new Rotor(individual[count]);
            count += 1;
        }
        for (Rotor element : allrotors) {
            element.set(0);
        }
        if (allrotors[0].hasInverse()) {
            System.err.println("Improper rotor in reflector position.");
            System.exit(1);
        }
        if (allrotors[1].advances() || !allrotors[1].hasInverse()) {
            System.err.println("Improper rotor in fixedrotor position.");
            System.exit(1);
        }
        for (int i = 2; i < 5; i += 1) {
            if (!allrotors[i].advances()) {
                System.err.println("Improper rotor: advancing rotor position");
                System.exit(1);
            }
        }
    }

    /** Set my rotors according to instance variable RSETTING, which
     *  must be a string of four upper-case letters coming from the
     *  last element of configuration that was passed through Machine
     *  constructor. The first letter refers to the leftmost rotor setting. */
    void setRotors() {
        int element = 0;
        for (int i = 1; i < 5; i += 1) {
            allrotors[i].set(Rotor.toIndex(rsettings.charAt(element)));
            element += 1;
        }
    }

    /** Advance necessary rotors by one. Rotors positioned to allow
     * rotors to the left to advance will be checked if they are
     * currently on a notch and will advance necessary rotors if true. */
    void changePosition() {
        if (allrotors[3].atNotch()) {
            allrotors[2].advance();
            allrotors[3].advance();
        } else if (allrotors[4].atNotch()) {
            allrotors[3].advance();
        }
        allrotors[4].advance();
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly by calling upon changePosition on THIS. */
    String convert(String msg) {
        String result = "";
        for (int e = 0; e < msg.length(); e += 1) {
            int initial = Rotor.toIndex(msg.charAt(e));
            this.changePosition();
            for (int i = 4; i >= 0; i -= 1) {
                initial = allrotors[i].convertForward(initial);
            }
            for (int k = 1; k <= 4; k += 1) {
                initial = allrotors[k].convertBackward(initial);
            }
            result += Rotor.toLetter(initial);
        }
        return result;
    }
}
