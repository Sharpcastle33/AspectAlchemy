package com.gmail.sharpcastle33.aspects;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the Aspects list for a given custom item
 */
public class AspectItemData {
	public String configName;
	public String displayName;
	public Map<Aspect, Integer> aspects;

	public AspectItemData(String name, String displayName, Map<Aspect, Integer> aspects) {
		this.configName = name;
		this.displayName = displayName.replace('&', '§');
		this.aspects = aspects;
	}

	public AspectItemData() {
		this(null, null, new HashMap<Aspect, Integer>());
	}
}
