package com.imjcker.gateway.util;

public class TypeValidateUtil {

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}}

	public static boolean isLong(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (Exception e) {
			return false;
		}}

	public static boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
			return true;
		} catch (Exception e) {
			return false;
		}}

	public static boolean isDoublle(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}}

	public static boolean isBoolean(String s) {
		return "TRUE".equals(s.toUpperCase()) || "FALSE".equals(s.toUpperCase());
	}

}
