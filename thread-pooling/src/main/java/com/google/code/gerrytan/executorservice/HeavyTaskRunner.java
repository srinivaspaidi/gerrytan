/*
 * Copyright 2012 Gerry Tan (http://gerrytan.wordpress.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.gerrytan.executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * Demonstrate a task runner service that utilizes thread pool obtained from executor
 * service API
 */
@Component
public class HeavyTaskRunner {
  private ExecutorService executorService;
  private static final int NUM_THREADS = 2;
  private int taskCounter = 1;
  
  public HeavyTaskRunner() {
    executorService = Executors.newFixedThreadPool(NUM_THREADS);
  }
  
  /**
   * Create a new {@link HeavyTask} and submit it to thread pool for execution
   */
  public int runTask() {
    int nextTaskId;
    synchronized(this) {
      nextTaskId = taskCounter++;
    }
    executorService.submit(new HeavyTask(nextTaskId));
    return nextTaskId;
  }
}
