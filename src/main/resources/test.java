/*
 * Copyright 2019 Khang Hoang Nguyen
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
/**
 * <p>The <code>Base2Util</code> class is a final class that provides
 * static methods for converting base 2 numbering system values in
 * string representation to a Java's Primitive Data Type.
 *
 * <p>Currently this class supports converting base 2 numbers values
 * in string representation to integer int values and integer
 * long values.
 *
 * <p>This class can parse unsigned base 2 numbers to a supported
 * integer signed type as if the integer type is unsigned. However,
 * some of the values must be interprete properly to get the correct
 * result.
 *
 * <p>Example for interpreting signed value as unsigned value.
 *
 * <p>It is possible to store the value of 18446744073709551615L
 * into a long(signed) value. However, if that value is stored into a
 * signed long integer type and if we were to interprete the value
 * normally, we would get a -1L value. However, if the -1L value is
 * pass to LongUtil.toStringAsUnsigned, we would get
 * 18446744073709551615 in string format.
 *
 * <p>The following example is to get to -1L. First, we assign a value
 * of 9223372036854775807L to an interger long variable, multiply that
 * variable to 2L, and add 1L to it.
 * <pre>
 *     long a = 9223372036854775807L * 2L + 1L;
 *     System.out.println(a);
 *     System.out.println(LongUtil.toStringAsUnsigned(a));
 * </pre>
 *
 * <p>Example methods for interprete signed type as unsigned type
 * in a decimal strings value are
 * {@link IntUtil#toStringAsUnsigned(int) IntUtil.toStringAsUnsigned}
 * and {@link LongUtil#toStringAsUnsigned(long) LongUtil.toStringAsUnsigned}.
 * </p>
 *
 * @author  Khang Hoang Nguyen
 *
 * @since  1.0.0.f
 **/
public final class Base2Util {
    private Base2Util() {
    }
    
    ;
    
    /**
     * Parse the input string as signed base 2 digits representation into an integer int value.
     *
     * @param input A string to be parsed as signed base 2 number to an integer int value.
     *
     * @return An integer int value of the signed base 2 number {@code input} string.
     * @throws NumberFormatException If the {@code input} string contains invalid signed base 2 digits, if the {@code input} string contains a value that is smaller than the value of
     *                               Integer.MIN_VALUE( {@value java.lang.Integer#MIN_VALUE}), or if the {@code input} string contains a value that is larger than the value of Integer.MAX_VALUE(
     *                               {@value java.lang.Integer#MAX_VALUE}).
     * @throws EmptyStringException  If the {@code input} string is empty.
     * @since 1.0.0.f
     **/
    public static final int toInt(final String input) {
        final int length = input.length();
        if (length == 0) throw new EmptyStringException();
        final char ch1 = input.charAt(0);
        int start;
    
        if (ch1 == '-' || ch1 == '+') {
            if (length == 1) throw new NumberFormatException(input);
            start = 1;
        } else {
            start = 0;
        }
    
        int out = 0, c;
        while (start < length && input.charAt(start) == '0') start++;
        final int runlen = length - start;
    
        if (runlen > 31) {
            if (runlen > 32) throw new NumberFormatException(input);
            if (ch1 != '-') throw new NumberFormatException(input);
        
            if (input.charAt(start++) != '1') throw new NumberFormatException(input);
        
            for (; start < length; start++) {
                if (input.charAt(start) != '0') throw new NumberFormatException(input);
            }
        
            return -2147483648;
        }
    
        for (; start < length; start++) {
            c = (input.charAt(start) ^ '0');
            if (c > 1) throw new NumberFormatException(input);
            out = (out << 1) | c;
        }
    
        if (ch1 == '-') return ~out + 1;
        return out;
    }
    
    /**
     * Parse the input string as unsigned base 2 number representation into an integer int value as if the integer int is an unsigned type. For values that need to be interpreted correctly, see the
     * {@link IntUtil#toStringAsUnsigned(int) toStringAsUnsigned} method of the {@link IntUtil IntUtil} class.
     *
     * @param input A string to be parsed as unsigned base 2 number to an integer int value as if the integer int is an unsigned type.
     *
     * @return An int value that represents an unsigned integer int value of the unsigned base 2 number {@code input} string.
     * @throws NumberFormatException If the {@code input} string contains invalid unsigned base 2 digits, if the {@code input} string contains a value that is beyond the capacity of the integer int
     *                               data type.
     * @throws EmptyStringException  If the {@code input} string is empty.
     * @since 1.0.0.f
     **/
    public static final int toIntAsUnsigned(final String input) {
        final int length = input.length();
        if (length == 0) throw new EmptyStringException();
        int start = 0;
    
        int out = 0, c;
        while (start < length && input.charAt(start) == '0') start++;
        if (length - start > 32) throw new NumberFormatException(input);
    
        for (; start < length; start++) {
            c = (input.charAt(start) ^ '0');
            if (c > 1) throw new NumberFormatException(input);
            out = (out << 1) | c;
        }
    
        return out;
    }
    
    /**
     * Parse the input string as signed base 2 number representation into an integer long value.
     *
     * @param input A string to be parsed as signed base 2 number to an integer long value.
     *
     * @return An integer long value of the signed base 2 number {@code input} string.
     * @throws NumberFormatException If the {@code input} string contains invalid signed base 2 digits, if the {@code input} string contains a value that is smaller than the value of Long.MIN_VALUE(
     *                               {@value java.lang.Long#MIN_VALUE}), or if the {@code input} string contains a value that is larger than the value of Long.MAX_VALUE( {@value
     *                               java.lang.Long#MAX_VALUE}).
     * @throws EmptyStringException  If the {@code input} string is empty.
     * @since 1.0.0.f
     **/
    public static final long toLong(final String input) {
        final int length = input.length();
        if (length == 0) throw new EmptyStringException();
        final char ch1 = input.charAt(0);
        int start = 0;
    
        if (ch1 == '-' || ch1 == '+') {
            if (length == 1) throw new NumberFormatException(input);
            start = 1;
        }
    
        long out = 0, c;
        while (start < length && input.charAt(start) == '0') start++;
        final int runlen = length - start;
    
        if (runlen > 63) {
            if (runlen > 64) throw new NumberFormatException(input);
            if (ch1 != '-') throw new NumberFormatException(input);
        
            if (input.charAt(start++) != '1') throw new NumberFormatException(input);
        
            for (; start < length; start++) {
                if (input.charAt(start) != '0') throw new NumberFormatException(input);
            }
        
            return -9223372036854775808L;
        }
    
        for (; start < length; start++) {
            c = (input.charAt(start) ^ '0');
            if (c > 1L) throw new NumberFormatException(input);
            out = (out << 1) | c;
        }
    
        if (ch1 == '-') return ~out + 1L;
        return out;
    }
    
    /**
     * Parse the input string as unsigned base 2 number representation into an integer long value as if the integer long is an unsigned type. For values that need to be interpreted correctly, see the
     * {@link LongUtil#toStringAsUnsigned(long) toStringAsUnsigned} method of the {@link LongUtil LongUtil} class.
     *
     * @param input A string to be parsed as unsigned base 2 number to an integer long value as if the integer long is an unsigned type.
     *
     * @return An integer long value represent the unsigned integer long value of the unsigned base 2 number {@code input} string.
     * @throws NumberFormatException If the {@code input} string contains invalid unsigned base 2 digits, or if the {code input} string contains a value that is beyond the capacity of the long data
     *                               type.
     * @since 1.0.0.f
     **/
    public static final long toLongAsUnsigned(final String input) {
        final int length = input.length();
        if (length == 0) throw new EmptyStringException();
        int start = 0;
    
        long out = 0, c;
        while (start < length && input.charAt(start) == '0') start++;
        if (length - start > 64) throw new NumberFormatException(input);
    
        for (; start < length; start++) {
            c = (input.charAt(start) ^ '0');
            if (c > 1L) throw new NumberFormatException(input);
            out = (out << 1) | c;
        }
    
        return out;
    }
}