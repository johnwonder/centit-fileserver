package com.centit.fileserver.service.impl;

import com.centit.fileserver.dao.FileLibraryAccessDao;
import com.centit.fileserver.po.FileLibraryAccess;
import com.centit.fileserver.service.FileLibraryAccessManager;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.support.database.utils.PageDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * FileLibraryAccess  Service.
 * create by scaffold 2020-08-18 13:38:15
 *
 * @author codefan@sina.com
 * 项目库授权信息
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FileLibraryAccessManagerImpl extends BaseEntityManagerImpl<FileLibraryAccess, String, FileLibraryAccessDao>
    implements FileLibraryAccessManager{

    private static final Logger logger = LoggerFactory.getLogger(FileLibraryAccessManager.class);

    @Autowired
    private FileLibraryAccessDao fileLibraryAccessDao;

    @Override
    public void updateFileLibraryAccess(FileLibraryAccess fileLibraryAccess) {
        fileLibraryAccessDao.updateObject(fileLibraryAccess);
        fileLibraryAccessDao.saveObjectReferences(fileLibraryAccess);
    }

    @Override
    public void createFileLibraryAccess(FileLibraryAccess fileLibraryAccess) {
        fileLibraryAccessDao.saveNewObject(fileLibraryAccess);
        fileLibraryAccessDao.saveObjectReferences(fileLibraryAccess);
    }

    @Override
    public List<FileLibraryAccess> listFileLibraryAccess(Map<String, Object> param, PageDesc pageDesc) {
        return fileLibraryAccessDao.listObjects(param, pageDesc);
    }


    @Override
    public FileLibraryAccess getFileLibraryAccess(String accessId) {
        return fileLibraryAccessDao.getObjectById(accessId);
    }

    @Override
    public void deleteFileLibraryAccess(String accessId) {
        fileLibraryAccessDao.deleteObjectById(accessId);
    }

}

