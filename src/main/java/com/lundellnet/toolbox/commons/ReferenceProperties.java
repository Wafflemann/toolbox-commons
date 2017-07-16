/*
 Copyright 2017 Appropriate Technologies LLC.

 This file is part of toolbox-commons, a component of the Lundellnet Java Toolbox.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.lundellnet.toolbox.commons;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ReferenceProperties {
	private static final Logger REF_LOG = Logger.getLogger(ReferenceProperties.class);
	private static final int CHAIN_LIMIT = 256;
	private static final Pattern REF_PATTERN = Pattern.compile("(\\$\\()([\\w\\.]+)(\\))");

	@SafeVarargs
	protected static Properties processProps(Properties baseProps, Properties... propArg) {
		for (Properties refs : propArg) {
			baseProps = mergeProps(refs, baseProps);
		}
		
		for (Enumeration<?> e = baseProps.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			
			baseProps.put(key, substitute(baseProps, key));
		}
		
		return baseProps;
	}
	
	@SafeVarargs
	protected static Properties mergeProps(Properties... propArg) {
		Properties merged = new Properties();
		
		for (int i = propArg.length - 1; i >= 0; i--) {
			for (Map.Entry<Object, Object> e : propArg[i].entrySet()) {
				if (e != null) {
					merged.put(e.getKey(), e.getValue());
				}
			}
		}
		
		return merged;
	}
	
	protected static String substitute(Properties props, String keyToSubstitute) {
		Deque<String> chain = new ArrayDeque<>();
		
		chain.push(keyToSubstitute);
		
		return substitute(props, chain);
	}
	
	protected static String substitute(Properties props, Deque<String> subChain) {
		return substitute(props, subChain, CHAIN_LIMIT);
	}
	
	protected static String substitute(Properties props, Deque<String> subChain, int maxStack) {
		if (subChain == null || subChain.size() == 0) {
			return null;
		} else if (subChain.size() > maxStack) {
			throw new StackOverflowError("Too many substitutions in the substitute chain. CHAIN: " + subChain.toString());
		}
		
		String refKey = subChain.peek();
		String refValue = (String) props.get(refKey);
		
		if (refValue == null) {
			REF_LOG.warn("Could not find the referral key: " + refKey);
			
			return "";
		} else {
			for (Matcher m = REF_PATTERN.matcher(refValue); m.find(); m = REF_PATTERN.matcher(refValue)) {
				String keyOfRefferal = m.group(2);
				subChain.push(keyOfRefferal);
				
				refValue = refValue.replace(m.group(0), substitute(props, subChain, maxStack));
			}
			
			subChain.pop();
			
			return refValue;
		}
	}
	
	private final Properties properties;
	
	protected ReferenceProperties(Properties parentProps, Properties props) {
		properties = processProps(parentProps, props);
	}
	
	protected Properties properties() {
		return properties;
	}
	
	public ReferenceProperties(Properties props) {
		properties = processProps(new Properties(), props);
	}
	
	public ReferenceProperties spawnChild(Properties childProps) {
		return new ReferenceProperties(properties, childProps);
	}
	
	public String getValue(String key) {
		return properties.getProperty(key);
	}
}
