/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thanhdatbk.gsm7bitconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author dat.dinh-thanh
 */
@SuppressWarnings("empty-statement")
public class GSM7BitConverter {

    private static Map<Character, Integer> _charToGsmMap;
    private static Map<Integer, Character> _gsmToCharMap;
    private static ArrayList<Character> _extensionChars;
    private static int _extendedChar = 0x1b;

    public static char getChar(int code) {
        if (_gsmToCharMap.containsKey(code)) {
            return _gsmToCharMap.get(code);
        }
        return ' ';
    }

    public static int getGsm(char c) {
        if (_charToGsmMap.containsKey(c)) {
            return _charToGsmMap.get(c);
        }
        return 0x20;
    }

    public static byte getEndByteOfChar(char c) {
        int code = getGsm(c);
        byte ret = (byte) (code & 0xff);
        return ret;
    }

    public static boolean isExtensionChar(char c) {
        return _extensionChars.contains(c);
    }

    public static Byte[] stringToGsm7Bit(String msg) {
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        for (Character c : msg.toCharArray()) {
            if (isExtensionChar(c)) {
                bytes.add((byte) _extendedChar);
            }
            bytes.add(getEndByteOfChar(c));
        }
        return bytes.toArray(new Byte[bytes.size()]);
    }

    public static String stringToGsm7BitHex(String msg) {
        Byte[] bytes = stringToGsm7Bit(msg);
        return byteArrayToHexString(bytes);
    }

    public static Byte[] stringToGsm7BitPack(String msg) {
        Byte[] gsmBytes = stringToGsm7Bit(msg);
        Byte[] answer = packByteArray(gsmBytes);
        return answer;
    }

    public static String stringToGsm7BitPackHex(String msg) {
        Byte[] bytes = stringToGsm7BitPack(msg);
        return byteArrayToHexString(bytes);
    }

    public static String gsm7BitToString(Byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            Byte b = bytes[i];
            if ((int) b == _extendedChar) {
                Byte n_b = bytes[i + 1];
                int code = (b << 8) | n_b;
                sb.append(getChar(code));
                i++;
            } else {
                sb.append(getChar(b));
            }
        }
        return sb.toString();
    }

    public static String gsm7BitHexToString(String hex) {
        return gsm7BitToString(hexStringToByteArray(hex));
    }

    public static String gsm7BitPackToString(Byte[] bytes) {
        Byte[] unpackBytes = unpackByteArray(bytes);
        return gsm7BitToString(unpackBytes);
    }

    public static String gsm7BitPackHexToString(String hex) {
        return gsm7BitPackToString(hexStringToByteArray(hex));
    }

    public static Byte[] packByteArray(Byte[] gsmBytes) {
        ArrayList<Byte> answer = new ArrayList<>();
        if (gsmBytes.length == 1) {
            answer.add(gsmBytes[0]);
        } else {
            for (int i = 0; i < gsmBytes.length; i++) {
                Byte b = gsmBytes[i];
                Byte n_b = (byte) 0;
                if (i + 1 != gsmBytes.length) {
                    n_b = gsmBytes[i + 1];
                }

                int r_shift = i % 8;
                int l_shift = 8 - r_shift - 1;

                if (r_shift == 7) {
                    continue;
                }

                int r_byte = b >> r_shift;
                int l_byte = n_b << l_shift;
                byte b_result = (byte) (r_byte | l_byte);
                answer.add(b_result);
            }
        }
        return answer.toArray(new Byte[answer.size()]);
    }

    public static Byte[] unpackByteArray(Byte[] bytes) {
        ArrayList<Byte> unpackBytes = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            Byte b = bytes[i];
            Byte p_b = (byte) 0;

            int l_shift = i % 8;
            int r_shift = 8 - l_shift;
            int rm_shift = 8 - r_shift + 1;

            if (l_shift != 0) {
                p_b = bytes[i - 1];
            }

            byte r_b = (byte) (b << rm_shift);
            byte rm_b = (byte) (r_b >> rm_shift << l_shift);
            int o_bit = p_b >> r_shift;
            int result_b = rm_b | o_bit;
            unpackBytes.add((byte) result_b);

            if (l_shift == 6) {
                int end_b = b >> 1;
                if (end_b != 0) {
                    unpackBytes.add((byte) end_b);
                }
            }
        }

        return unpackBytes.toArray(new Byte[unpackBytes.size()]);
    }

    public static String byteArrayToHexString(Byte[] ba) {
        StringBuilder hex = new StringBuilder(ba.length * 2);
        for (byte b : ba) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    public static Byte[] hexStringToByteArray(String s) {
        int len = s.length();
        Byte[] data = new Byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    static {
        _charToGsmMap = Stream.of(new Object[][]{
            {'@', 0x0},
            {'£', 0x1},
            {'$', 0x2},
            {'¥', 0x3},
            {'è', 0x4},
            {'é', 0x5},
            {'ù', 0x6},
            {'ì', 0x7},
            {'ò', 0x8},
            {'Ç', 0x9},
            {'\n', 0x0A},
            {'Ø', 0x0B},
            {'ø', 0x0C},
            {'\r', 0x0D},
            {'Å', 0x0E},
            {'å', 0x0F},
            {'Δ', 0x10},
            {'_', 0x11},
            {'Φ', 0x12},
            {'Γ', 0x13},
            {'Λ', 0x14},
            {'Ω', 0x15},
            {'Π', 0x16},
            {'Ψ', 0x17},
            {'Σ', 0x18},
            {'Θ', 0x19},
            {'Ξ', 0x1A},
            //            {'\u0027',    0x1B},
            {'\u000C', 0x1B0A},
            {'^', 0x1B14},
            {'{', 0x1B28},
            {'}', 0x1B29},
            {'\\', 0x1B2F},
            {'[', 0x1B3C},
            {'~', 0x1B3D},
            {']', 0x1B3E},
            {'|', 0x1B40},
            {'€', 0x1B65},
            {'Æ', 0x1C},
            {'æ', 0x1D},
            {'ß', 0x1E},
            {'É', 0x1F},
            {' ', 0x20},
            {'!', 0x21},
            {'"', 0x22},
            {'#', 0x23},
            {'¤', 0x24},
            {'%', 0x25},
            {'&', 0x26},
            {'\'', 0x27},
            {'(', 0x28},
            {')', 0x29},
            {'*', 0x2A},
            {'+', 0x2B},
            {',', 0x2C},
            {'-', 0x2D},
            {'.', 0x2E},
            {'/', 0x2F},
            {'0', 0x30},
            {'1', 0x31},
            {'2', 0x32},
            {'3', 0x33},
            {'4', 0x34},
            {'5', 0x35},
            {'6', 0x36},
            {'7', 0x37},
            {'8', 0x38},
            {'9', 0x39},
            {':', 0x3A},
            {';', 0x3B},
            {'<', 0x3C},
            {'=', 0x3D},
            {'>', 0x3E},
            {'?', 0x3F},
            {'¡', 0x40},
            {'A', 0x41},
            {'B', 0x42},
            {'C', 0x43},
            {'D', 0x44},
            {'E', 0x45},
            {'F', 0x46},
            {'G', 0x47},
            {'H', 0x48},
            {'I', 0x49},
            {'J', 0x4A},
            {'K', 0x4B},
            {'L', 0x4C},
            {'M', 0x4D},
            {'N', 0x4E},
            {'O', 0x4F},
            {'P', 0x50},
            {'Q', 0x51},
            {'R', 0x52},
            {'S', 0x53},
            {'T', 0x54},
            {'U', 0x55},
            {'V', 0x56},
            {'W', 0x57},
            {'X', 0x58},
            {'Y', 0x59},
            {'Z', 0x5A},
            {'Ä', 0x5B},
            {'Ö', 0x5C},
            {'Ñ', 0x5D},
            {'Ü', 0x5E},
            {'§', 0x5F},
            {'¿', 0x60},
            {'a', 0x61},
            {'b', 0x62},
            {'c', 0x63},
            {'d', 0x64},
            {'e', 0x65},
            {'f', 0x66},
            {'g', 0x67},
            {'h', 0x68},
            {'i', 0x69},
            {'j', 0x6A},
            {'k', 0x6B},
            {'l', 0x6C},
            {'m', 0x6D},
            {'n', 0x6E},
            {'o', 0x6F},
            {'p', 0x70},
            {'q', 0x71},
            {'r', 0x72},
            {'s', 0x73},
            {'t', 0x74},
            {'u', 0x75},
            {'v', 0x76},
            {'w', 0x77},
            {'x', 0x78},
            {'y', 0x79},
            {'z', 0x7A},
            {'ä', 0x7B},
            {'ö', 0x7C},
            {'ñ', 0x7D},
            {'ü', 0x7E},
            {'à', 0x7F},}).collect(Collectors.toMap(data -> (Character) data[0], data -> (Integer) data[1]));

        _extensionChars = new ArrayList<>(Arrays.asList('\u000C', '^', '{', '}', '\\', '[', '~', ']', '|', '€'));

        _gsmToCharMap = new HashMap<>();
        for (Map.Entry<Character, Integer> entrySet : _charToGsmMap.entrySet()) {
            Character key = entrySet.getKey();
            Integer value = entrySet.getValue();
            _gsmToCharMap.put(value, key);
        }
    }
}
