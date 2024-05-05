package sierra.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class text {
	private static final String BUNDLE_NAME = "sierra.habbohotel.commands.MUS"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private text() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
