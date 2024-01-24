# Jenkins

Jenkins project

## distributedFrame

分散式的 Jenkins 範例

**step1 =>** 首先新增 jenkins node (以下為我設定的 node agent)

    -- android_agent(用來編譯 android 專案)

    -- ios_agent(用來編譯 ios 專案)

**step2 =>** 配合 pipeline 的語法使用並列(parallel))執行的方式，同時執行 android_agent(agent{label "android_agent"})、	 

    ios_agent(agent{label "ios_agent"})
