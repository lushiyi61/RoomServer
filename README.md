# RoomServer

### 本地调试
- com.qq.tars.server.startup.Main
- -Dconfig=E:\git_l\RoomServer\target\classes\JFGame.RoomServer.config.conf

### 新增tars
- 修改配置文件：

        <tars2JavaConfig>
            <tarsFiles>
                <tarsFile>${basedir}/src/main/resources/RoomServant.tars</tarsFile>
            </tarsFiles>
            <tarsFileCharset>UTF-8</tarsFileCharset>
            <servant>true</servant>
            <srcPath>${basedir}/src/main/java</srcPath>
            <charset>UTF-8</charset>
            <packagePrefixName>com.bzw.tars</packagePrefixName>
        </tars2JavaConfig>
        
- 配置文件说明：
    - <tarsFile>${basedir}/src/main/resources/RoomServant.tars</tarsFile> ：新增tars文件
    - <servant>true</servant> ：true为服务端，false为客户端
- 编译指令：mvn tars:tars2java
- 打包指令：mvn package生成war包，后续可以管理系统进行发布。
- 架构图：https://www.draw.io/，请用这个软件打开Diagram.xml
