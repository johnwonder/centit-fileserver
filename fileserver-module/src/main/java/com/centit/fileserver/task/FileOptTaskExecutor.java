package com.centit.fileserver.task;

import com.centit.fileserver.common.FileTaskInfo;
import com.centit.fileserver.common.FileTaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FileOptTaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(FileOptTaskExecutor.class);

    private FileTaskQueue fileOptTaskQueue;

    private Map<Integer, Consumer<FileTaskInfo>> fileOptList;


    public FileOptTaskExecutor(){
        fileOptList = new HashMap<>(20);
    }

    public void addFileOpt(int taskType, Consumer<FileTaskInfo> fileOpt){
        fileOptList.put(taskType, fileOpt);
    }

    public FileTaskQueue getFileOptTaskQueue() {
        return fileOptTaskQueue;
    }

    public void setFileOptTaskQueue(FileTaskQueue fileOptTaskQueue) {
        this.fileOptTaskQueue = fileOptTaskQueue;
    }

    /*@PostConstruct
    public void doTask() {
        new Thread(new FileOptTask()).start();
    }
    */
    public void doFileOptJob() {
        FileTaskInfo taskInfo = fileOptTaskQueue.get();
        while(taskInfo != null){
            int taskType = taskInfo.getTaskType();
            fileOptList.get(taskType).accept(taskInfo);
            taskInfo = fileOptTaskQueue.get();
        }
    }

   /* class FileOptTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    FileOptTaskInfo taskInfo = fileOptTaskQueue.get();
                    if (null == taskInfo) {
                        Thread.sleep(5000);
                    } else {
                        int taskType = taskInfo.getTaskType();
                        fileOptList.get(taskType).accept(taskInfo);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }*/
}
