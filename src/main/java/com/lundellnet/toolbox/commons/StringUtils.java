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

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class StringUtils {

	private static final char UNDERSCORE = '_';
	
	public static final UnaryOperator<StringBuilder> SNAKE_TO_CAMEL_CASE = (text) -> {
			return manipulateString(new StringBuilder(text), (i, sb) -> {
					if (sb.charAt(i) == UNDERSCORE) {
						sb.deleteCharAt(i);
						sb.replace(i,  i + 1,  String.valueOf(Character.toUpperCase(sb.charAt(i))));
					}
					
					return sb.toString();
				});
		};
		
	/*public static final UnaryOperator<StringBuilder> UPPERCASE_SNAKE_TO_CAMEL_CASE = (text) -> {
			final StringBuilder builder = new StringBuilder(text);
		
			return manipulateString(builder, (i, sb) -> {
					if (sb.charAt(i) == UNDERSCORE) {
						sb.deleteCharAt(i);
						
						
						
						
						int underscoreIndex = String.indexOf(UNDERSCORE);
						String upperCasedText = builder.substring(i, underscoreIndex);
						
						sb.replace(i, builder.indexOf(String.valueOf(UNDERSCORE)), String.valueOf());
					}
					
					return sb.toString();
				});
		};*/
		
	private static final UnaryOperator<String> UPPERCASE_FIRST_LETTER = (s) -> {
			return s.replaceAll("^(\\w?)", "$1");
		};
		
	private static StringBuilder manipulateString(StringBuilder sb, BiFunction<Integer, StringBuilder, String> manipulator) {
		for (int i = 0; i < sb.length(); i++) {
			manipulator.apply(i, sb);
		}
		
		return sb;
	}
}
