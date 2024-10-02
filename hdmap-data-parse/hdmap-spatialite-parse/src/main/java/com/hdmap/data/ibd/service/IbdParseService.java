package com.hdmap.data.ibd.service;

import com.hdmap.data.ibd.dto.IbdUpload;

public interface IbdParseService {
    boolean parse(IbdUpload params) throws Exception;
}
