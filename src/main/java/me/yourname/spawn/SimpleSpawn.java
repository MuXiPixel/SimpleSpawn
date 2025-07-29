package me.yourname.spawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleSpawn extends JavaPlugin {
    
    @Override
    public void onEnable() {
        getLogger().info("SpawnPlugin 已加载 - 使用 /spawn [玩家名]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            // 无参数 - 玩家传送自己
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "控制台用法: /spawn <玩家名>");
                    return true;
                }
                
                Player player = (Player) sender;
                teleportToSpawn(player);
                return true;
            }
            
            // 有参数 - 传送指定玩家(需要权限)
            if (args.length == 1) {
                if (!sender.hasPermission("simplespawn.teleport.others")) {
                    sender.sendMessage(ChatColor.RED + "你没有权限传送其他玩家");
                    return true;
                }
                
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "找不到玩家: " + args[0]);
                    return true;
                }
                
                teleportToSpawn(target);
                sender.sendMessage(ChatColor.GREEN + "已将玩家 " + target.getName() + " 传送至出生点");
                return true;
            }
            
            sender.sendMessage(ChatColor.RED + "用法: /spawn [玩家名]");
            return true;
        }
        return false;
    }
    
    private void teleportToSpawn(Player player) {
        Location spawn = player.getWorld().getSpawnLocation();
        Location adjusted = spawn.add(0.5, 0, 0.5); // 调整到方块中心
        adjusted.setPitch(player.getLocation().getPitch());
        adjusted.setYaw(player.getLocation().getYaw());
        
        player.teleport(adjusted);
        player.sendMessage(ChatColor.GREEN + "已传送至世界出生点");
    }
}