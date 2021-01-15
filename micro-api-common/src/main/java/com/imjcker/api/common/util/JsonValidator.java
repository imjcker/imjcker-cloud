package com.imjcker.api.common.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * @Title
 * @Description
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月17日 下午5:45:28
 */
public class JsonValidator {

    private static CharacterIterator it;
    private static char              c;
    private static int               col;

    /**
     * 验证一个字符串是否是合法的JSON串
     *
     * @param input 要验证的字符串
     * @return true-合法 ，false-非法
     */
    public final static boolean validate(String input) {
        input = input.trim();
        boolean ret = valid(input);
        return ret;
    }

    private final static boolean valid(String input) {
        if ("".equals(input)) return true;

        boolean ret = true;
        it = new StringCharacterIterator(input);
        c = it.first();
        col = 1;
        if (!value()) {
            ret = error("value", 1);
        } else {
            skipWhiteSpace();
            if (c != CharacterIterator.DONE) {
                ret = error("end", col);
            }
        }

        return ret;
    }

    private final static boolean value() {
        return literal("true") || literal("false") || literal("null") || string() || number() || object() || array();
    }

    private final static boolean literal(String text) {
        CharacterIterator ci = new StringCharacterIterator(text);
        char t = ci.first();
        if (c != t) return false;

        int start = col;
        boolean ret = true;
        for (t = ci.next(); t != CharacterIterator.DONE; t = ci.next()) {
            if (t != nextCharacter()) {
                ret = false;
                break;
            }
        }
        nextCharacter();
        if (!ret) error("literal " + text, start);
        return ret;
    }

    private final static boolean array() {
        return aggregate('[', ']', false);
    }

    private final static boolean object() {
        return aggregate('{', '}', true);
    }

    private final static boolean aggregate(char entryCharacter, char exitCharacter, boolean prefix) {
        if (c != entryCharacter) return false;
        nextCharacter();
        skipWhiteSpace();
        if (c == exitCharacter) {
            nextCharacter();
            return true;
        }

        for (;;) {
            if (prefix) {
                int start = col;
                if (!string()) return error("string", start);
                skipWhiteSpace();
                if (c != ':') return error("colon", col);
                nextCharacter();
                skipWhiteSpace();
            }
            if (value()) {
                skipWhiteSpace();
                if (c == ',') {
                    nextCharacter();
                } else if (c == exitCharacter) {
                    break;
                } else {
                    return error("comma or " + exitCharacter, col);
                }
            } else {
                return error("value", col);
            }
            skipWhiteSpace();
        }

        nextCharacter();
        return true;
    }

    private final static boolean number() {
        if (!Character.isDigit(c) && c != '-') return false;
        int start = col;
        if (c == '-') nextCharacter();
        if (c == '0') {
            nextCharacter();
        } else if (Character.isDigit(c)) {
            while (Character.isDigit(c))
                nextCharacter();
        } else {
            return error("number", start);
        }
        if (c == '.') {
            nextCharacter();
            if (Character.isDigit(c)) {
                while (Character.isDigit(c))
                    nextCharacter();
            } else {
                return error("number", start);
            }
        }
        if (c == 'e' || c == 'E') {
            nextCharacter();
            if (c == '+' || c == '-') {
                nextCharacter();
            }
            if (Character.isDigit(c)) {
                while (Character.isDigit(c))
                    nextCharacter();
            } else {
                return error("number", start);
            }
        }
        return true;
    }

    private final static boolean string() {
        if (c != '"') return false;

        int start = col;
        boolean escaped = false;
        for (nextCharacter(); c != CharacterIterator.DONE; nextCharacter()) {
            if (!escaped && c == '\\') {
                escaped = true;
            } else if (escaped) {
                if (!escape()) {
                    return false;
                }
                escaped = false;
            } else if (c == '"') {
                nextCharacter();
                return true;
            }
        }
        return error("quoted string", start);
    }

    private final static boolean escape() {
        int start = col - 1;
        if (" \\\"/bfnrtu".indexOf(c) < 0) {
            return error("escape sequence  \\\",\\\\,\\/,\\b,\\f,\\n,\\r,\\t  or  \\uxxxx ", start);
        }
        if (c == 'u') {
            if (!ishex(nextCharacter()) || !ishex(nextCharacter()) || !ishex(nextCharacter())
                || !ishex(nextCharacter())) {
                return error("unicode escape sequence  \\uxxxx ", start);
            }
        }
        return true;
    }

    private final static boolean ishex(char d) {
        return "0123456789abcdefABCDEF".indexOf(c) >= 0;
    }

    private final static char nextCharacter() {
        c = it.next();
        ++col;
        return c;
    }

    private final static void skipWhiteSpace() {
        while (Character.isWhitespace(c)) {
            nextCharacter();
        }
    }

    private final static boolean error(String type, int col) {
         //System.out.printf("type: %s, col: %s%s", type, col, System.getProperty("line.separator"));
        return false;
    }
/*    public static void main(String[] args){
        String jsonStr = "{\"errorCode\": 200,\"errorMsg\": \"请求成功\",\"uid\": \"20170502142940611cQWhmEZbMokHVNS\",\"data\": {\"result\": 0,\"name\": \"邓汉海\", \"description\": \"The record not found\",\"id\": \"460026197603204813\",\"brand\": \"\"}}";
        String jsonStr2 = "{\n     \"errorCode\": 200,\n     \"errorMsg\": \"请求成功\",\n     \"uid\": \"20160810185932722FwJuvD2geitedbv\",\n     \"data\": {\n    \"result\": \"1\",\n    \"msg\": \"认证信息匹配\"\n  }\n}";
        String jsonStr3 = "{\"errorCode\":200,\"errorMsg\":\"请求成功\",\r\"uid\":\"20160810185932722FwJuvD2geitedbv\",\t         \"data\":{ \"result\":\"1\", \"msg\":\"认证信息匹配\" }}";
        String jsonStr4 = "{    \"errorCode\": 200,    \"errorMsg\": \"请求成功\",    \"uid\": \"20160810185932722FwJuvD2geitedbv\",\"data\": {        \"result\": \"1\",       \"msg\": \"认证信息匹配\"    }}";
        String jsonStr5 = "{     \"errorCode.\": 200,     \"errorMsg.\": \"请求成功\",     \"uid.\": \"20160810185932722FwJuvD2geitedbv\",     \"data\": {    \"result\": \"1\",    \"msg\": \"认证信息匹配\"  }}";
        System.out.println(jsonStr5+":"+JsonValidator.validate(jsonStr5));
    }*/
}
