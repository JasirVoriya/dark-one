package cn.darkone.framework.common.bytebye.initializer;

import cn.darkone.framework.common.bytebye.handler.base.PipeLineHandler;

public interface PipelineInitializer<P extends PipeLineHandler> {
    void initHandle(P handle);
}
