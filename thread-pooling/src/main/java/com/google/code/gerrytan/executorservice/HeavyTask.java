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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simulates a long running task. Takes an id when constructed, and simply sleeps for 10
 * seconds. A log message will be printed when task started and finished.
 * 
 * @author gerrytan
 */
public class HeavyTask implements Runnable {
  private static Logger logger = LoggerFactory.getLogger(HeavyTask.class);
  private int taskId;
  
  public HeavyTask(int taskId) {
    this.taskId = taskId;
  }
  
  public void run() {
    logger.info(String.format("Task #%s is starting on thread %s...", taskId, Thread.currentThread().getName()));
    
    try {
      Thread.sleep(10 * 1000);
    } catch (InterruptedException e) {
      logger.error(String.format("Task #%s running on thread %s is interrupted", taskId, Thread.currentThread().getName()), e);
    }
    
    logger.info(String.format("Task #%s on thread %s finished", taskId, Thread.currentThread().getName()));
  }
}
