package enigma;

/** Class that represents a rotor in the enigma machine.
 *  @author Brian Truong
 */

class Rotor {

    /** Size of alphabet used for plaintext and ciphertext. */
    private static final int ALPHABET_SIZE = 26;

    /** All the letters of the alphabet used for finding an index
     *  of a char based on the alphabet and to find a char at a
     * particular index. */
    private static final String ALPHABET_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** Index number of all rotors found in ROTOR_SPECS used for checking
     *  over all twelves available array of rotors. */
    private static final int INDEX_ROTOR = 11;

    /** Static variable that allows access to ROTOR_SPECS in
     *  PermutationData. 2D Array with information about each type of
     *  avaiable rotor to choose from. */
    private static String[][] blueprint = PermutationData.ROTOR_SPECS;

    /** Current setting of THIS current rotor as an integer with respect to
     *  the alphabet. */
    private int _setting;

    /** Instance variable that pulls a single array line from ROTOR_SPECS
     *  and contains all specifications of THIS rotor. */
    private String[] rotorinfo;

    /** Rotor constructor that takes in a ROTORNUMERAL which contains the
     *  name of a valid rotor that can be found in ROTOR_SPECS. In order to
     *  check if ROTORNUMERAL is a valid rotor, rotor will iterate through the
     *  zeroth element of each array line in ROTOR_SPECS. If it finds a match,
     *  that array line will be set to instance variable ROTORINFO, else the
     *  ROTORNUMERAL entered must have been invalid and a error message will
     *  be printed followed by an exit with code 1. */
    Rotor(String rotornumeral) {
        int count = 0;
        while (count <= INDEX_ROTOR) {
            if (blueprint[count][0].equals(rotornumeral)) {
                rotorinfo = blueprint[count];
                break;
            } else {
                count += 1;
            }
        }
        if (rotorinfo == null) {
            System.err.println("Rotor might be misnamed.");
            System.exit(1);
        }
    }

    /** Assuming that P is an integer in the range 0..25, returns the
     *  corresponding upper-case letter in the range A..Z. */
    static char toLetter(int p) {
        char result = ALPHABET_LETTERS.charAt(p);
        return result;
    }

    /** Assuming that C is an upper-case letter in the range A-Z, return the
     *  corresponding index in the range 0..25. Inverse of toLetter. */
    static int toIndex(char c) {
        int result = ALPHABET_LETTERS.indexOf(c);
        return result;
    }

    /** Returns true iff this rotor has a ratchet and can advance. */
    boolean advances() {
        return rotorinfo.length == 4;
    }

    /** Returns true iff this rotor has a left-to-right inverse. */
    boolean hasInverse() {
        return rotorinfo.length > 2;
    }

    /** Return my current rotational _SETTING as an integer between 0
     *  and 25 (corresponding to letters 'A' to 'Z').  */
    int getSetting() {
        return _setting;
    }

    /** Returns my current ROTORINFO as an array of strings containing
     *  THIS particular rotor specifications from ROTOR_SPEC. */
    String[] getInfo() {
        return rotorinfo;
    }

    /** Set getSetting() to POSN.  */
    void set(int posn) {
        assert 0 <= posn && posn < ALPHABET_SIZE;
        _setting = posn;
    }

    /** Return the conversion of P (an integer in the range 0..25)
     *  according to my permutation. */
    int convertForward(int p) {
        int enter = (_setting + p) % ALPHABET_SIZE;
        char tmp = rotorinfo[1].charAt(enter);
        int result = toIndex(tmp);
        int fin = result - _setting;
        if (fin < 0) {
            fin += ALPHABET_SIZE;
        }
        return fin % ALPHABET_SIZE;
    }

    /** Return the conversion of E (an integer in the range 0..25)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int enter = (_setting + e) % ALPHABET_SIZE;
        char tmp = rotorinfo[2].charAt(enter);
        int result = toIndex(tmp);
        int fin = result - _setting;
        if (fin < 0) {
            fin += ALPHABET_SIZE;
        }
        return fin % ALPHABET_SIZE;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        if (this.advances()) {
            if (toIndex(rotorinfo[3].charAt(0)) == _setting) {
                return true;
            } else if (rotorinfo[3].length() == 2) {
                return toIndex(rotorinfo[3].charAt(1)) == _setting;
            }
        }
        return false;
    }

    /** Advance me one position. */
    void advance() {
        _setting = (_setting + 1) % ALPHABET_SIZE;
    }
}
