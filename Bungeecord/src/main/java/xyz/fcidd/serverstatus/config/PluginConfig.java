package xyz.fcidd.serverstatus.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.osiris.dyml.DreamYaml;

import net.md_5.bungee.api.plugin.Plugin;

public class PluginConfig {

    private static DreamYaml config;

    /**
     * 加载配置文件
     * 
     * @param plugin 插件
     * @throws Exception 抛出一大堆，不过无所谓，反正catch到了直接卸载插件
     */
    public static void loadConfig(Plugin plugin) throws Exception {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            file.mkdirs();
            file.createNewFile();
        }
        if (file.exists()) {
            DreamYaml config = new DreamYaml(file);
            config.load();
            config.add("socket-port").setDefValue("556").setComment("内置服务器端口，默认端口为556");
            config.add("translate-servername").setDefValues("server1: 例子1", "server2: 例子2")
                    .setComments("将服务器id翻译成名称，使用如下格式：", "- server1: 例子1", "- server2: 例子2");
            config.add("server-started-broadcast").setDefValue("&6${server_translation} is online now!")
                    .setComment("支持颜色代码如“&6”，转义“\\&” -> “&”，使用“${server_translation}”代表上下线的服务器");
            config.add("server-closed-broadcast").setDefValue("&6${server_translation} is offline now!");
            config.save();
            PluginConfig.config = config;
        } else {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
    }

    /**
     * 获取服务器名称
     * 
     * @param serverID bc端配置文件中的服务器ID
     * @return 翻译后的服务器名称字符串
     */
    public static String getTranslateServername(String serverID) {
        List<String> serverNames = config.getLoadedModuleByKeys("translate-servername").getValues();
        for (String serverKY : serverNames) {
            String[] serverName = serverKY.replace("\n", "").replaceAll("\\s", "").split(":");
            if (serverName[0].equals(serverID))
                return serverName[1];
        }
        return serverID;
    }

    /**
     * 设置服务器名称
     * 
     * @param serverID            bc端配置文件中的服务器ID
     * @param translateServerName 翻译后的服务器名称字符串
     */
    public static void setTranslateServername(String serverID, String translateServerName) throws Exception {
        config.getLoadedModuleByKeys("translate-servername").addValue(serverID + ": " + translateServerName);
        config.save();
    }

    /**
     * 获取服务器上线提示语
     * 
     * @return 服务器上线提示语
     */
    public static String getServerStartedBroadcast() {
        return config.getLoadedModuleByKeys("server-started-broadcast").getValue();
    }

    /**
     * 设置服务器上线提示语
     * 
     * @param serverStartedBroadcast 服务器上线提示语
     * @throws Exception 丢出去不管
     */
    public static void setServerStartedBroadcast(String serverStartedBroadcast) throws Exception {
        config.getLoadedModuleByKeys("server-started-broadcast").setValue(serverStartedBroadcast);
        config.save();
    }

    /**
     * 获取服务器下线提示语
     * 
     * @return 服务器上线提示语
     */
    public static String getServerClosingBroadcast() {
        return config.getLoadedModuleByKeys("server-closed-broadcast").getValue();
    }

    /**
     * 设置服务器下线提示语
     * 
     * @param serverStartedBroadcast 服务器下线提示语
     * @throws Exception 丢出去不管
     */
    public static void setServerClosingBroadcast(String serverClosingBroadcast) throws Exception {
        config.getLoadedModuleByKeys("server-closed-broadcast").setValue(serverClosingBroadcast);
        config.save();
    }

    /**
     * 获取通讯监听端口号
     * 
     * @return 通讯监听端口号
     */
    public static int getSocketPort() {
        return config.getLoadedModuleByKeys("socket-port").asInt();
    }

    /**
     * 设置通讯监听端口号
     * 
     * @param port 通讯监听端口号
     */
    public static void setSocketPort(int port) throws Exception {
        config.getLoadedModuleByKeys("socket-port").setValue(String.valueOf(port));
        config.save();
    }
}
