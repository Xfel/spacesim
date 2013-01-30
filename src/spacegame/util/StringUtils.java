package spacegame.util;

public class StringUtils {

	public static boolean isBlank(String s) {
		if (s == null)
			return false;

		return s.trim().isEmpty();
	}

}
