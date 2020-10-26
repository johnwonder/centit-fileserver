package com.centit.fileserver.task;

import com.centit.fileserver.common.FileTaskInfo;
import com.centit.fileserver.po.FileInfo;
import com.centit.fileserver.pretreat.FilePretreatUtils;
import com.centit.fileserver.service.FileInfoManager;
import com.centit.fileserver.utils.SystemTempFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.function.Consumer;

/**
 * zip压缩文件
 */
@Service
public class ZipFileOpt extends FileOpt implements Consumer<FileTaskInfo> {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileOpt.class);

    @Autowired
    private FileInfoManager fileInfoManager;

    @Override
    public void accept(FileTaskInfo fileOptTaskInfo) {
        String fileId = fileOptTaskInfo.getFileId();
        long fileSize = fileOptTaskInfo.getFileSize();
        FileInfo fileInfo = fileInfoManager.getObjectById(fileId);
        String originalTempFilePath = SystemTempFileUtils.getTempFilePath(fileInfo.getFileMd5(), fileSize);
        try {
            String zipFilePath = FilePretreatUtils.zipFile(fileInfo, originalTempFilePath);
            if (null != zipFilePath) {
                save(zipFilePath, fileInfo.getFileMd5(), new File(zipFilePath).length());
                fileInfoManager.updateObject(fileInfo);
                logger.info("zip压缩文件完成");
            }
        } catch (Exception e) {
            logger.error("zip压缩文件时出错！", e);
        }
    }
}
