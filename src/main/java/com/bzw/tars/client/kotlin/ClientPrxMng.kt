package com.bzw.tars.client.kotlin


import com.bzw.tars.client.tars.jfgame.TarsRouterPrx
import com.bzw.tars.client.tars.tarsgame.IGameMessagePrx
import com.qq.tars.client.CommunicatorConfig
import com.qq.tars.client.CommunicatorFactory
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.error.YAMLException
import java.io.FileInputStream


/**
 * @创建者 zoujian
 * @创建时间 2018/7/11
 * @描述
 */
class ClientPrxMng {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = ClientPrxMng();
    }

    // 其他服务连接信息
    private val clientPrxMap: MutableMap<String, Any> = mutableMapOf();
    private val clientClassMap: MutableMap<String, Class<out Any>> = mutableMapOf();

    /*
     * @description 在此添加其他服务对应类
     * =====================================
     * @author zoujian
     * @date 2018/8/14 16:44
     */
    private constructor() {
        this.clientClassMap.put("RouterObj", TarsRouterPrx::class.java);
    }

    /////////////////////////////////////////////////
    fun loadConfig() {
        println(String.format("loadConfig start"));
        val cfg = CommunicatorConfig();
        val communicator = CommunicatorFactory.getInstance().getCommunicator(cfg)

        try {
            // 读其他服务端配置
            val yaml = Yaml()
            val url = this::class.java.classLoader.getResource("RoomServer.yaml")
            if (url != null) {
                //获取test.yaml文件中的配置数据，然后转换为List<ClientPrxBase>，
                val listClientPrxBase = yaml.loadAs(FileInputStream(url.file), ClientPrxConf::class.java);

                println(listClientPrxBase.clientPrxList)
                for (clientPrx in listClientPrxBase.clientPrxList) {
                    val server = clientPrx.get("server")!!
                    val locator = clientPrx.get("locator")!!
                    this.clientPrxMap.put(server, communicator.stringToProxy(
                            this.clientClassMap.get(server) ?: IGameMessagePrx::class.java,
                            locator)
                    );
                    println(String.format("clientPrx：%s,%s", server, this.clientPrxMap.get(server).toString()));
                }
            }
            println(String.format("loadConfig success"));
        } catch (e: FileSystemException) {
            e.printStackTrace();
        } catch (e: YAMLException) {
            e.printStackTrace();
        }
    };

    fun getClientPrx(prxName: String): Any? {
        return this.clientPrxMap.get(prxName);
    }

    fun getRouterPrx(): TarsRouterPrx {
        return this.clientPrxMap.get("RouterObj")!! as TarsRouterPrx;
    };
}