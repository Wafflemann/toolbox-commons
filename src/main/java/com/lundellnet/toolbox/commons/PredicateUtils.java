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

import java.util.concurrent.Future;
import java.util.function.Predicate;

public class PredicateUtils {
  
    public static final Predicate<?> IS_NULL = (t) -> t == null;
    public static final Predicate<?> IS_NOT_NULL = (t) -> t != null;
    
    public static final Predicate<Future<?>> FUTURE_TASK_COMPLETED = (f) -> f.isDone();
    
	public static <T> boolean isNull(T t) {
	  return typeCaptureAndTest(IS_NULL, t);
	}
	
	public static <T> boolean isNotNull(T t) {
	  return typeCaptureAndTest(IS_NOT_NULL, t);
	}
	
	public static <T> boolean futureTaskCompleted(Future<T> future) {
	  return typeCaptureAndTest(FUTURE_TASK_COMPLETED, future);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> boolean typeCaptureAndTest(Predicate<?> predicate, T t) {
		return ((Predicate<T>) predicate).test(t);
	}
}
