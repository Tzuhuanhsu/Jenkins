pipeline
{
    agent {label "master"}
    parameters{
        booleanParam(name:"isDebug", defaultValue:false,description:"是否為Debug")
        choice(name:"EnvType", choices:['Web', 'Android', 'iOS'],description:"編譯版本")
    }
    environment{
        APP_VERSION = ""
        RES_DIR = "/Users/richard/Desktop/cocosCreator/JumperGame"
        COOCS_ENGINE = "/Applications/CocosCreator/Creator/3.4.2/CocosCreator.app/Contents/MacOS/CocosCreator"
        WEB_CONFIG = "/Users/richard/Desktop/cocosCreator/JumperGame/build/buildConfig_web-mobile.json"
        ANDROID_PROJ = "/Users/richard/Desktop/cocosCreator/ANDROID_PROJ"
        IOS_PROJ = "/Users/richard/Desktop/cocosCreator/IOS_PROJ"
    }
    stages{
        stage("版本號生成"){
            failFast true
            steps{
                script{
                    APP_VERSION = "1.0.0"
                    echo "version is ${APP_VERSION}"
                }
                
            }
        }
        stage("Build Cocos"){
            steps{
                echo "pass build cocos"
            }
        }
        stage("Build APP"){
            parallel {
                stage("建構 Android"){
                    agent{label "android_agent"}
                    steps{
                        echo "建構 Android "
                        dir(env.ANDROID_PROJ){
                            script{
                                status = sh(
                                            script:'${COOCS_ENGINE}  --project . --build "configPath=${WEB_CONFIG};"',
                                            returnStatus: true
                                        )
                                if (status == 36)
                                {
                                    compileSuccess = true
                                }
                                else
                                {
                                    compileSuccess = false
                                }
                            }
                        }
                    }
                }
                stage("建構 iOS"){
                    agent{label "ios_agent"}
                    steps{
                        echo "建構 iOS"
                        dir(env.IOS_PROJ){
                            script{
                                status = sh(
                                            script:'${COOCS_ENGINE}  --project . --build "configPath=${WEB_CONFIG};"',
                                            returnStatus: true
                                        )
                                if (status == 36)
                                {
                                    compileSuccess = true
                                }
                                else
                                {
                                    compileSuccess = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    post
    {
        always
        {
            //刪除暫存資料夾
            dir("${env.IOS_PROJ}@tmp") {
                deleteDir()
            }
           
            //刪除暫存資料夾
            dir("${env.ANDROID_PROJ}@tmp") {
                deleteDir()
            }
        }
    }
}

