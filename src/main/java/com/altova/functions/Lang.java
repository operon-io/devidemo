/**
 * Lang.java
 *
 * This file was generated by MapForce 2022r2.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the MapForce Documentation for further details.
 * http://www.altova.com/mapforce
 */

package com.altova.functions;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.altova.types.DateTime;
import com.altova.types.Duration;
import com.altova.types.NotANumberException;
import java.util.UUID;

public class Lang {
	// logical
	public static boolean logicalXor(boolean a, boolean b) { return a ^ b; }
	public static boolean positive(int a) { return a > 0; }
	public static boolean positive(long a) { return a > 0; }
	public static boolean positive(double a) { return a > 0; }
	public static boolean positive(BigInteger n) { return n.compareTo(BigInteger.ZERO) > 0; }
	public static boolean positive(BigDecimal n) { return n.compareTo(BigDecimal.ZERO) > 0; }
	public static boolean positive(Duration d) { return !d.isNegative(); }
	public static boolean negative(int a) { return a < 0; }
	public static boolean negative(long a) { return a < 0; }
	public static boolean negative(double a) { return a < 0; }
	public static boolean negative(BigInteger n) { return n.compareTo(BigInteger.ZERO) < 0; }
	public static boolean negative(BigDecimal n) { return n.compareTo(BigDecimal.ZERO) < 0; }
	public static boolean negative(Duration d) { return d.isNegative(); }
	public static boolean numeric(int a) { return true; }
	public static boolean numeric(long a) { return true; }
	public static boolean numeric(double a) { return true; }
	public static boolean numeric(BigInteger a) { return true; }
	public static boolean numeric(BigDecimal a) { return true; }
	public static boolean numeric(boolean b) { return false; }
	private static java.util.regex.Pattern numberPattern =
		java.util.regex.Pattern.compile("^\\s*(INF|-INF|NaN|[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)([Ee][+-]?[0-9]+)?)\\s*$");
	public static boolean numeric(String s) { return numberPattern.matcher(s).matches(); }
	public static boolean numeric(DateTime dt) { return false; }
	public static boolean numeric(Duration d) { return false; }

	// divide integer
	public static int divideInteger(int a, int b) { return a / b; }
	public static long divideInteger(long a, long b) { return a / b; }
	public static double divideInteger(double a, double b) { return Math.floor(a / b); }
	public static BigInteger divideInteger(BigInteger a, BigInteger b) { return a.divide(b); }
	public static BigDecimal divideInteger(BigDecimal a, BigDecimal b) { return a.divide(b, java.math.RoundingMode.FLOOR); }

	// UnaryMinus
	public static int unaryMinus(int a) { return -a; }
	public static long unaryMinus(long a) { return -a; }
	public static double unaryMinus(double a) { return -a; }
	public static BigInteger unaryMinus(BigInteger a) { return a.negate(); }
	public static BigDecimal unaryMinus(BigDecimal a) { return a.negate(); }
	public static Duration unaryMinus(Duration a) { Duration dur = new Duration(a); dur.setNegative( !a.isNegative() ); return dur; }

	// Max
	public static int max(int a, int b) { return a < b ? b : a; }
	public static long max(long a, long b) { return a < b ? b : a; }
	public static double max(double a, double b) { return a < b ? b : a; }
	public static BigInteger max(BigInteger a, BigInteger b) { return a.max(b); }
	public static BigDecimal max(BigDecimal a, BigDecimal b) { return a.max(b); }

	// Min
	public static int min(int a, int b) { return a < b ? a : b; }
	public static long min(long a, long b) { return a < b ? a : b; }
	public static double min(double a, double b) { return a < b ? a : b; }
	public static BigInteger min(BigInteger a, BigInteger b) { return a.min(b); }
	public static BigDecimal min(BigDecimal a, BigDecimal b) { return a.min(b); }

	// Pi
	public static double pi() { return Math.PI; }

	// Trig
	public static double sin(double num) { return Math.sin(num); }
	public static double cos(double num) { return Math.cos(num); }
	public static double tan(double num) { return Math.tan(num); }
	public static double asin(double num) { return Math.asin(num); }
	public static double acos(double num) { return Math.acos(num); }
	public static double atan(double num) { return Math.atan(num); }
	public static double radians(double num) { return num * pi() / 180.0; }
	public static double degrees(double num) { return num * 180.0 / pi(); }

	// Abs
	public static int abs(int a) { return a < 0 ? -a : a; }
	public static long abs(long a) { return a < 0 ? -a : a; }
	public static double abs(double a) { return Math.abs(a); }
	public static BigInteger abs(BigInteger a) { return a.abs(); }
	public static BigDecimal abs(BigDecimal a) { return a.abs(); }

	// Exp, log, pow...
	public static double exp(double d) { return Math.exp(d); }
	public static double log(double d) { return Math.log(d); }
	public static double log10(double d) { return Math.log(d) / Math.log(10.0); }
	public static double pow(double a, double b) { return Math.pow(a, b); }
	public static double sqrt(double d) { return Math.sqrt(d); }

	// string
	public static String formatGuidString(String sGuid)	{
		int nLength = sGuid.length();
		boolean bValid = nLength == 32;
		if (bValid) {
			for (int i = 0; i < nLength; ++i) {
				char c = sGuid.charAt( i );
				if (!((c >= '0' && c <= '9') || 
					(c >= 'A' && c <= 'F') || 
					(c >= 'a' && c <= 'f')))
					bValid = false;
			}
		}

		if (!bValid)
			throw new IllegalArgumentException("formatGuidString: invalid guid string");

		String sBuffer;
		String sResult = "";
		for (int i = 0; i < 16; ++i) {
			sBuffer = sGuid.substring( i*2, i*2+2 );
			sResult += sBuffer;

			if (i==3 || i==5 || i==7 || i==9)
				sResult += "-";
		}

		return sResult;
	}

	public static String uppercase(String str) { return str.toUpperCase(); }
	public static String lowercase(String str) { return str.toLowerCase(); }
	
	public static String capitalize(String str)	{
		String sResult = str;
		int nPos = -1;
		while (true) {
			if (nPos < sResult.length() - 1) {
				sResult = sResult.substring(0, nPos + 1) + sResult.substring(nPos + 1, nPos + 2).toUpperCase() + sResult.substring(nPos + 2, sResult.length());
			}
			nPos = sResult.indexOf(" ", nPos + 1);
			if (nPos < 0)
				break;
		}
		return sResult;
	}

	public static int stringCompare(String string1, String string2) {
		return string1.compareTo( string2 );
	}

	public static int stringCompareIgnoreCase(String string1, String string2 ) {
		return string1.compareToIgnoreCase( string2 );
	}

	public static int countSubstring(String str, String substr ) {
		int nResult = 0;
		for (int i = 0; i <= str.length(); ++i)
			if (str.startsWith(substr, i))
				nResult ++;
		
		return nResult;
	}

	public static boolean matchPattern(String str, String pattern ) {
		return str.matches( pattern );
	}

	public static int findSubstring(String str, String substr, int startindex ) {
		int nStart = startindex - 1;
		if (substr.length() == 0)
			return nStart + 1;

		if (nStart <= 0)
			nStart = 0;
		if (nStart >= str.length())
			return 0;

		return str.indexOf( substr, nStart )+1;
	}

	public static int reversefindSubstring(String str, String substr, int endindex ) {
		int nStart = endindex - 1;

		if (substr.length() == 0)
			return nStart + 1;

		if (nStart <= 0)
			return 0;

		return str.lastIndexOf( substr, nStart ) + 1;
	}

	public static String left(String str, int number ) {
		try {
			return str.substring(0, number);
		} catch (IndexOutOfBoundsException e) {
			return str;
		}
	}

	public static String leftTrim(String str ) {
		String s = str;
		int nPosition = 0;
		while (nPosition < s.length() && Character.isWhitespace(s.charAt(nPosition) )) {
			nPosition++;
		}
		try {
			return str.substring(nPosition, str.length());
		} catch (IndexOutOfBoundsException e) {
			return str;
		}
	}

	public static String right(String str, int number ) {
		String s = str;
		try {
			return s.substring(s.length() - number, s.length());
		} catch (IndexOutOfBoundsException e) {
			return str;
		}
	}

	public static String rightTrim(String str ) {
		int nPosition = str.length();
		while (nPosition > 0 && Character.isWhitespace(str.charAt(nPosition-1) )) {
			nPosition--;
		}
		try {
			return str.substring(0, nPosition);
		} catch (IndexOutOfBoundsException e) {
			return str;
		}
	}

	public static String trim(String str, String chars) {
		return trimRight( trimLeft( str, chars), chars);
	}
	
	public static String trimLeft(String str, String chars) {
		for (int i = 0; i < str.length(); ++i) {
			if (chars.indexOf( str.charAt( i)) == -1)
				return str.substring( i);
		}
		return "";
	}

	public static StringBuffer trimLeft(StringBuffer strb, String chars) {
		for (int i = 0; i < strb.length(); ++i) {
			if (chars.indexOf( strb.charAt( i)) == -1)
				return strb.delete(0, i);
		}
		return strb.delete(0, strb.length());
	}

	
	public static String trimRight(String str, String chars) {
		for (int i = str.length() - 1; i >= 0; --i) {
			if (chars.indexOf( str.charAt( i)) == -1)
				return str.substring( 0, i + 1);
		}
		return "";
	}
	
	public static StringBuffer trimRight(StringBuffer str, String chars) {
		for (int i = str.length() - 1; i >= 0; --i) {
			if (chars.indexOf( str.charAt( i)) == -1)
				return str.delete(i + 1, str.length());
		}
		return str.delete(0, str.length());
	}

	public static String replace(String val, String oldstring, String newstring ) {
		if (oldstring.equals(""))
			throw new IllegalArgumentException("The search string passed to the replace function is empty.");
			
		if (val.length() < oldstring.length())
			return val;

		String sResult = val;
		int nPos = sResult.indexOf( oldstring );
		while (nPos >= 0) {
			sResult = sResult.substring( 0, nPos ) + newstring + sResult.substring( nPos+oldstring.length(), sResult.length());
			nPos = sResult.indexOf( oldstring, nPos+newstring.length() );
		}
		return sResult;
	}

	public static String repeatString( String s, int count ) {
		if (count < 0 || count == 0 || s.equals(""))
			return "";

		if (count == 1)
			return s;

		return new String(new char[count]).replace("\0", s);
	}

	public static boolean empty(String val) { return val.length()==0; }

	public static DateTime datetimeAdd(DateTime dt, Duration dur) {
		int months = dur.getYearMonthValue();
		DateTime result = new DateTime(dt);
		result.setMonth(dt.getMonth()+months);
		int yearsOverflow;
		if (result.getMonth() >= 1)
			yearsOverflow = (result.getMonth() - 1) / 12;
		else
			yearsOverflow = (result.getMonth() - 12) / 12;
		result.setMonth(result.getMonth() - yearsOverflow * 12);
		if (yearsOverflow > 0 && result.getYear() < 0 && yearsOverflow >= -result.getYear())
			result.setYear(result.getYear() + yearsOverflow + 1);
		else if (yearsOverflow < 0 && result.getYear() > 0 && -yearsOverflow >= result.getYear())
			result.setYear(result.getYear() + yearsOverflow - 1);
		else
			result.setYear(result.getYear() + yearsOverflow);

		boolean leapYear = (result.getYear() % 4 == 0) && ((result.getYear() % 100 != 0) || (result.getYear() % 400 == 0));
		int daysInMonth = result.getMonth() == 2 ? leapYear ? 29 : 28 : (30 + ((result.getMonth() & 1) ^ ((result.getMonth() >> 3) & 1)));
		if (result.getDay() > daysInMonth)
			result.setDay(daysInMonth);

		long tv = result.getTimeValue() + dur.getDayTimeValue() + result.getTimezoneOffset() * 60000;
		result.setTimeFromTimeValue(tv);
		result.setDateFromTimeValue(tv);
		return result;
	}

	public static Duration datetimeDiff(DateTime datetime1, DateTime datetime2) {
		long resultticks = datetime1.getTimeValue() - datetime2.getTimeValue();
		Duration dur = new Duration( 0, 0, 0, 0, 0, 0, 0.0, false );

		dur.setDayTimeValue(resultticks);
		return dur;
	}

	public static DateTime datetimeFromParts(double year, double month, double day, double hour, double minute, double second, double millisecond, double timezone) {
		DateTime result = datetimeAdd(new DateTime(1,1,1), new Duration((int)year-1, (int)month-1, (int)day-1, (int)hour, (int)minute, (int)second, millisecond*0.001, false)); // false means "not-negative"
		if (timezone >= -1440.0 && timezone <= 1440.0) {
			if ((int) timezone == 0)
				result.setHasTimezone(DateTime.TZ_UTC);
			else {
				result.setHasTimezone(DateTime.TZ_OFFSET);
				result.setTimezoneOffset( (int) timezone );
			}
		}
		return result;
	}

	public static DateTime datetimeFromParts(double year, double month, double day, double hour, double minute, double second, BigDecimal millisecond, double timezone) {
		return datetimeFromParts( year, month, day, hour, minute, second, millisecond.doubleValue(), timezone );
	}

	public static DateTime datetimeFromDateAndTime( DateTime date, DateTime time ) {
		DateTime ret = new DateTime(date);
		ret.setHour( time.getHour() );
		ret.setMinute( time.getMinute() );
		ret.setSecond( time.getSecond() );
		ret.setPartSecond( time.getPartSecond() );
		return ret;
	}
	
	public static DateTime dateFromDatetime(DateTime dt) {
		DateTime ret = new DateTime(dt);
		ret.setHour( 0 );
		ret.setMinute( 0 );
		ret.setSecond( 0 );
		ret.setPartSecond( 0.0 );
		return ret;
	}

	public static DateTime timeFromDatetime(DateTime dt) {
		DateTime ret = new DateTime(dt);
		ret.setYear( 1 );
		ret.setMonth( 1 );
		ret.setDay( 1 );
		return ret;
	}

	public static int yearFromDatetime(DateTime dt) { return dt.getYear(); }
	public static int monthFromDatetime(DateTime dt) { return dt.getMonth(); }
	public static int dayFromDatetime(DateTime dt) { return dt.getDay(); }
	public static int hourFromDatetime(DateTime dt) { return dt.getHour(); }
	public static int minuteFromDatetime(DateTime dt) { return dt.getMinute(); }
	public static int secondFromDatetime(DateTime dt) { return dt.getSecond(); }
	public static BigDecimal millisecondFromDatetime(DateTime dt) { return com.altova.CoreTypes.castToBigDecimal( dt.getPartSecond() * 1000.0 ); }

	public static boolean leapyear(DateTime dt) { int year = dt.getYear(); return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0; }

	public static Duration durationFromParts(double years, double months, double days, double hours, double minutes, double seconds, double millis, boolean negative) {
		return new Duration((int) years, (int) months, (int) days, (int) hours, (int) minutes, (int) seconds, millis * 0.001, negative);
	}

	public static Duration durationFromParts(double years, double months, double days, double hours, double minutes, double seconds, BigDecimal millis, boolean negative) {
		return durationFromParts( years, months, days, hours, minutes, seconds, millis.doubleValue(), negative );
	}

	public static int timezone(DateTime dt) { return dt.getTimezoneOffset(); }
	public static int weekday(DateTime dt) { // 1=sunday,...		//{ return (int) dt.Value.DayOfWeek; }
		long a = ( 14 - dt.getMonth() ) / 12;
		long m = dt.getMonth() + 12 * a - 3;
		long y = dt.getYear() + 4800 - a;

		long JD = dt.getDay() + (153 * m + 2) / 5 + y*365 + y/4 - y/100 + y/400 - 32045; // julian date

		return (int)(JD % 7 + 1);
	}
	public static int weeknumber(DateTime dt) {
		long a = ( 14 - dt.getMonth() ) / 12;
		long m = dt.getMonth() + 12 * a - 3;
		long y = dt.getYear() + 4800 - a;

		long JD = dt.getDay() + (153 * m + 2) / 5 + y*365 + y/4 - y/100 + y/400 - 32045;

		long d4 = (JD+31741 - ( JD % 7 ) ) % 146097 % 36524 % 1461;
		long L = d4/1460;
		long d1 = ((d4-L) % 365) + L;

		return (int)(d1/7+1);
	}

	public static Duration durationAdd(Duration duration1, Duration duration2) {
		Duration dur = new Duration( 0, 0, 0, 0, 0, 0, 0.0, false );
		int ym = duration1.getYearMonthValue() + duration2.getYearMonthValue();
		long dt = duration1.getDayTimeValue() + duration2.getDayTimeValue();

		if ((ym < 0 && dt > 0) || (ym > 0 && dt < 0))
			throw new NotANumberException("Invalid duration result, yearmonth part differs in sign from daytime part.");

		if (ym != 0)
			dur.setYearMonthValue(ym);
		if (dt != 0)
			dur.setDayTimeValue(dt);
		return dur;
	}

	public static Duration durationSubtract(Duration duration1, Duration duration2) {
		Duration dur = new Duration( 0, 0, 0, 0, 0, 0, 0.0, false );
		int ym = duration1.getYearMonthValue() - duration2.getYearMonthValue();
		long dt = duration1.getDayTimeValue() - duration2.getDayTimeValue();

		if ((ym < 0 && dt > 0) || (ym > 0 && dt < 0))
			throw new NotANumberException("Invalid duration result, yearmonth part differs in sign from daytime part.");

		if (ym != 0)
			dur.setYearMonthValue(ym);
		if (dt != 0)
			dur.setDayTimeValue(dt);
		return dur;
	}

	public static int yearFromDuration(Duration dur) { return dur.getYear(); }
	public static int monthFromDuration(Duration dur) { return dur.getMonth(); }
	public static int dayFromDuration(Duration dur) { return dur.getDay(); }
	public static int hourFromDuration(Duration dur) { return dur.getHour(); }
	public static int minuteFromDuration(Duration dur) { return dur.getMinute(); }
	public static int secondFromDuration(Duration dur) { return dur.getSecond(); }
	public static BigDecimal millisecondFromDuration(Duration dur) { return com.altova.CoreTypes.castToBigDecimal( dur.getPartSecond() * 1000.0 ); }

	public static DateTime now() {return DateTime.now();}
	
	public static DateTime convertToUTC(DateTime dt) {
		long time = dt.getTimeValue();
				
		DateTime dt2 = new DateTime();
		dt2.setDateFromTimeValue(time);
		dt2.setTimeFromTimeValue(time);	
		return dt2;
	}
	
	public static DateTime removeTimezone(DateTime dt) {
		DateTime dt0= new DateTime(dt);
		dt0.setHasTimezone(DateTime.TZ_MISSING);
		return dt0;
	}

	public static double random() { return java.lang.Math.random(); }

	// ---- generator functions --------------

	// Creates a global-unique-identifier as hexadecimal-encoded string.
	public static String createGuid(RuntimeContext context) throws Exception {
		String guid = UUID.randomUUID().toString().replaceAll("[-:]","");
		return guid;
	}
	
	// qname functions
	public static javax.xml.namespace.QName qname(String uri, String localname) {
		int i = localname.indexOf(":");
		if (i < 0)
			return new javax.xml.namespace.QName(uri, localname);
			
		return new javax.xml.namespace.QName(uri, localname.substring(i+1), localname.substring(0, i));
	}
	public static String qnameAsString(javax.xml.namespace.QName qn) {return qn.toString();}
	public static javax.xml.namespace.QName stringAsQName(String s) { 
		String uri = "";
		if (s.startsWith("{")) {
			int namespaceEnd = s.indexOf('}');
			if (namespaceEnd < 0) throw new RuntimeException("Invalid string-as-QName format");
			uri = s.substring(1, namespaceEnd);
			s = s.substring(namespaceEnd + 1);
		}
		String prefix = "";
		int prefixEnd = s.indexOf(':');
		
		if (prefixEnd >= 0) {
			prefix = s.substring(0, prefixEnd);
			s = s.substring(prefixEnd + 1);
		}
		
		return new javax.xml.namespace.QName(uri, s, prefix); 
	}
}

