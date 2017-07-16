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

import java.util.function.Function;

public class NumberUtils {
	
	private static final int ORDINAL_TEN_LIMIT = 10000000;
	
	public static Function<Enum<?>, Integer> ordinalPowerOfTen() {
		return (e) -> {
				int pwrOfTen = 1;
				int ordinal = e.ordinal();
				for (int n = 10; (ordinal > n) ? n < ORDINAL_TEN_LIMIT || ordinal % n > 1 : false; n = n * 10) pwrOfTen = n;
				
				return pwrOfTen;
			};
	}
	public static int ordinalPowerOfTen(Enum<?> e) {
		return ordinalPowerOfTen().apply(e);
	}
	
	public static Function<Enum<?>[], Integer> ordinalArrayAddress() {
		return (eArr) -> {
				int address = 0;
				int[] addressMultiples = new int[eArr.length];
				
				for (int i = 0; i < eArr.length; i++) {
					addressMultiples[i] = (i != 0) ?
							ordinalPowerOfTen(eArr[i]) * addressMultiples[i - 1]
						:
							ordinalPowerOfTen(eArr[i]);
					address = address + eArr[i].ordinal() * ((i == 0) ?
							1 : (addressMultiples[i - 1] == 1) ?
									addressMultiples[i] * 10 : addressMultiples[i]
						);
				}
				
				return address;
			};
	}
	public static int ordinalArrayAddress(Enum<?>[] eArr) {
		return ordinalArrayAddress().apply(eArr);
	}
	public static int ordinalAddress(Enum<?>... eVars) {
		return ordinalArrayAddress(eVars);
	}
}
