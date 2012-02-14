/* --- Copyright Jonathan Meyer 1997. All rights reserved. -----------------
 > File:        jasmin/src/jasmin/ScannerUtils.java
 > Purpose:     Various static methods utilized to breakdown strings
 > Author:      Jonathan Meyer, 8 Feb 1997
 */

package jasmin;

abstract class ScannerUtils {

    //
    // Converts a string to a number (int, float, long, or double).
    // (uses smallest format that will hold the number)
    //
    public static Number convertNumber(String str) throws NumberFormatException {
        str = str.toUpperCase();
        if (str.startsWith("0X")) {// hex
            switch (str.charAt(str.length() - 1)) {
            case 'D':
                return Double.longBitsToDouble(Long.parseLong(str.substring(2, str.length() - 1), 16));
            case 'L':
                return Long.parseLong(str.substring(2, str.length() - 1), 16);
            case 'F':
                return Float.intBitsToFloat(Integer.parseInt(str.substring(2, str.length() - 1), 16));
            default:
                return Integer.parseInt(str.substring(2), 16);
            }
        } else {
            switch (str.charAt(str.length() - 1)) {
            case 'D':
                return Double.parseDouble(str.substring(0, str.length() - 1));
            case 'L':
                return Long.parseLong(str.substring(0, str.length() - 1));
            case 'F':
                return Float.parseFloat(str.substring(0, str.length() - 1));
            default:
                if (str.indexOf('.') >= 0) {
                    return Double.parseDouble(str);
                }
                return Integer.parseInt(str);
            }
        }
    }

    //
    // Maps '.' characters to '/' characters in a string
    //
    public static String convertDots(String orig_name)
    {
        return convertChars(orig_name, ".", '/');
    }

    //
    // Maps chars to toChar in a given String
    //
    public static String convertChars(String orig_name,
                                      String chars, char toChar)
    {
        StringBuffer tmp = new StringBuffer(orig_name);
        int i;
        for (i = 0; i < tmp.length(); i++) {
            if (chars.indexOf(tmp.charAt(i)) != -1) {
                tmp.setCharAt(i, toChar);
            }
        }
        return new String(tmp);
    }

    //
    // Splits a string like:
    //     "a/b/c/d(xyz)v"
    // into three strings:
    //     "a/b/c", "d", "(xyz)v"
    //
    public static String[] splitClassMethodSignature(String name) {
        String result[] = new String[3];
        int i, pos = 0, sigpos = 0;
        for (i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '.' || c == '/') pos = i;
            else if (c == '(') {sigpos = i; break; }
        }
        try {
            result[0] = convertDots(name.substring(0, pos));
            result[1] = name.substring(pos + 1, sigpos);
            result[2] = convertDots(name.substring(sigpos));
        } catch(StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("malformed signature : "+name);
        }
        return result;
    }

    //
    // Splits a string like:
    //    "java/lang/System/out"
    // into two strings:
    //    "java/lang/System" and "out"
    //
    public static String[] splitClassField(String name)
    {
        String result[] = new String[2];
        int i, pos = -1, sigpos = 0;
        for (i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '.' || c == '/') pos = i;
        }
        if (pos == -1) {    // no '/' in string
            result[0] = null;
            result[1] = name;
        } else {
            result[0] = convertDots(name.substring(0, pos));
            result[1] = name.substring(pos + 1);
        }

        return result;
    }

    // Splits a string like:
    //      "main(Ljava/lang/String;)V
    // into two strings:
    //      "main" and "(Ljava/lang/String;)V"
    //
    public static String[] splitMethodSignature(String name)
    {
        String result[] = new String[2];
        int i, sigpos = 0;
        for (i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '(') {sigpos = i; break; }
        }
        result[0] = name.substring(0, sigpos);
        result[1] = convertDots(name.substring(sigpos));

        return result;
    }
}

/* --- Revision History ---------------------------------------------------
--- Panxiaobo, Feb 14 2012
    'D'/'F'/'L' in real constant, force double/float/long mode.
--- Iouri Kharon, May 07 2010
    'd' in real constant, force double mode.
*/
