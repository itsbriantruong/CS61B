package enigma;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashSet;

/** Enigma simulator.
 *  @author Brian Truong
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified in the input from the standard input.  Print the
     *  results on the standard output. Exits normally if there are
     *  no errors in the input; otherwise with code 1. */
    public static void main(String[] unused) {
        Machine M;
        BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));

        M = null;

        try {
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                if (isConfigurationLine(line)) {
                    M = new Machine(line);
                    M.replaceRotors();
                    M.setRotors();
                } else if (M == null) {
                    System.err.println("Must start with proper configuration.");
                    System.exit(1);
                } else {
                    printMessageLine(M.convert(standardize(line)));
                }
            }
        } catch (IOException excp) {
            System.err.printf("Input error: %s%n", excp.getMessage());
            System.exit(1);
        }
    }

    /** Function that takes in a string ROTORIN and compares it
     *  to each valid rotor in ROTOR_SPECS. If ROTORIN is true
     *  for some rotor, return that rotor integer number, else
     *  return -1 (signaling the string ROTORIN is not a valid rotor). */
    private static int rotorCheck(String rotorin) {
        if (rotorin.equals("I")) {
            return 1;
        } else if (rotorin.equals("II")) {
            return 2;
        } else if (rotorin.equals("III")) {
            return 3;
        } else if (rotorin.equals("IV")) {
            return 4;
        } else if (rotorin.equals("V")) {
            return 5;
        } else if (rotorin.equals("VI")) {
            return 6;
        } else if (rotorin.equals("VII")) {
            return 7;
        } else if (rotorin.equals("VIII")) {
            return 8;
        } else {
            return -1;
        }
    }

    /** Return true iff LINE is an Enigma configuration line. */
    private static boolean isConfigurationLine(String line) {
        if (!line.equals("")) {
            if (line.charAt(0) == '*') {
                String[] inputs = ((line.substring(1)).trim()).split("\\s+");
                if (inputs.length == 6) {
                    if (inputs[5].length() != 4) {
                        System.err.println("Position string: wrong length.");
                        System.exit(1);
                    }
                    for (int i = 0; i < 4; i += 1) {
                        if (!Character.isLetter(inputs[5].charAt(i))) {
                            System.err.println("Position string invalid char.");
                            System.exit(1);
                        }
                    }
                }
                HashSet<String> noduplicates = new HashSet<String>();
                if (inputs.length != 6) {
                    System.err.println("Config line: wrong number arguments.");
                    System.exit(1);
                }
                for (String element : inputs) {
                    noduplicates.add(element);
                }
                if (noduplicates.size() != 6) {
                    System.err.println("Rotor may be repeated in config line.");
                    System.exit(1);
                }
                if (!inputs[0].equals("C") && !inputs[0].equals("B")) {
                    return false;
                }
                if (!inputs[1].equals("BETA") && !inputs[1].equals("GAMMA")) {
                    return false;
                }
                for (int k = 2; k < 5; k += 1) {
                    if (rotorCheck(inputs[k]) == -1) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    /** Return the result of converting LINE to all upper case,
     *  removing all blanks and tabs.  It is an error if LINE contains
     *  characters other than letters and blanks. */
    private static String standardize(String line) {
        String result = "";
        int k = 0;
        while (k < line.length()) {
            if (Character.isLetter(line.charAt(k))) {
                result += line.charAt(k);
            } else if (!Character.isWhitespace(line.charAt(k))
                && !Character.isLetter(line.charAt(k))) {
                System.err.println("Line contains invalid characters.");
                System.exit(1);
            }
            k += 1;
        }
        return result.toUpperCase();
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private static void printMessageLine(String msg) {
        String result = "";
        int i = 0;
        while (i < msg.length()) {
            result += msg.charAt(i);
            if ((i + 1) % 5 == 0) {
                result += " ";
            }
            i += 1;
        }
        System.out.println(result);
    }
}

